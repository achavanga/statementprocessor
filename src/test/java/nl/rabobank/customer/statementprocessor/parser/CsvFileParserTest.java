package nl.rabobank.customer.statementprocessor.parser;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import nl.rabobank.customer.statementprocessor.dto.CustomerStatement;
import nl.rabobank.customer.statementprocessor.exception.FileParsingException;
import nl.rabobank.customer.statementprocessor.mapper.CsvToStatementMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@ExtendWith(MockitoExtension.class)
class CsvFileParserTest {

    private CsvFileParser csvFileParser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        CsvToStatementMapper csvToStatementMapper = new CsvToStatementMapper();
        CsvMapper csvMapper = new CsvMapper();
        csvFileParser = new CsvFileParser(csvToStatementMapper, csvMapper);
    }

    @Test
    void shouldParseFileSuccessfully(){
        // Given
        String csvContent = """
                Reference,AccountNumber,Description,Start Balance,Mutation,End Balance
                194261,NL91RABO0315273637,Clothes from Jan Bakker,21.6,-41.83,-20.23
                112806,NL27SNSB0917829871,Clothes for Willem Dekker,91.23,+15.57,106.8
                183049,NL69ABNA0433647324,Clothes for Jan King,86.66,+44.5,131.16
            """;
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.csv",
                "text/csv", csvContent.getBytes());

        List<CustomerStatement> customerStatements = csvFileParser.parseFile(mockFile);
        assertThat(customerStatements)
                .isNotEmpty()
                .hasSize(3);

    }

    @Test
    void shouldParseAnEmptyFile(){
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.csv",
                "text/csv", "".getBytes());
        List<CustomerStatement> customerStatements = csvFileParser.parseFile(mockFile);
        assertThat(customerStatements).isEmpty();
    }

    @Test
    void shouldThrowInvalidFileExceptionWhenTryingToParseInvalidCsvFormat() {
        String csvContent = """
                Reference,AccountNumber,Description,Start Balance,Mutation,End Balance
                wwww,NL91RABO0315273637,Clothes from Jan Bakker,21.6,www.83,ttt
            """;
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.csv",
                "text/csv", csvContent.getBytes());

        assertThatThrownBy(() ->  csvFileParser.parseFile(mockFile))
                .isInstanceOf(FileParsingException.class)
                .hasMessageContaining("CSV parsing failed:");
    }
}
