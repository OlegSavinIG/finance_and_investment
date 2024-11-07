package oleg.savin.statistics.service.specification;

import jakarta.persistence.criteria.Predicate;
import oleg.savin.statistics.entity.StatisticEntity;
import oleg.savin.statistics.entity.StatisticSearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class StatisticSpecification {

    public static Specification<StatisticEntity> createSpecification(StatisticSearchCriteria criteria) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.getUserId() != null) {
                predicates.add(builder.equal(root.get("userId"), criteria.getUserId()));
            }
            if (criteria.getTicker() != null) {
                predicates.add(builder.equal(root.get("ticker"), criteria.getTicker()));
            }
            if (criteria.getStartTime() != null && criteria.getEndTime() != null) {
                predicates.add(builder.between(root.get("creationTime"), criteria.getStartTime(), criteria.getEndTime()));
            }
            if (criteria.getResult() != null) {
                predicates.add(builder.equal(root.get("result"), criteria.getResult()));
            }
            if (criteria.getType() != null) {
                predicates.add(builder.equal(root.get("type"), criteria.getType()));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
