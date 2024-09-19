package order.searchcriteria;


import jakarta.persistence.criteria.Predicate;
import order.OrderEntity;
import order.OrderSearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class OrderSpecifications {

    public static Specification<OrderEntity> searchByCriteria(Long userId, OrderSearchCriteria criteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.get("owner").get("id"), userId));

            if (criteria.getTicker() != null && !criteria.getTicker().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("ticker"), criteria.getTicker()));
            }

            if (criteria.getType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("type"), criteria.getType()));
            }

            if (criteria.getSumMin() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("sum"), criteria.getSumMin()));
            }

            if (criteria.getSumMax() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("sum"), criteria.getSumMax()));
            }

            if (criteria.getCreationTime() != null) {
                predicates.add(criteriaBuilder.equal(root.get("creationTime"), criteria.getCreationTime()));
            }

            if (criteria.getClosedTime() != null) {
                predicates.add(criteriaBuilder.equal(root.get("closedTime"), criteria.getClosedTime()));
            }

            if (criteria.getResultMax() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("result"), criteria.getResultMax()));
            }
            if (criteria.getResultMin() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("result"), criteria.getResultMin()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

