package com.trackwise.classification.service;

import com.trackwise.classification.dto.CategoryResponse;
import com.trackwise.classification.dto.CreateCategoryRequest;
import com.trackwise.classification.dto.CreateTagRequest;
import com.trackwise.classification.dto.TagResponse;
import com.trackwise.classification.dto.UpdateCategoryRequest;
import com.trackwise.classification.dto.UpdateTagRequest;
import com.trackwise.classification.model.Category;
import com.trackwise.classification.model.CategoryEntityType;
import com.trackwise.classification.model.Tag;
import com.trackwise.classification.repository.CategoryRepository;
import com.trackwise.classification.repository.TagRepository;
import com.trackwise.common.exception.BusinessException;
import com.trackwise.common.exception.ResourceNotFoundException;
import com.trackwise.user.model.User;
import com.trackwise.user.repository.UserRepository;
import java.time.LocalDateTime;
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
        List<Category> categories =
                (entityType != null)
                        ? categoryRepository.findAllByUserIdAndEntityTypeAndDeletedAtIsNull(
                                user.getId(), entityType)
                        : categoryRepository.findAllByUserIdAndDeletedAtIsNull(user.getId());

        return categories.stream().map(this::mapToCategoryResponse).toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(String userEmail, Long categoryId) {
        User user = getUser(userEmail);
        Category category = findCategoryByIdAndUser(categoryId, user.getId());
        return mapToCategoryResponse(category);
    }

    @Transactional
    public CategoryResponse createCategory(String userEmail, CreateCategoryRequest request) {
        User user = getUser(userEmail);

        Category parent = null;
        if (request.getParentId() != null) {
            parent = findCategoryByIdAndUser(request.getParentId(), user.getId());
        }

        Category category =
                Category.builder()
                        .user(user)
                        .name(request.getName())
                        .parent(parent)
                        .entityType(request.getEntityType())
                        .build();

        Category saved = categoryRepository.save(category);
        return mapToCategoryResponse(saved);
    }

    @Transactional
    public CategoryResponse updateCategory(
            String userEmail, Long categoryId, UpdateCategoryRequest request) {
        User user = getUser(userEmail);
        Category category = findCategoryByIdAndUser(categoryId, user.getId());

        validateParentNotSelf(categoryId, request.getParentId());

        Category parent = null;
        if (request.getParentId() != null) {
            parent = findCategoryByIdAndUser(request.getParentId(), user.getId());
        }

        category.setName(request.getName());
        category.setParent(parent);

        Category saved = categoryRepository.save(category);
        return mapToCategoryResponse(saved);
    }

    @Transactional
    public void deleteCategory(String userEmail, Long categoryId) {
        User user = getUser(userEmail);
        Category category = findCategoryByIdAndUser(categoryId, user.getId());

        if (categoryRepository.existsByParentIdAndDeletedAtIsNull(categoryId)) {
            throw new BusinessException(
                    "Cannot delete category with active subcategories. Delete or reassign children first.");
        }

        category.setDeletedAt(LocalDateTime.now());
        categoryRepository.save(category);
    }

    @Transactional(readOnly = true)
    public List<TagResponse> getTags(String userEmail) {
        User user = getUser(userEmail);
        return tagRepository.findAllByUserIdAndDeletedAtIsNull(user.getId()).stream()
                .map(this::mapToTagResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TagResponse getTagById(String userEmail, Long tagId) {
        User user = getUser(userEmail);
        Tag tag = findTagByIdAndUser(tagId, user.getId());
        return mapToTagResponse(tag);
    }

    @Transactional
    public TagResponse createTag(String userEmail, CreateTagRequest request) {
        User user = getUser(userEmail);

        Tag tag =
                Tag.builder()
                        .user(user)
                        .name(request.getName())
                        .color(request.getColor() != null ? request.getColor() : "#6B7280")
                        .build();

        Tag saved = tagRepository.save(tag);
        return mapToTagResponse(saved);
    }

    @Transactional
    public TagResponse updateTag(String userEmail, Long tagId, UpdateTagRequest request) {
        User user = getUser(userEmail);
        Tag tag = findTagByIdAndUser(tagId, user.getId());

        tag.setName(request.getName());
        if (request.getColor() != null) {
            tag.setColor(request.getColor());
        }

        Tag saved = tagRepository.save(tag);
        return mapToTagResponse(saved);
    }

    @Transactional
    public void deleteTag(String userEmail, Long tagId) {
        User user = getUser(userEmail);
        Tag tag = findTagByIdAndUser(tagId, user.getId());

        tag.setDeletedAt(LocalDateTime.now());
        tagRepository.save(tag);
    }

    private User getUser(String email) {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private Category findCategoryByIdAndUser(Long categoryId, Long userId) {
        return categoryRepository
                .findByIdAndUserIdAndDeletedAtIsNull(categoryId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    private Tag findTagByIdAndUser(Long tagId, Long userId) {
        return tagRepository
                .findByIdAndUserIdAndDeletedAtIsNull(tagId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found"));
    }

    private void validateParentNotSelf(Long categoryId, Long parentId) {
        if (parentId != null && parentId.equals(categoryId)) {
            throw new BusinessException("A category cannot be its own parent");
        }
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
