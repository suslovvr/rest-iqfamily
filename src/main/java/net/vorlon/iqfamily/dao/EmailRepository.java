package net.vorlon.iqfamily.dao;

import net.vorlon.iqfamily.model.domain.Email;
import net.vorlon.iqfamily.model.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface EmailRepository extends JpaRepository<Email, Long> {
    List<Email> findAll();
    @Transactional
    @Modifying
    @Query("delete from Email o  where o.email = :email")
    void deleteByEmail(String email);

}
