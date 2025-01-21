package com.kernelcrash.bytebank_server.repositories;

import com.kernelcrash.bytebank_server.models.User;
import com.kernelcrash.bytebank_server.models.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    User findByUsername(String username);

    User findByEmail(String email);
}
