package nl.rabobank.customer.statementprocessor.mapper;

import nl.rabobank.customer.statementprocessor.dto.CustomerStatement;
import nl.rabobank.customer.statementprocessor.dto.StatementCsv;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Component responsible for mapping a list of {@link StatementCsv} objects to a list of {@link CustomerStatement}.
 */
@Component
public class CsvToStatementMapper {

    /**
     * Converts a list of {@link StatementCsv} objects to a list of {@link CustomerStatement}.
     *
     * @param csvList The list of {@link StatementCsv} objects to be converted.
     * @return A list of {@link CustomerStatement} objects mapped from the input {@link StatementCsv} list.
     */
    public List<CustomerStatement> fromCsvToStatementList(List<StatementCsv> csvList) {
        return csvList.stream()
                .map(this::mapToStatement)
                .toList();
    }

    /**
     * Maps a single {@link StatementCsv} object to a {@link CustomerStatement}.
     *
     * @param csvRecord The {@link StatementCsv} to be mapped to a {@link CustomerStatement}.
     * @return A {@link CustomerStatement} object mapped from the given {@link StatementCsv}.
     */
    private CustomerStatement mapToStatement(StatementCsv csvRecord) {
        return new CustomerStatement(
                csvRecord.reference(),
                csvRecord.accountNumber(),
                csvRecord.startBalance(),
                csvRecord.mutation(),
                csvRecord.description(),
                csvRecord.endBalance()
        );
    }
}