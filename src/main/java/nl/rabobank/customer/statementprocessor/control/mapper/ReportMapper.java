package nl.rabobank.customer.statementprocessor.control.mapper;

import nl.rabobank.customer.statementprocessor.boundary.dto.Report;
import nl.rabobank.customer.statementprocessor.boundary.dto.ValidationResult;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.List;

@Component
public class ReportMapper {
    private final SecureRandom secureRandom = new SecureRandom();

    // Map ValidationResult list into a StatementReport
    public Report mapValidationResultsToReport(List<ValidationResult> failedRecords) {
        long reportId = secureRandom.nextLong();
        return new Report(reportId, failedRecords);
    }
}
