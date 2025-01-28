package nl.rabobank.customer.statementprocessor.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * Not yet implemented on the business logic to save error reports
 */
@Entity
public class ValidationError {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long reference;
    private String description;
    private String errorMessage;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false)
    private StatementReport statementReport;


    protected ValidationError() {}

    // Constructor
    public ValidationError(Long reference, String description, String errorMessage, StatementReport statementReport) {
        this.reference = reference;
        this.description = description;
        this.errorMessage = errorMessage;
        this.statementReport = statementReport;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReference() {
        return reference;
    }

    public void setReference(Long reference) {
        this.reference = reference;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public StatementReport getReport(StatementReport statementReport) {
        return this.statementReport;
    }

    public void setReport(StatementReport statementReport) {
        this.statementReport = statementReport;
    }
}