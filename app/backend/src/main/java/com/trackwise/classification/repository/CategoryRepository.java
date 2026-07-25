package com.trackwise.classification.repository;

import com.trackwise.classification.model.Category;
import com.trackwise.classification.model.CategoryEntityType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAllByUserIdAndDeletedAtIsNull(Long userId);

    List<Category> findAllByUserIdAndEntityTypeAndDeletedAtIsNull(Long userId, CategoryEntityType entityType);

    Optional<Category> findByIdAndUserIdAndDeletedAtIsNull(Long id, Long userId);
}
