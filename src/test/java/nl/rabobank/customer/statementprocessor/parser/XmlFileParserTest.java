package nl.rabobank.customer.statementprocessor.parser;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import nl.rabobank.customer.statementprocessor.dto.CustomerStatement;
import nl.rabobank.customer.statementprocessor.exception.FileParsingException;
import nl.rabobank.customer.statementprocessor.mapper.XmlToStatementMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class XmlFileParserTest {

    @InjectMocks
    private XmlFileParser xmlFileParser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        XmlToStatementMapper xmlToStatementMapper1 = new XmlToStatementMapper();
        XmlMapper xmlMapper = new XmlMapper();
        xmlFileParser = new XmlFileParser(xmlToStatementMapper1, xmlMapper);
    }

    @Test
    void shouldParseFileSuccessfully() {
        String csvContent = """
                <records>
                   <record reference="130498">
                     <accountNumber>NL69ABNA0433647324</accountNumber>
                     <description>Tickets for Peter Theuß</description>
                     <startBalance>26.9</startBalance>
                     <mutation>-18.78</mutation>
                     <endBalance>8.12</endBalance>
                   </record>
                 </records>
            """;
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.xml",
                "text/xml", csvContent.getBytes());


        List<CustomerStatement> customerStatements = xmlFileParser.parseFile(mockFile);
        assertThat(customerStatements)
                .isNotEmpty()
                .hasSize(1);
    }

    @Test
    void shouldThrowInvalidFileExceptionWhenFieIsEmpty() {
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.xml",
                "text/xml","".getBytes());
        assertThatThrownBy(() ->  xmlFileParser.parseFile(mockFile))
                .isInstanceOf(FileParsingException.class)
                .hasMessageContaining("XML processing failed:");
    }
    @Test
    void shouldThrowInvalidFileExceptionWhenFieIsInvalid() {
        String csvContent = """
                 <records>
                   <record reference="130498">
                     <accountNumber>NL69ABNA0433647324</accountNumber>
                     <description>Tickets for Peter Theu</description>
                     <startBalance>26.9</startBalance>
            """;
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.xml", "text/xml", csvContent.getBytes());

        assertThatThrownBy(() ->  xmlFileParser.parseFile(mockFile))
                .isInstanceOf(FileParsingException.class)
                .hasMessageContaining("XML processing failed:");
    }
}
