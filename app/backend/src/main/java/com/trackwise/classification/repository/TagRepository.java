package com.trackwise.classification.repository;

import com.trackwise.classification.model.Tag;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    List<Tag> findAllByUserIdAndDeletedAtIsNull(Long userId);

    Optional<Tag> findByIdAndUserIdAndDeletedAtIsNull(Long id, Long userId);

    Set<Tag> findAllByIdInAndUserIdAndDeletedAtIsNull(Set<Long> ids, Long userId);
}
