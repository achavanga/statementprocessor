package nl.rabobank.customer.statementprocessor.mapper;

import nl.rabobank.customer.statementprocessor.dto.CustomerStatement;
import nl.rabobank.customer.statementprocessor.model.Statement;

import java.util.List;

public class CustomerStatementToStatementMapper {

    public List<Statement> toStatementList(List<CustomerStatement> customerStatements) {
        return customerStatements.stream()
                .map(this::mapToStatement)
                .toList();
    }

    private Statement mapToStatement(CustomerStatement customerStatement) {
        return new Statement(
                customerStatement.reference(),
                customerStatement.accountNumber(),
                customerStatement.startBalance(),
                customerStatement.mutation(),
                customerStatement.description(),
                customerStatement.endBalance()
        );
    }
}
