package nl.rabobank.customer.statementprocessor.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

/**
 * Represents a xml wrapper for a list of {@link StatementXml} records.
 * This class is annotated with Jackson XML annotations to map XML elements to Java fields.
 */
@JacksonXmlRootElement(localName = "records")
public record StatementXmlList(
        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "record")
        List<StatementXml> records
) {}