package nl.rabobank.customer.statementprocessor.mapper;

import nl.rabobank.customer.statementprocessor.dto.CustomerStatement;
import nl.rabobank.customer.statementprocessor.dto.StatementXml;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Component responsible for mapping a list of {@link StatementXml} objects to a list of {@link CustomerStatement}.
 */
@Component
public class XmlToStatementMapper {

    /**
     * Converts a list of {@link StatementXml} objects to a list of {@link CustomerStatement}.
     *
     * @param statementCsvs The list of {@link StatementXml} objects to be converted.
     * @return A list of {@link CustomerStatement} objects mapped from the input {@link StatementXml} list.
     */
    public List<CustomerStatement> fromXmlToStatementList(List<StatementXml> statementCsvs) {
        return statementCsvs.stream()
                .map(this::mapToStatement)
                .toList();
    }

    /**
     * Maps a single {@link StatementXml} object to a {@link CustomerStatement}.
     *
     * @param statementXml The {@link StatementXml} to be mapped to a {@link CustomerStatement}.
     * @return A {@link CustomerStatement} object mapped from the given {@link StatementXml}.
     */
    private CustomerStatement mapToStatement(StatementXml statementXml) {
        return new CustomerStatement(
                statementXml.reference(),
                statementXml.accountNumber(),
                statementXml.startBalance(),
                statementXml.mutation(),
                statementXml.description(),
                statementXml.endBalance()
        );
    }
}