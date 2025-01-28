package nl.rabobank.customer.statementprocessor.events;

import nl.rabobank.customer.statementprocessor.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * The {@code StatementsEventPublisher} class is responsible for publishing events related to statements.
 * It checks if statements exist and publishes a {@link StatementEvent} if there are statements to publish.
 * This class uses Spring's {@link ApplicationEventPublisher} to publish events.
 */
@Component
public class StatementsEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(StatementsEventPublisher.class);
    private final ApplicationEventPublisher publisher;

    public StatementsEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    /**
     * Publishes a {@link StatementEvent} with a list of statements if there are statements to publish.
     *
     * @param statements the list of statements to publish
     */
    public void publish(List<Statement> statements) {
        if (isEmpty(statements)) return;

        log.info("Start publishing statements event...");
        StatementEvent statementEvent = new StatementEvent(this, statements);
        publisher.publishEvent(statementEvent);
        log.info("Done publishing statements event...");
    }

    /**
     * Checks if the provided list of statements is either {@code null} or empty.
     * Logs a message if no statements are available to publish.
     *
     * @param statements the list of statements to check
     * @return {@code true} if the list is empty or {@code null}, {@code false} otherwise
     */
    private static boolean isEmpty(List<Statement> statements) {
        boolean isEmpty = Optional.ofNullable(statements)
                .map(List::isEmpty)
                .orElse(true);

        if (isEmpty) {
            log.info("No statements to publish.");
        }
        return isEmpty;
    }
}
