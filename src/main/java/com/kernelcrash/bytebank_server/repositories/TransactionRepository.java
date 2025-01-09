package com.kernelcrash.bytebank_server.repositories;

import com.kernelcrash.bytebank_server.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
