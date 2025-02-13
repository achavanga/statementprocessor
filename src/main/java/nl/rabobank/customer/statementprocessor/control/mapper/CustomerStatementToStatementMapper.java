package nl.rabobank.customer.statementprocessor.control.mapper;

import nl.rabobank.customer.statementprocessor.boundary.dto.CustomerStatement;
import nl.rabobank.customer.statementprocessor.entity.model.Statement;

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
