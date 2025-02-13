package nl.rabobank.customer.statementprocessor.util;

import nl.rabobank.customer.statementprocessor.boundary.dto.CustomerStatement;
import nl.rabobank.customer.statementprocessor.boundary.dto.ValidationResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static nl.rabobank.customer.statementprocessor.util.StatementConstants.DUPLICATE_REFERENCE_DETECTED;
import static nl.rabobank.customer.statementprocessor.util.StatementConstants.INVALID_END_BALANCE;

/**
 * Component responsible for validating a list of {@link CustomerStatement} objects.
 * The validation checks for duplicate references and ensures the end balance is correct.
 */
@Component
public class StatementValidator {

    /**
     * Validates a list of customer statements for duplicate references and end balance correctness.
     *
     * @param customerStatements The list of {@link CustomerStatement} to be validated.
     * @return A list of {@link ValidationResult} objects representing the validation errors for each statement.
     */
    public List<ValidationResult> validateParsedStatements(List<CustomerStatement> customerStatements) {
        return customerStatements.stream()
                .collect(Collectors.groupingBy(CustomerStatement::reference))
                .entrySet().stream()
                .flatMap(entry -> entry.getValue().stream()
                        .map(statement ->
                                validateStatement(statement, entry.getValue().size() > 1)))  // Check for duplicates
                .filter(Objects::nonNull) // Filter out null results (if no validation error)
                .toList();
    }

    /**
     * Validates a single customer statement for duplication and end balance validity.
     *
     * @param customerStatement The {@link CustomerStatement} to be validated.
     * @param isDuplicate Flag indicating if the statement is a duplicate.
     * @return A {@link ValidationResult} with the error message if validation fails, otherwise null.
     */
    private ValidationResult validateStatement(CustomerStatement customerStatement, boolean isDuplicate) {
        boolean isEndBalanceValid = validateEndBalance(customerStatement);

        // If duplicate reference or invalid end balance, return a validation error
        if (isDuplicate || !isEndBalanceValid) {
            return new ValidationResult(
                    customerStatement.reference(),
                    customerStatement.description(),
                    createErrorMessage(isDuplicate, isEndBalanceValid)
            );
        }
        return null; // No validation error
    }

    /**
     * Creates an error message based on whether the statement is a duplicate and if the end balance is valid.
     *
     * @param isDuplicate Flag indicating if the statement has a duplicate reference.
     * @param isEndBalanceValid Flag indicating if the end balance is valid.
     * @return The error message as a string.
     */
    private String createErrorMessage(boolean isDuplicate, boolean isEndBalanceValid) {
        StringBuilder errorMessage = new StringBuilder();

        // Append a relevant error message for duplicate or invalid balance
        if (isDuplicate) {
            errorMessage.append(DUPLICATE_REFERENCE_DETECTED);
        }
        if (!isEndBalanceValid) {
            errorMessage.append(INVALID_END_BALANCE);
        }
        return errorMessage.toString().trim();
    }

    /**
     * Validates the end balance of the given customer statement by checking if
     * the sum of the start balance and mutation equals the end balance.
     *
     * @param customerStatement The {@link CustomerStatement} whose end balance is to be validated.
     * @return True if the end balance is valid, otherwise false.
     */
    private boolean validateEndBalance(CustomerStatement customerStatement) {
        return customerStatement.startBalance()
                .add(customerStatement.mutation())
                .compareTo(customerStatement.endBalance()) == 0;
    }
}
