package com.example.template.repository;

import com.example.template.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<File, String>, JpaSpecificationExecutor<File> {
    Optional<File> findFirstByUrl(String url);
    long countByDeletedFalse();

    @Query("SELECT f.category, COUNT(f) " +
            "FROM File f " +
            "WHERE (f.deleted = false OR f.deleted IS NULL) " +
            "GROUP BY f.category")
    List<Object[]> countFilesByCategory();

    @Query("""
        SELECT f
        FROM File f
        ORDER BY f.deleted ASC, f.createdAt DESC
    """)
    List<File> findAllOrderByDeleted();
}