package com.trackwise.classification.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTagRequest {

    @NotBlank(message = "Tag name is required")
    @Size(max = 50, message = "Name cannot exceed 50 characters")
    private String name;

    @Pattern(regexp = "^#([A-Fa-f0-9]{6})$", message = "Color must be a valid 6-character hex string (e.g. #FF5733)")
    private String color;
}
