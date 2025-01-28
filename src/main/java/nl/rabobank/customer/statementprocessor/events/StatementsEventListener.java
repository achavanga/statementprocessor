package nl.rabobank.customer.statementprocessor.events;

import nl.rabobank.customer.statementprocessor.repository.StatementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * The {@code StatementsEventListener} class listens for {@link StatementEvent} and processes
 * the event by saving the statements to the database using the {@link StatementRepository}.
 * It ensures that only valid events with statements are processed.
 */
@Component
public class StatementsEventListener {
    private static final Logger log = LoggerFactory.getLogger(StatementsEventListener.class);
    private final StatementRepository statementRepository;

    public StatementsEventListener(StatementRepository statementRepository) {
        this.statementRepository = statementRepository;
    }

    /**
     * Handles the {@link StatementEvent} when it is published.
     * Saves the statements from the event to the database if they exist.
     *
     * @param statementEvent the event that contains the statements to be saved
     */
    @EventListener
    public void handleBatchStartedEvent(StatementEvent statementEvent) {
        if (isEmpty(statementEvent)) return;

        log.info("Handling StatementEvent and saving it to DB...");
        statementRepository.saveAll(statementEvent.getStatements());
        log.info("Done saving StatementEvent to DB.");
    }

    /**
     * Checks if the given {@link StatementEvent} has any statements to process.
     * Logs a message if there are no statements in the event.
     *
     * @param statementEvent the event to check
     * @return {@code true} if the event has no statements, {@code false} otherwise
     */
    private static boolean isEmpty(StatementEvent statementEvent) {
        boolean isEmpty = Optional.ofNullable(statementEvent)
                .map(StatementEvent::getStatements)
                .map(List::isEmpty)
                .orElse(true);

        if (isEmpty) {
            log.info("No statements to process.");
        }
        return isEmpty;
    }
}