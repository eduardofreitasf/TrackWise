package com.trackwise.classification.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClassificationServiceTest {

    @Mock private CategoryRepository categoryRepository;

    @Mock private TagRepository tagRepository;

    @Mock private UserRepository userRepository;

    @InjectMocks private ClassificationService classificationService;

    private User user;

    @BeforeEach
    void setUp() {
        user =
                User.builder()
                        .email("user@example.com")
                        .firstName("Alice")
                        .lastName("Smith")
                        .build();
        user.setId(1L);
    }

    @Test
    void createCategory_ShouldSaveAndReturnResponse() {
        CreateCategoryRequest request =
                CreateCategoryRequest.builder()
                        .name("Electronics")
                        .entityType(CategoryEntityType.ASSET)
                        .build();

        Category savedCategory =
                Category.builder()
                        .user(user)
                        .name("Electronics")
                        .entityType(CategoryEntityType.ASSET)
                        .build();
        savedCategory.setId(10L);

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);

        CategoryResponse response =
                classificationService.createCategory("user@example.com", request);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getName()).isEqualTo("Electronics");
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void getCategoryById_ShouldReturnCategory_WhenExists() {
        Category category =
                Category.builder()
                        .user(user)
                        .name("Vehicles")
                        .entityType(CategoryEntityType.ASSET)
                        .build();
        category.setId(10L);

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(categoryRepository.findByIdAndUserIdAndDeletedAtIsNull(10L, 1L))
                .thenReturn(Optional.of(category));

        CategoryResponse response = classificationService.getCategoryById("user@example.com", 10L);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getName()).isEqualTo("Vehicles");
    }

    @Test
    void getCategoryById_ShouldThrowException_WhenNotFound() {
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(categoryRepository.findByIdAndUserIdAndDeletedAtIsNull(99L, 1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> classificationService.getCategoryById("user@example.com", 99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Category not found");
    }

    @Test
    void updateCategory_ShouldUpdateAndReturnResponse() {
        UpdateCategoryRequest request =
                UpdateCategoryRequest.builder().name("Updated Name").build();

        Category existing =
                Category.builder()
                        .user(user)
                        .name("Old Name")
                        .entityType(CategoryEntityType.ASSET)
                        .build();
        existing.setId(10L);

        Category saved =
                Category.builder()
                        .user(user)
                        .name("Updated Name")
                        .entityType(CategoryEntityType.ASSET)
                        .build();
        saved.setId(10L);

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(categoryRepository.findByIdAndUserIdAndDeletedAtIsNull(10L, 1L))
                .thenReturn(Optional.of(existing));
        when(categoryRepository.save(any(Category.class))).thenReturn(saved);

        CategoryResponse response =
                classificationService.updateCategory("user@example.com", 10L, request);

        assertThat(response.getName()).isEqualTo("Updated Name");
    }

    @Test
    void updateCategory_ShouldThrowException_WhenParentIsSelf() {
        UpdateCategoryRequest request =
                UpdateCategoryRequest.builder().name("Name").parentId(10L).build();

        Category existing =
                Category.builder()
                        .user(user)
                        .name("Name")
                        .entityType(CategoryEntityType.ASSET)
                        .build();
        existing.setId(10L);

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(categoryRepository.findByIdAndUserIdAndDeletedAtIsNull(10L, 1L))
                .thenReturn(Optional.of(existing));

        assertThatThrownBy(
                        () ->
                                classificationService.updateCategory(
                                        "user@example.com", 10L, request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("A category cannot be its own parent");
    }

    @Test
    void deleteCategory_ShouldSoftDelete_WhenNoChildren() {
        Category category =
                Category.builder()
                        .user(user)
                        .name("ToDelete")
                        .entityType(CategoryEntityType.ASSET)
                        .build();
        category.setId(10L);

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(categoryRepository.findByIdAndUserIdAndDeletedAtIsNull(10L, 1L))
                .thenReturn(Optional.of(category));
        when(categoryRepository.existsByParentIdAndDeletedAtIsNull(10L)).thenReturn(false);

        classificationService.deleteCategory("user@example.com", 10L);

        assertThat(category.getDeletedAt()).isNotNull();
        verify(categoryRepository).save(category);
    }

    @Test
    void deleteCategory_ShouldThrowException_WhenHasChildren() {
        Category category =
                Category.builder()
                        .user(user)
                        .name("Parent")
                        .entityType(CategoryEntityType.ASSET)
                        .build();
        category.setId(10L);

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(categoryRepository.findByIdAndUserIdAndDeletedAtIsNull(10L, 1L))
                .thenReturn(Optional.of(category));
        when(categoryRepository.existsByParentIdAndDeletedAtIsNull(10L)).thenReturn(true);

        assertThatThrownBy(() -> classificationService.deleteCategory("user@example.com", 10L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("active subcategories");
    }

    @Test
    void createTag_ShouldSaveAndReturnResponse() {
        CreateTagRequest request =
                CreateTagRequest.builder().name("Important").color("#FF0000").build();

        Tag savedTag = Tag.builder().user(user).name("Important").color("#FF0000").build();
        savedTag.setId(5L);

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(tagRepository.save(any(Tag.class))).thenReturn(savedTag);

        TagResponse response = classificationService.createTag("user@example.com", request);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(5L);
        assertThat(response.getName()).isEqualTo("Important");
        assertThat(response.getColor()).isEqualTo("#FF0000");
    }

    @Test
    void getCategories_ShouldReturnCategoryList() {
        Category cat =
                Category.builder()
                        .user(user)
                        .name("Real Estate")
                        .entityType(CategoryEntityType.ASSET)
                        .build();
        cat.setId(1L);

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(categoryRepository.findAllByUserIdAndDeletedAtIsNull(1L)).thenReturn(List.of(cat));

        List<CategoryResponse> result =
                classificationService.getCategories("user@example.com", null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Real Estate");
    }

    @Test
    void getTagById_ShouldReturnTag_WhenExists() {
        Tag tag = Tag.builder().user(user).name("Urgent").color("#FF0000").build();
        tag.setId(5L);

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(tagRepository.findByIdAndUserIdAndDeletedAtIsNull(5L, 1L))
                .thenReturn(Optional.of(tag));

        TagResponse response = classificationService.getTagById("user@example.com", 5L);

        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("Urgent");
    }

    @Test
    void getTagById_ShouldThrowException_WhenNotFound() {
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(tagRepository.findByIdAndUserIdAndDeletedAtIsNull(99L, 1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> classificationService.getTagById("user@example.com", 99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Tag not found");
    }

    @Test
    void updateTag_ShouldUpdateAndReturnResponse() {
        UpdateTagRequest request =
                UpdateTagRequest.builder().name("Renamed").color("#00FF00").build();

        Tag existing = Tag.builder().user(user).name("Old").color("#FF0000").build();
        existing.setId(5L);

        Tag saved = Tag.builder().user(user).name("Renamed").color("#00FF00").build();
        saved.setId(5L);

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(tagRepository.findByIdAndUserIdAndDeletedAtIsNull(5L, 1L))
                .thenReturn(Optional.of(existing));
        when(tagRepository.save(any(Tag.class))).thenReturn(saved);

        TagResponse response = classificationService.updateTag("user@example.com", 5L, request);

        assertThat(response.getName()).isEqualTo("Renamed");
        assertThat(response.getColor()).isEqualTo("#00FF00");
    }

    @Test
    void deleteTag_ShouldSoftDelete() {
        Tag tag = Tag.builder().user(user).name("ToDelete").color("#FF0000").build();
        tag.setId(5L);

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(tagRepository.findByIdAndUserIdAndDeletedAtIsNull(5L, 1L))
                .thenReturn(Optional.of(tag));

        classificationService.deleteTag("user@example.com", 5L);

        assertThat(tag.getDeletedAt()).isNotNull();
        verify(tagRepository).save(tag);
    }
}
