package nl.rabobank.customer.statementprocessor.events;

import nl.rabobank.customer.statementprocessor.model.Statement;
import nl.rabobank.customer.statementprocessor.repository.StatementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StatementsEventListenerTest {

    private StatementRepository statementRepository;
    private StatementsEventListener statementsEventListener;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        statementRepository = mock(StatementRepository.class);
        statementsEventListener = new StatementsEventListener(statementRepository);
    }

    @Test
    void shouldHandleStatementEventAndSaveToDatabase() {
        List<Statement> statements = List.of(new Statement());
        StatementEvent statementEvent = new StatementEvent(this, statements);
        statementsEventListener.handleBatchStartedEvent(statementEvent);
        verify(statementRepository, times(1)).saveAll(statements);
    }

    @Test
    void shouldNotSaveAnythingWhenEventHasNoStatements() {
        List<Statement> statements = Collections.emptyList();
        StatementEvent statementEvent = new StatementEvent(this, statements);
        statementsEventListener.handleBatchStartedEvent(statementEvent);
        verify(statementRepository, never()).saveAll(any());
    }

    @Test
    void shouldDoNothingWhenStatementEventIsNull() {
        statementsEventListener.handleBatchStartedEvent(null);
        verify(statementRepository, never()).saveAll(any());
    }

    @Test
    void shouldThrowRuntimeExceptionWhenRepositoryFails() {
        List<Statement> statements = List.of(new Statement());
        StatementEvent statementEvent = new StatementEvent(this, statements);
        doThrow(new RuntimeException("Database error")).when(statementRepository).saveAll(statements);
        assertThatThrownBy(() -> statementsEventListener.handleBatchStartedEvent(statementEvent))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Database error");

        verify(statementRepository, times(1)).saveAll(statements);
    }
}