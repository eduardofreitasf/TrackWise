package com.trackwise.classification.controller;

import com.trackwise.classification.dto.CategoryResponse;
import com.trackwise.classification.dto.CreateCategoryRequest;
import com.trackwise.classification.dto.UpdateCategoryRequest;
import com.trackwise.classification.model.CategoryEntityType;
import com.trackwise.classification.service.ClassificationService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final ClassificationService classificationService;

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getCategories(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) CategoryEntityType entityType) {
        List<CategoryResponse> categories =
                classificationService.getCategories(userDetails.getUsername(), entityType);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(
            @AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        CategoryResponse category =
                classificationService.getCategoryById(userDetails.getUsername(), id);
        return ResponseEntity.ok(category);
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CreateCategoryRequest request) {
        CategoryResponse category =
                classificationService.createCategory(userDetails.getUsername(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @Valid @RequestBody UpdateCategoryRequest request) {
        CategoryResponse category =
                classificationService.updateCategory(userDetails.getUsername(), id, request);
        return ResponseEntity.ok(category);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        classificationService.deleteCategory(userDetails.getUsername(), id);
        return ResponseEntity.noContent().build();
    }
}
