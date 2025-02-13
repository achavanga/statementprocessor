package nl.rabobank.customer.statementprocessor.events;

import nl.rabobank.customer.statementprocessor.control.events.StatementEvent;
import nl.rabobank.customer.statementprocessor.control.events.StatementsEventPublisher;
import nl.rabobank.customer.statementprocessor.entity.model.Statement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
 class StatementsEventPublisherTest {

    private ApplicationEventPublisher publisher;
    private StatementsEventPublisher statementsEventPublisher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Manually mock ApplicationEventPublisher
        publisher = Mockito.mock(ApplicationEventPublisher.class);
        statementsEventPublisher = new StatementsEventPublisher(publisher);
    }

    @Test
    void shouldPublishStatementEvent() {
        List<Statement> statements = List.of(new Statement());
        statementsEventPublisher.publish(statements);

        // Capture the argument passed to publishEvent
        ArgumentCaptor<StatementEvent> eventCaptor = ArgumentCaptor.forClass(StatementEvent.class);
        verify(publisher, times(1)).publishEvent(eventCaptor.capture());
    }
    @Test
    void shouldNotPublishEventWhenNoStatements() {
        List<Statement> statements = Collections.emptyList();
        statementsEventPublisher.publish(statements);
        verify(publisher, never()).publishEvent(any(StatementEvent.class));
    }
    @Test
    void shouldNotPublishEventWhenStatementsIsNull() {
        statementsEventPublisher.publish(null);
        verify(publisher, never()).publishEvent(any(StatementEvent.class));
    }
    @Test
    void shouldThrowExceptionIfPublisherFails() {
        doThrow(new RuntimeException("Publisher error")).when(publisher).publishEvent(any());

        statementsEventPublisher = new StatementsEventPublisher(publisher);
        assertThatThrownBy(() -> statementsEventPublisher.publish(Collections.singletonList(new Statement())))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Publisher error");
    }
}