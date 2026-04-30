package com.ogocx.userservice.specifications;

import com.ogocx.userservice.models.UserModel;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class UserSpecification {

    private UserSpecification() {}

    public static Specification<UserModel> filter(
            String firstName,
            String lastName,
            String email,
            String firstDocument,
            String secondDocument,
            Boolean status,
            LocalDate birthDate
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (firstName != null && !firstName.isBlank())
                predicates.add(cb.like(cb.lower(root.get("firstName")), "%" + firstName.toLowerCase() + "%"));

            if (lastName != null && !lastName.isBlank())
                predicates.add(cb.like(cb.lower(root.get("lastName")), "%" + lastName.toLowerCase() + "%"));

            if (email != null && !email.isBlank())
                predicates.add(cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%"));

            if (firstDocument != null && !firstDocument.isBlank())
                predicates.add(cb.equal(root.get("firstDocument"), firstDocument));

            if (secondDocument != null && !secondDocument.isBlank())
                predicates.add(cb.equal(root.get("secondDocument"), secondDocument));

            if (status != null)
                predicates.add(cb.equal(root.get("status"), status));

            if (birthDate != null)
                predicates.add(cb.equal(root.get("birthDate"), birthDate));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
