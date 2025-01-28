package nl.rabobank.customer.statementprocessor.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

/**
 * Not yet implemented on the business logic to save error reports
 */
@Entity
public class StatementReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long reportId;

    @OneToMany(mappedBy = "statementReport", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<ValidationError> failedRecords = new ArrayList<>();

    public StatementReport() {}

    public StatementReport(Long reportId, List<ValidationError> failedRecords) {
        this.reportId = reportId;
        this.failedRecords = failedRecords;
    }

    public void addValidationError(ValidationError validationError) {
        failedRecords.add(validationError);
        validationError.getReport(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public List<ValidationError> getFailedRecords() {
        return failedRecords;
    }

    public void setFailedRecords(List<ValidationError> failedRecords) {
        this.failedRecords = failedRecords;
    }
}