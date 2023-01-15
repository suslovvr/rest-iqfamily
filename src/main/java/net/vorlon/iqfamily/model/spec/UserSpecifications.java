package net.vorlon.iqfamily.model.spec;
import javax.persistence.criteria.*;

import liquibase.pro.packaged.S;
import net.vorlon.iqfamily.model.domain.Email;
import net.vorlon.iqfamily.model.domain.User;
import org.springframework.data.jpa.domain.Specification;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class UserSpecifications {

    public static Specification<User> userHasName(String name) {
        return new Specification<User> (){
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.like(root.get("name"), name + "%");
            }
        };
    }
    public static Specification<User> userHasBirthday(LocalDate dateOfBirth) {
        return new Specification<User> (){
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.greaterThanOrEqualTo(root.get("birthDate"), dateOfBirth);
            }
        };
    }
    public static Specification<User> hasUserEmail(String email) {
        return (root, query, criteriaBuilder) -> {
            Join<Email, User> userEmail = root.join("emails");
            return criteriaBuilder.equal(userEmail.get("email"), email);
        };
    }
}
