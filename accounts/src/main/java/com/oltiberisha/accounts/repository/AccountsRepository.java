package com.oltiberisha.accounts.repository;

import com.oltiberisha.accounts.entity.Accounts;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface AccountsRepository extends JpaRepository<Accounts, Long> {
    Optional<Accounts> findByCustomerId(Long customerId);

    //@Modifying - update @Transactional - commit
    @Transactional
    @Modifying
    void deleteByCustomerId(Long customerId);
}
