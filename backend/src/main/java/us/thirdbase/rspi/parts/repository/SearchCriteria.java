package us.thirdbase.rspi.parts.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
class SearchCriteria {
    private String key;
    private SearchOperation operation;
    private Object value;

    SearchCriteria(String key, SearchOperation operation, String value) {
        this.key = key;
        this.operation = operation;
        this.value = value;
    }

    boolean isOrPredicate() {
        return false;
    }
}
