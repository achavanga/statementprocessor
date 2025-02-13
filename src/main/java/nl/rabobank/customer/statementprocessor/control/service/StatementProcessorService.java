package nl.rabobank.customer.statementprocessor.control.service;

import nl.rabobank.customer.statementprocessor.boundary.dto.CustomerStatement;
import nl.rabobank.customer.statementprocessor.boundary.dto.Report;
import nl.rabobank.customer.statementprocessor.boundary.dto.ValidationResult;
import nl.rabobank.customer.statementprocessor.control.events.StatementsEventPublisher;
import nl.rabobank.customer.statementprocessor.control.exception.InvalidFileException;
import nl.rabobank.customer.statementprocessor.control.mapper.CustomerStatementToStatementMapper;
import nl.rabobank.customer.statementprocessor.control.mapper.ReportMapper;
import nl.rabobank.customer.statementprocessor.control.parser.CsvFileParser;
import nl.rabobank.customer.statementprocessor.control.parser.FileParser;
import nl.rabobank.customer.statementprocessor.control.parser.XmlFileParser;
import nl.rabobank.customer.statementprocessor.util.StatementValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Service responsible for processing customer statement files, validating, and saving the statements.
 * Supports CSV and XML file formats only.
 */
@Service
public class StatementProcessorService {

    private static final Logger log = LoggerFactory.getLogger(StatementProcessorService.class);
    private final CsvFileParser csvFileParser;
    private final XmlFileParser xmlFileParser;
    private final StatementValidator validator;
    private final ReportMapper reportMapper;
    private final CustomerStatementToStatementMapper customerStatementToStatementMapper;
    private final StatementsEventPublisher statementsEventPublisher;

    /**
     * Constructor for initializing dependencies.
     *
     * @param csvFileParser                     CSV file parser.
     * @param xmlFileParser                     XML file parser.
     * @param validator                         Validator for customer statements.
     * @param reportMapper                      Mapper for converting validation results to report.
     * @param customerStatementToStatementMapper Mapper for converting customer statements to general statements.
     * @param statementsEventPublisher               Publish statement events.
     */
    public StatementProcessorService(
            CsvFileParser csvFileParser,
            XmlFileParser xmlFileParser,
            StatementValidator validator,
            ReportMapper reportMapper,
            CustomerStatementToStatementMapper customerStatementToStatementMapper,
            StatementsEventPublisher statementsEventPublisher) {

        this.csvFileParser = csvFileParser;
        this.xmlFileParser = xmlFileParser;
        this.validator = validator;
        this.reportMapper = reportMapper;
        this.customerStatementToStatementMapper = customerStatementToStatementMapper;
        this.statementsEventPublisher = statementsEventPublisher;
    }

    /**
     * Processes the provided file, parses the content, validates the statements, and saves valid statements.
     *
     * @param file The file containing the customer statements (CSV or XML).
     * @return A report containing validation results.
     * @throws InvalidFileException if the file type is unsupported or if the file name is missing.
     */
    public Report process(MultipartFile file) throws InvalidFileException {
        var filename = getFileName(file);

        log.info("Start processing file {}", filename);

        // Determine the file type and parse accordingly
        FileParser fileParser = getFileParser(filename);

        var customerStatements = fileParser.parseFile(file);
        var validationErrors = validator.validateParsedStatements(customerStatements);

        saveStatements(customerStatements, validationErrors);

        log.info("Done processing file {}", filename);
        return reportMapper.mapValidationResultsToReport(validationErrors);
    }

    /**
     * Retrieves the file name from the MultipartFile, throwing an exception if it's missing.
     *
     * @param file The uploaded file.
     * @return The original file name.
     * @throws InvalidFileException if the file name is missing.
     */
    private String getFileName(MultipartFile file) throws InvalidFileException {
        var filename = file.getOriginalFilename();
        if (filename == null) {
            throw new InvalidFileException("File name is missing from the file");
        }
        return filename;
    }

    /**
     * Returns the appropriate file parser based on the file extension.
     *
     * @param filename The name of the file to process.
     * @return The file parser (CSV or XML).
     * @throws InvalidFileException if the file type is unsupported.
     */
    private FileParser getFileParser(String filename) throws InvalidFileException {
        return switch (filename.toLowerCase()) {
            case String fileName when fileName.endsWith(".csv") -> csvFileParser;
            case String fileName when fileName.endsWith(".xml") -> xmlFileParser;
            default -> throw new InvalidFileException("Unsupported file type");
        };
    }

    /**
     * Publish customer statements to an event if there are no validation errors.
     *
     * @param customerStatements The list of parsed customer statements.
     * @param validationErrors   The list of validation errors.
     */
    private void saveStatements(List<CustomerStatement> customerStatements, List<ValidationResult> validationErrors) {
        if (validationErrors.isEmpty()) {
            var statements = customerStatementToStatementMapper.toStatementList(customerStatements);
            statementsEventPublisher.publish(statements);
        }
    }
}
