package net.vorlon.iqfamily.model.spec;
import net.vorlon.iqfamily.model.domain.User;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class UserSpecification implements Specification<User> {

    private SpecSearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        if (criteria.getOperation().name().equalsIgnoreCase(SearchOperation.getSimpleOperation('>').name())) {
            return builder.greaterThanOrEqualTo(
                    root.<String> get(criteria.getKey()), criteria.getValue().toString());
        }
        else if (criteria.getOperation().name().equalsIgnoreCase(SearchOperation.getSimpleOperation('<').name())) {
            return builder.lessThanOrEqualTo(
                    root.<String> get(criteria.getKey()), criteria.getValue().toString());
        }
        else if (criteria.getOperation().name().equalsIgnoreCase(SearchOperation.getSimpleOperation(':').name())) {
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                return builder.like(
                        root.<String>get(criteria.getKey()), "%" + criteria.getValue() + "%");
            } else {
                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
            }
        }
        return null;
    }

}
