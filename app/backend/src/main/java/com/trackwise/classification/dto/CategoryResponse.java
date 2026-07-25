package com.trackwise.classification.dto;

import com.trackwise.classification.model.CategoryEntityType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {

    private Long id;
    private String name;
    private Long parentId;
    private CategoryEntityType entityType;
}
