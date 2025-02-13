package nl.rabobank.customer.statementprocessor.control.parser;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import nl.rabobank.customer.statementprocessor.boundary.dto.CustomerStatement;
import nl.rabobank.customer.statementprocessor.boundary.dto.StatementCsv;
import nl.rabobank.customer.statementprocessor.control.exception.FileParsingException;
import nl.rabobank.customer.statementprocessor.control.exception.InvalidFileException;
import nl.rabobank.customer.statementprocessor.control.mapper.CsvToStatementMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public final class CsvFileParser implements FileParser {

    private static final Logger log = LoggerFactory.getLogger(CsvFileParser.class);

    private final CsvToStatementMapper csvToStatementMapper;
    private final CsvMapper csvMapper;

    /**
     * Constructs a CsvFileParser instance.
     *
     * @param csvToStatementMapper The mapper used to convert CSV data to CustomerStatement objects.
     * @param csvMapper The Jackson CsvMapper instance used for reading and parsing CSV files.
     */
    public CsvFileParser(CsvToStatementMapper csvToStatementMapper, CsvMapper csvMapper) {
        this.csvToStatementMapper = csvToStatementMapper;
        this.csvMapper = csvMapper;
    }

    /**
     * Parses a CSV file and converts it into a list of CustomerStatement objects.
     *
     * This method reads the provided CSV file, maps it to a list of StatementCsv objects using Jackson's CsvMapper,
     * and then converts these objects into CustomerStatement objects using the CsvToStatementMapper.
     *
     * @param file The CSV file to be parsed.
     * @return A list of CustomerStatement objects parsed from the CSV file.
     * @throws InvalidFileException if the file cannot be parsed or the format is invalid.
     */
    @Override
    public List<CustomerStatement> parseFile(MultipartFile file) {
        log.info("Start parsing the CSV file: {}", file.getOriginalFilename());

        try {
            var schema = createCsvSchema();
            var statementCsvList = parseCsvFile(file, schema);
            log.info("Successfully parsed CSV file: {}", file.getOriginalFilename());

            return csvToStatementMapper.fromCsvToStatementList(statementCsvList);
        } catch (Exception e) {
            log.error("Failed to parse CSV file: {}", file.getOriginalFilename(), e);
            throw new FileParsingException("CSV parsing failed: " + e.getMessage());
        }
    }

    /**
     * Creates the CSV schema for parsing StatementCsv objects.
     *
     * @return The CsvSchema instance configured for parsing StatementCsv records.
     */
    private CsvSchema createCsvSchema() {
        return csvMapper
                .schemaFor(StatementCsv.class)
                .withHeader()
                .withColumnSeparator(CsvSchema.DEFAULT_COLUMN_SEPARATOR);
    }

    /**
     * Parses the given CSV file using the provided schema.
     *
     * @param file   The CSV file to be parsed.
     * @param schema The schema to be used for parsing the file.
     * @return A list of StatementCsv objects parsed from the file.
     * @throws IOException If an error occurs while reading or parsing the file.
     */
    private List<StatementCsv> parseCsvFile(MultipartFile file, CsvSchema schema) throws IOException {
        try (MappingIterator<StatementCsv> mappingIterator = csvMapper
                .readerWithSchemaFor(StatementCsv.class)
                .with(schema)
                .readValues(new String(file.getBytes(), StandardCharsets.UTF_8))) {
            return mappingIterator.readAll();
        }
    }
}
