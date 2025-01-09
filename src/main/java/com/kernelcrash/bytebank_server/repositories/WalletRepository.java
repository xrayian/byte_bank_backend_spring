package com.kernelcrash.bytebank_server.repositories;

import com.kernelcrash.bytebank_server.models.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository  extends JpaRepository<Wallet, Long> {
}
