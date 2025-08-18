package com.example.template.specification;

import com.example.template.dto.request.FileSearchRequest;
import com.example.template.entity.File;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class FileSpecification {
    public static Specification<File> search(FileSearchRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
                String keyword = "%" + request.getKeyword().toLowerCase() + "%";
                Predicate nameLike = cb.like(cb.lower(root.get("name")), keyword);
                Predicate descriptionLike = cb.like(cb.lower(root.get("description")), keyword);
                predicates.add(cb.or(nameLike, descriptionLike));
            }
            if (request.getCategories() != null && !request.getCategories().isEmpty()) {
                predicates.add(root.get("category").in(request.getCategories()));
            }
            predicates.add(cb.isFalse(root.get("deleted")));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}