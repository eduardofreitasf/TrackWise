package com.trackwise.asset.dto;

import com.trackwise.asset.model.AssetStatus;
import com.trackwise.classification.dto.CategoryResponse;
import com.trackwise.classification.dto.TagResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetResponse {

    private Long id;
    private String name;
    private String description;
    private CategoryResponse category;
    private Set<TagResponse> tags;
    private LocalDate purchaseDate;
    private MoneyDto purchasePrice;
    private MoneyDto currentValue;
    private AssetStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
