package com.trackwise.classification.service;

import com.trackwise.classification.dto.CategoryResponse;
import com.trackwise.classification.dto.CreateCategoryRequest;
import com.trackwise.classification.dto.CreateTagRequest;
import com.trackwise.classification.dto.TagResponse;
import com.trackwise.classification.model.Category;
import com.trackwise.classification.model.CategoryEntityType;
import com.trackwise.classification.model.Tag;
import com.trackwise.classification.repository.CategoryRepository;
import com.trackwise.classification.repository.TagRepository;
import com.trackwise.common.exception.ResourceNotFoundException;
import com.trackwise.user.model.User;
import com.trackwise.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClassificationService {

    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<CategoryResponse> getCategories(String userEmail, CategoryEntityType entityType) {
        User user = getUser(userEmail);
        List<Category> categories = (entityType != null)
                ? categoryRepository.findAllByUserIdAndEntityTypeAndDeletedAtIsNull(user.getId(), entityType)
                : categoryRepository.findAllByUserIdAndDeletedAtIsNull(user.getId());

        return categories.stream()
                .map(this::mapToCategoryResponse)
                .toList();
    }

    @Transactional
    public CategoryResponse createCategory(String userEmail, CreateCategoryRequest request) {
        User user = getUser(userEmail);

        Category parent = null;
        if (request.getParentId() != null) {
            parent = categoryRepository.findByIdAndUserIdAndDeletedAtIsNull(request.getParentId(), user.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent category not found"));
        }

        Category category = Category.builder()
                .user(user)
                .name(request.getName())
                .parent(parent)
                .entityType(request.getEntityType())
                .build();

        Category saved = categoryRepository.save(category);
        return mapToCategoryResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<TagResponse> getTags(String userEmail) {
        User user = getUser(userEmail);
        return tagRepository.findAllByUserIdAndDeletedAtIsNull(user.getId()).stream()
                .map(this::mapToTagResponse)
                .toList();
    }

    @Transactional
    public TagResponse createTag(String userEmail, CreateTagRequest request) {
        User user = getUser(userEmail);

        Tag tag = Tag.builder()
                .user(user)
                .name(request.getName())
                .color(request.getColor() != null ? request.getColor() : "#6B7280")
                .build();

        Tag saved = tagRepository.save(tag);
        return mapToTagResponse(saved);
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private CategoryResponse mapToCategoryResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .parentId(category.getParent() != null ? category.getParent().getId() : null)
                .entityType(category.getEntityType())
                .build();
    }

    private TagResponse mapToTagResponse(Tag tag) {
        return TagResponse.builder()
                .id(tag.getId())
                .name(tag.getName())
                .color(tag.getColor())
                .build();
    }
}
