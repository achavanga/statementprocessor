package nl.rabobank.customer.statementprocessor.control.parser;

import nl.rabobank.customer.statementprocessor.boundary.dto.CustomerStatement;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * A sealed interface for parsing different types of files into customer statements.
 * It defines a contract for file parsers (CSV, XML) to implement the parsing logic.
 */
public sealed interface FileParser permits CsvFileParser, XmlFileParser {
    List<CustomerStatement> parseFile(MultipartFile file) ;
}
