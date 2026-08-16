package com.trackwise.asset.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.trackwise.asset.dto.AssetResponse;
import com.trackwise.asset.dto.CreateAssetRequest;
import com.trackwise.asset.dto.MoneyDto;
import com.trackwise.asset.model.Asset;
import com.trackwise.asset.model.AssetStatus;
import com.trackwise.asset.repository.AssetRepository;
import com.trackwise.classification.repository.CategoryRepository;
import com.trackwise.classification.repository.TagRepository;
import com.trackwise.common.exception.ResourceNotFoundException;
import com.trackwise.user.model.User;
import com.trackwise.user.repository.UserRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AssetServiceTest {

    @Mock private AssetRepository assetRepository;

    @Mock private CategoryRepository categoryRepository;

    @Mock private TagRepository tagRepository;

    @Mock private UserRepository userRepository;

    @InjectMocks private AssetService assetService;

    private User user;
    private Asset asset;
    private CreateAssetRequest createRequest;

    @BeforeEach
    void setUp() {
        user = User.builder().email("user@example.com").firstName("John").lastName("Doe").build();
        user.setId(1L);

        createRequest =
                CreateAssetRequest.builder()
                        .name("MacBook Pro")
                        .description("Work laptop")
                        .purchaseDate(LocalDate.of(2023, 1, 15))
                        .purchasePrice(
                                MoneyDto.builder()
                                        .amount(new BigDecimal("2499.99"))
                                        .currency("USD")
                                        .build())
                        .currentValue(
                                MoneyDto.builder()
                                        .amount(new BigDecimal("2000.00"))
                                        .currency("USD")
                                        .build())
                        .build();

        asset =
                Asset.builder()
                        .user(user)
                        .name("MacBook Pro")
                        .description("Work laptop")
                        .purchaseDate(LocalDate.of(2023, 1, 15))
                        .purchasePrice(new BigDecimal("2499.99"))
                        .purchaseCurrency("USD")
                        .currentValue(new BigDecimal("2000.00"))
                        .currentValueCurrency("USD")
                        .status(AssetStatus.ACTIVE)
                        .tags(Collections.emptySet())
                        .build();
        asset.setId(100L);
    }

    @Test
    void createAsset_ShouldSaveAndReturnAssetResponse() {
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(assetRepository.save(any(Asset.class))).thenReturn(asset);

        AssetResponse response = assetService.createAsset("user@example.com", createRequest);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(100L);
        assertThat(response.getName()).isEqualTo("MacBook Pro");
        assertThat(response.getStatus()).isEqualTo(AssetStatus.ACTIVE);
        verify(assetRepository).save(any(Asset.class));
    }

    @Test
    void getAssetById_ShouldReturnAsset_WhenExists() {
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(assetRepository.findByIdAndUserIdAndDeletedAtIsNull(100L, 1L))
                .thenReturn(Optional.of(asset));

        AssetResponse response = assetService.getAssetById("user@example.com", 100L);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(100L);
        assertThat(response.getName()).isEqualTo("MacBook Pro");
    }

    @Test
    void getAssetById_ShouldThrowException_WhenNotFoundOrNotOwned() {
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(assetRepository.findByIdAndUserIdAndDeletedAtIsNull(100L, 1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> assetService.getAssetById("user@example.com", 100L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Asset not found");
    }

    @Test
    void deleteAsset_ShouldSetDeletedAt_WhenAssetExists() {
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(assetRepository.findByIdAndUserIdAndDeletedAtIsNull(100L, 1L))
                .thenReturn(Optional.of(asset));

        assetService.deleteAsset("user@example.com", 100L);

        assertThat(asset.getDeletedAt()).isNotNull();
        verify(assetRepository).save(asset);
    }
}
