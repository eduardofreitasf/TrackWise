package com.trackwise.asset.repository;

import com.trackwise.asset.model.Asset;
import com.trackwise.asset.model.AssetStatus;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {

    Page<Asset> findAllByUserIdAndDeletedAtIsNull(Long userId, Pageable pageable);

    Page<Asset> findAllByUserIdAndStatusAndDeletedAtIsNull(Long userId, AssetStatus status, Pageable pageable);

    Optional<Asset> findByIdAndUserIdAndDeletedAtIsNull(Long id, Long userId);
}
