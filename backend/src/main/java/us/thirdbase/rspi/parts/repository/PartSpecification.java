package us.thirdbase.rspi.parts.repository;

import org.springframework.data.jpa.domain.Specification;
import us.thirdbase.rspi.parts.model.Part;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class PartSpecification<T extends Part> implements Specification<Part> {
    private static final long serialVersionUID = 4476731907802012676L;
    private final SearchCriteria criteria;

    PartSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Part> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder) {
        switch (criteria.getOperation()) {
            case GREATER_THAN:
                return builder.greaterThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
            case LESS_THAN:
                return builder.lessThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
            case EQUALITY:
                if (root.get(criteria.getKey()).getJavaType() == String.class) {
                    return builder.like(builder.upper(root.get(criteria.getKey())), "%"
                        + criteria.getValue().toString().toUpperCase() + "%");
                } else {
                    return builder.equal(builder.upper(root.get(criteria.getKey())),
                        criteria.getValue().toString().toUpperCase());
                }
            case LIKE:
                return builder.like(builder.upper(root.get(criteria.getKey())), criteria.getValue().toString().toUpperCase());
            case NEGATION:
                return builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
            case STARTS_WITH:
                return builder.like(builder.upper(root.get(criteria.getKey())),
                    criteria.getValue().toString().toUpperCase() + "%");
            case ENDS_WITH:
                return builder.like(builder.upper(root.get(criteria.getKey())),
                    "%" + criteria.getValue().toString().toUpperCase());
            case CONTAINS:
                return builder.like(builder.upper(root.get(criteria.getKey())),
                    "%" + criteria.getValue().toString().toUpperCase() + "%");
            default:
                throw new IllegalStateException("Unexpected value: " + criteria.getOperation());
        }
    }
}
