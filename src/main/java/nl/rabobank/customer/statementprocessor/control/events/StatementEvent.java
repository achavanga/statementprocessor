package nl.rabobank.customer.statementprocessor.control.events;

import nl.rabobank.customer.statementprocessor.entity.model.Statement;
import org.springframework.context.ApplicationEvent;

import java.util.List;
import java.util.Objects;

public class StatementEvent extends ApplicationEvent {
    private final List<Statement> statements;

    public StatementEvent(Object source, List<Statement> statements) {
        super(source);
        this.statements = Objects.requireNonNull(statements, "statements cannot be null");
    }

    public List<Statement> getStatements() {
        return statements;
    }
}