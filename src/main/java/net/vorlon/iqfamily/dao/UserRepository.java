package net.vorlon.iqfamily.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import net.vorlon.iqfamily.model.domain.User;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findAll(Pageable pageable);
    List<User> findAll();
    List<User> findAll(Specification<User> userSpecification);
    @Query(value = "SELECT u.id FROM USERS u ",
            nativeQuery = true)
    List<Long> findAllIds();



    Optional<User> findById(Long id);

    Optional<String> findNameById(Long id);

    Optional<User> findByName(String name);

    @Query("select o from User o join o.emails e where e.email = :email")
    List<User> findByEmail(String email);

    @Query("select o from User o join o.phones e where e.phone = :phone")
    List<User> findByPhone(String phone);
}
