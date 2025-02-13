package nl.rabobank.customer.statementprocessor.service;

import nl.rabobank.customer.statementprocessor.control.service.StatementProcessorService;
import nl.rabobank.customer.statementprocessor.boundary.dto.CustomerStatement;
import nl.rabobank.customer.statementprocessor.boundary.dto.Report;
import nl.rabobank.customer.statementprocessor.boundary.dto.ValidationResult;
import nl.rabobank.customer.statementprocessor.control.events.StatementsEventPublisher;
import nl.rabobank.customer.statementprocessor.control.exception.InvalidFileException;
import nl.rabobank.customer.statementprocessor.control.mapper.CustomerStatementToStatementMapper;
import nl.rabobank.customer.statementprocessor.control.mapper.ReportMapper;
import nl.rabobank.customer.statementprocessor.control.parser.CsvFileParser;
import nl.rabobank.customer.statementprocessor.control.parser.XmlFileParser;
import nl.rabobank.customer.statementprocessor.util.StatementValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static nl.rabobank.customer.statementprocessor.util.StatementConstants.DUPLICATE_REFERENCE_DETECTED;
import static nl.rabobank.customer.statementprocessor.util.StatementConstants.INVALID_END_BALANCE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatementProcessorServiceTest {

    @Mock
    private CsvFileParser csvFileParser;
    @Mock
    private XmlFileParser xmlFileParser;
    @Mock
    private StatementValidator validator;
    @Mock
    private ReportMapper reportMapper;
    @Mock
    private StatementsEventPublisher statementsEventPublisher;
    @Mock
    private CustomerStatementToStatementMapper customerStatementToStatementMapper;
    @Mock
    private MultipartFile file;

    @InjectMocks
    private StatementProcessorService statementProcessorService;

    private static final String CSV_FILENAME = "test.csv";
    private static final String XML_FILENAME = "test.xml";
    private static final String UNSUPPORTED_FILENAME = "test.txt";
    private static final String NULL_FILENAME = null;

    private List<CustomerStatement> customerStatements;
    private List<ValidationResult> validationResults;
    private Report report;

    @BeforeEach
    void setUp() {
        CustomerStatement customerStatement = new CustomerStatement(123L, "NL1234567890",
                new BigDecimal("1000.00"), new BigDecimal("+200.00"), "Test", new BigDecimal("1200.00"));
        customerStatements = List.of(customerStatement);
        validationResults = new ArrayList<>();
        report = new Report(1L, validationResults);
    }

    @Test
    void shouldProcessCsvFileSuccessfully() {
        // Given
        prepareFileMock(CSV_FILENAME);
        when(csvFileParser.parseFile(file)).thenReturn(customerStatements);
        when(validator.validateParsedStatements(customerStatements)).thenReturn(validationResults);
        when(reportMapper.mapValidationResultsToReport(validationResults)).thenReturn(report);
        when(customerStatementToStatementMapper.toStatementList(customerStatements)).thenReturn(new ArrayList<>());

        Report result = statementProcessorService.process(file);

        assertThat(result.failedRecords()).isEmpty();
        verify(validator, times(1)).validateParsedStatements(customerStatements);
        verify(reportMapper, times(1)).mapValidationResultsToReport(validationResults);
        verify(csvFileParser, times(1)).parseFile(file);
        verify(statementsEventPublisher).publish(anyList());
    }

    @Test
    void shouldProcessXmlFileSuccessfully() {
        prepareFileMock(XML_FILENAME);
        when(xmlFileParser.parseFile(file)).thenReturn(customerStatements);
        when(validator.validateParsedStatements(customerStatements)).thenReturn(validationResults);
        when(reportMapper.mapValidationResultsToReport(validationResults)).thenReturn(report);

        Report result = statementProcessorService.process(file);

        // Then
        assertThat(result.failedRecords()).isEmpty();
        assertThat(1L).isEqualTo(result.reportId());
        assertThat(validationResults).isEqualTo(result.failedRecords());
        verify(validator, times(1)).validateParsedStatements(customerStatements);
        verify(reportMapper, times(1)).mapValidationResultsToReport(validationResults);
        verify(statementsEventPublisher).publish(anyList());
    }

    @Test
    void shouldThrowInvalidFileExceptionForUnsupportedFileType() {
        // Given
        prepareFileMock(UNSUPPORTED_FILENAME);

        // When / Then
        assertThatThrownBy(() -> statementProcessorService.process(file))
                .isInstanceOf(InvalidFileException.class)
                .hasMessage("Unsupported file type");
    }

    @Test
    void shouldThrowInvalidFileExceptionForMissingFileName() {
        // Given
        prepareFileMock(NULL_FILENAME);

        // When / Then
        assertThatThrownBy(() -> statementProcessorService.process(file))
                .isInstanceOf(InvalidFileException.class)
                .hasMessage("File name is missing from the file");
    }

    @Test
    void shouldReturnReportWithValidationErrors() {
        // Given
        prepareFileMock(CSV_FILENAME);

        // Simulate customerStatements with validation errors
        customerStatements = createCustomerStatementsWithErrors();
        List<ValidationResult> validationErrors = createValidationErrors();
        when(csvFileParser.parseFile(file)).thenReturn(customerStatements);
        when(validator.validateParsedStatements(customerStatements)).thenReturn(validationErrors);
        when(reportMapper.mapValidationResultsToReport(validationErrors)).thenReturn(new Report(1L, validationErrors));

        // When
        Report result = statementProcessorService.process(file);

        // Then
        assertThat(result.failedRecords()).isNotEmpty();
        assertThat(result.reportId()).isEqualTo(1L);
        assertThat(validationErrors).isEqualTo(result.failedRecords());
        assertThat(result.failedRecords())
                .hasSize(3)
                .extracting(ValidationResult::errorMessage)
                .containsExactly(DUPLICATE_REFERENCE_DETECTED, DUPLICATE_REFERENCE_DETECTED, INVALID_END_BALANCE);

        verify(validator, times(1)).validateParsedStatements(customerStatements);
        verify(csvFileParser, times(1)).parseFile(file);
    }

    private void prepareFileMock(String filename) {
        when(file.getOriginalFilename()).thenReturn(filename);
    }

    private List<CustomerStatement> createCustomerStatementsWithErrors() {
        CustomerStatement statement1 = new CustomerStatement(123L, "NL1234567890",
                new BigDecimal("1000.00"), new BigDecimal("+200.00"), "Test 1", new BigDecimal("1200.00"));
        CustomerStatement statement2 = new CustomerStatement(123L, "NL1234567890",
                new BigDecimal("1500.00"), new BigDecimal("+300.00"), "Test 2", new BigDecimal("1800.00"));
        CustomerStatement statement3 = new CustomerStatement(125L, "NL1234567891",
                new BigDecimal("2000.00"), new BigDecimal("+400.00"), "Test 3", new BigDecimal("2400.00"));
        CustomerStatement statement4 = new CustomerStatement(126L, "NL1234567891",
                new BigDecimal("2000.00"), new BigDecimal("+400.00"), "Test 4", new BigDecimal("1400.00"));
        return List.of(statement1, statement2, statement3, statement4);
    }

    private List<ValidationResult> createValidationErrors() {
        ValidationResult error1 = new ValidationResult(123L, "Test 1", DUPLICATE_REFERENCE_DETECTED);
        ValidationResult error2 = new ValidationResult(123L, "Test 2", DUPLICATE_REFERENCE_DETECTED);
        ValidationResult error3 = new ValidationResult(123L, "Test 4", INVALID_END_BALANCE);
        return List.of(error1, error2, error3);
    }

}
