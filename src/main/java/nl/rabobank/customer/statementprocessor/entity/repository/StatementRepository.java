package nl.rabobank.customer.statementprocessor.entity.repository;

import nl.rabobank.customer.statementprocessor.entity.model.Statement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatementRepository  extends JpaRepository<Statement, Long> {

    Statement findByReference(Long reference);

    List<Statement> findByAccountNumber(String accountNumber);
}