package nl.rabobank.customer.statementprocessor.validation;

import nl.rabobank.customer.statementprocessor.boundary.dto.CustomerStatement;
import nl.rabobank.customer.statementprocessor.boundary.dto.ValidationResult;
import nl.rabobank.customer.statementprocessor.util.StatementValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static nl.rabobank.customer.statementprocessor.util.StatementConstants.DUPLICATE_REFERENCE_DETECTED;
import static nl.rabobank.customer.statementprocessor.util.StatementConstants.INVALID_END_BALANCE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class StatementValidatorTest {

    @InjectMocks
    private StatementValidator statementValidator;

    private CustomerStatement validCustomerStatement;
    private CustomerStatement invalidEndBalanceCustomerStatement;
    private CustomerStatement duplicateCustomerStatement;

    @BeforeEach
    void setUp() {
        // Set up mock statements
        validCustomerStatement = new CustomerStatement(1L, "NL93ABNA0585619023", new BigDecimal("100.00"),
                new BigDecimal("50.00"), "Valid statement", new BigDecimal("150.00"));

        invalidEndBalanceCustomerStatement = new CustomerStatement(2L, "NL69ABNA0433647324", new BigDecimal("200.00"),
                new BigDecimal("-50.00"), "Invalid end balance statement", new BigDecimal("100.00"));

        duplicateCustomerStatement = new CustomerStatement(1L, "NL93ABNA0585619023", new BigDecimal("100.00"),
                new BigDecimal("50.00"), "Duplicate statement", new BigDecimal("150.00"));
    }

    @Test
    void testValidateParsedStatements_noErrors() {
        var statements = List.of(validCustomerStatement);
        var results = statementValidator.validateParsedStatements(statements);

        // Then there should be no validation errors
        assertTrue(results.isEmpty());
    }

    @Test
    void testValidateParsedStatements_duplicateReference() {
        var statements = List.of(validCustomerStatement, duplicateCustomerStatement);
        var results = statementValidator.validateParsedStatements(statements);

        // Then there should be a validation error for the duplicate reference
        assertEquals(2, results.size());
        ValidationResult result = results.getFirst();
        assertEquals(DUPLICATE_REFERENCE_DETECTED, result.errorMessage());
    }

    @Test
    void testValidateParsedStatements_invalidEndBalance() {
        var statements = List.of(invalidEndBalanceCustomerStatement);
        var results = statementValidator.validateParsedStatements(statements);

        // Then there should be a validation error for the invalid end balance
        assertEquals(1, results.size());
        ValidationResult result = results.getFirst();
        assertEquals(INVALID_END_BALANCE, result.errorMessage());
    }

    @Test
    void testValidateParsedStatements_duplicateReferenceAndInvalidEndBalance() {
        var statements = List.of(validCustomerStatement, duplicateCustomerStatement, invalidEndBalanceCustomerStatement);
        var results = statementValidator.validateParsedStatements(statements);

        // Then there should be 3 validation errors: 2 for duplicates and 1 for invalid end balance
        assertEquals(3, results.size());

       // Check if there are exactly two duplicate errors and one balance error
        long duplicateCount = results.stream()
                .filter(result -> DUPLICATE_REFERENCE_DETECTED.equals(result.errorMessage()))
                .count();
        assertEquals(2, duplicateCount, "There should be exactly 2 duplicate errors");

        long balanceCount = results.stream()
                .filter(result -> INVALID_END_BALANCE.equals(result.errorMessage()))
                .count();
        assertEquals(1, balanceCount, "There should be exactly 1 invalid balance error");

    }

    @Test
    void testValidateParsedStatements_noErrorsWhenAllValid() {
        var statements = List.of(validCustomerStatement, new CustomerStatement(2L, "NL987654321",
                new BigDecimal("200.00"), new BigDecimal("50.00"), "Another valid statement",
                new BigDecimal("250.00")));
        var results = statementValidator.validateParsedStatements(statements);

        // Then there should be no validation errors
        assertTrue(results.isEmpty());
    }
}
