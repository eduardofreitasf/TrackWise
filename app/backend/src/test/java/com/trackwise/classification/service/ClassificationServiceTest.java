package com.trackwise.classification.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.trackwise.classification.dto.CategoryResponse;
import com.trackwise.classification.dto.CreateCategoryRequest;
import com.trackwise.classification.dto.CreateTagRequest;
import com.trackwise.classification.dto.TagResponse;
import com.trackwise.classification.model.Category;
import com.trackwise.classification.model.CategoryEntityType;
import com.trackwise.classification.model.Tag;
import com.trackwise.classification.repository.CategoryRepository;
import com.trackwise.classification.repository.TagRepository;
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

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ClassificationService classificationService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("user@example.com")
                .firstName("Alice")
                .lastName("Smith")
                .build();
        user.setId(1L);
    }

    @Test
    void createCategory_ShouldSaveAndReturnResponse() {
        CreateCategoryRequest request = CreateCategoryRequest.builder()
                .name("Electronics")
                .entityType(CategoryEntityType.ASSET)
                .build();

        Category savedCategory = Category.builder()
                .user(user)
                .name("Electronics")
                .entityType(CategoryEntityType.ASSET)
                .build();
        savedCategory.setId(10L);

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);

        CategoryResponse response = classificationService.createCategory("user@example.com", request);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getName()).isEqualTo("Electronics");
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void createTag_ShouldSaveAndReturnResponse() {
        CreateTagRequest request = CreateTagRequest.builder()
                .name("Important")
                .color("#FF0000")
                .build();

        Tag savedTag = Tag.builder()
                .user(user)
                .name("Important")
                .color("#FF0000")
                .build();
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
        Category cat = Category.builder().user(user).name("Real Estate").entityType(CategoryEntityType.ASSET).build();
        cat.setId(1L);

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(categoryRepository.findAllByUserIdAndDeletedAtIsNull(1L)).thenReturn(List.of(cat));

        List<CategoryResponse> result = classificationService.getCategories("user@example.com", null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Real Estate");
    }
}
