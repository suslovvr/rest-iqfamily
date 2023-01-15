package net.vorlon.iqfamily.dao;

import net.vorlon.iqfamily.model.domain.Account;
import net.vorlon.iqfamily.model.domain.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findAll();
    long countByUserId(Long userId);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Account> findByUserId(Long userId);
}
