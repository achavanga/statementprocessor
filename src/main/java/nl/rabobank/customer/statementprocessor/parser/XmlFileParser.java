package nl.rabobank.customer.statementprocessor.parser;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import nl.rabobank.customer.statementprocessor.dto.CustomerStatement;
import nl.rabobank.customer.statementprocessor.dto.StatementXmlList;
import nl.rabobank.customer.statementprocessor.exception.FileParsingException;
import nl.rabobank.customer.statementprocessor.exception.InvalidFileException;
import nl.rabobank.customer.statementprocessor.mapper.XmlToStatementMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Service responsible for parsing XML files into customer statements.
 * Implements the {@link FileParser} interface.
 */
@Service
public final class XmlFileParser implements FileParser {

    private static final Logger log = LoggerFactory.getLogger(XmlFileParser.class);

    private final XmlToStatementMapper xmlToStatementMapper;
    private final XmlMapper xmlMapper;

    /**
     * Constructs an instance of {@link XmlFileParser}.
     *
     * @param xmlToStatementMapper The mapper to convert XML data to customer statements.
     * @param xmlMapper The XML mapper for reading XML data from the file.
     */
    public XmlFileParser(XmlToStatementMapper xmlToStatementMapper, XmlMapper xmlMapper) {
        this.xmlToStatementMapper = xmlToStatementMapper;
        this.xmlMapper = xmlMapper;
    }

    /**
     * Parses an XML file and converts its content into a list of {@link CustomerStatement} objects.
     *
     * @param file The XML file to be parsed.
     * @return A list of parsed customer statements.
     * @throws InvalidFileException if there is an issue with parsing the XML file.
     */
    @Override
    public List<CustomerStatement> parseFile(MultipartFile file) throws InvalidFileException {
        log.info("XML file processing started for file: {}", file.getOriginalFilename());
        try {
            // Deserialize XML data into StatementXmlList
            var statementXmlList = xmlMapper.readValue(file.getInputStream(), StatementXmlList.class);

            // Map XML records to customer statements
            return xmlToStatementMapper.fromXmlToStatementList(statementXmlList.records());
        } catch (IOException e) {
            log.error("XML processing failed for file: {} with error: {}", file.getOriginalFilename(), e.getMessage());
            throw new FileParsingException("XML processing failed: " + e.getMessage());
        }
    }
}