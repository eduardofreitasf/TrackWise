package com.trackwise.asset.service;

import com.trackwise.asset.dto.AssetResponse;
import com.trackwise.asset.dto.CreateAssetRequest;
import com.trackwise.asset.dto.MoneyDto;
import com.trackwise.asset.model.Asset;
import com.trackwise.asset.model.AssetStatus;
import com.trackwise.asset.repository.AssetRepository;
import com.trackwise.classification.dto.CategoryResponse;
import com.trackwise.classification.dto.TagResponse;
import com.trackwise.classification.model.Category;
import com.trackwise.classification.model.Tag;
import com.trackwise.classification.repository.CategoryRepository;
import com.trackwise.classification.repository.TagRepository;
import com.trackwise.common.exception.ResourceNotFoundException;
import com.trackwise.user.model.User;
import com.trackwise.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AssetService {

    private final AssetRepository assetRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    @Transactional
    public AssetResponse createAsset(String userEmail, CreateAssetRequest request) {
        User user = getUser(userEmail);

        Category category = null;
        if (request.getCategoryId() != null) {
            category =
                    categoryRepository
                            .findByIdAndUserIdAndDeletedAtIsNull(
                                    request.getCategoryId(), user.getId())
                            .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        }

        Set<Tag> tags = new HashSet<>();
        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            tags =
                    tagRepository.findAllByIdInAndUserIdAndDeletedAtIsNull(
                            request.getTagIds(), user.getId());
        }

        Asset asset =
                Asset.builder()
                        .user(user)
                        .name(request.getName())
                        .description(request.getDescription())
                        .category(category)
                        .tags(tags)
                        .purchaseDate(request.getPurchaseDate())
                        .purchasePrice(
                                request.getPurchasePrice() != null
                                        ? request.getPurchasePrice().getAmount()
                                        : null)
                        .purchaseCurrency(
                                request.getPurchasePrice() != null
                                        ? request.getPurchasePrice().getCurrency()
                                        : null)
                        .currentValue(
                                request.getCurrentValue() != null
                                        ? request.getCurrentValue().getAmount()
                                        : null)
                        .currentValueCurrency(
                                request.getCurrentValue() != null
                                        ? request.getCurrentValue().getCurrency()
                                        : null)
                        .status(AssetStatus.ACTIVE)
                        .build();

        Asset saved = assetRepository.save(asset);
        return mapToAssetResponse(saved);
    }

    @Transactional(readOnly = true)
    public Page<AssetResponse> getAssets(String userEmail, AssetStatus status, Pageable pageable) {
        User user = getUser(userEmail);
        Page<Asset> assets =
                (status != null)
                        ? assetRepository.findAllByUserIdAndStatusAndDeletedAtIsNull(
                                user.getId(), status, pageable)
                        : assetRepository.findAllByUserIdAndDeletedAtIsNull(user.getId(), pageable);

        return assets.map(this::mapToAssetResponse);
    }

    @Transactional(readOnly = true)
    public AssetResponse getAssetById(String userEmail, Long assetId) {
        User user = getUser(userEmail);
        Asset asset =
                assetRepository
                        .findByIdAndUserIdAndDeletedAtIsNull(assetId, user.getId())
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "Asset not found with id: " + assetId));

        return mapToAssetResponse(asset);
    }

    @Transactional
    public AssetResponse updateAsset(String userEmail, Long assetId, CreateAssetRequest request) {
        User user = getUser(userEmail);
        Asset asset =
                assetRepository
                        .findByIdAndUserIdAndDeletedAtIsNull(assetId, user.getId())
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "Asset not found with id: " + assetId));

        Category category = null;
        if (request.getCategoryId() != null) {
            category =
                    categoryRepository
                            .findByIdAndUserIdAndDeletedAtIsNull(
                                    request.getCategoryId(), user.getId())
                            .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        }

        Set<Tag> tags = new HashSet<>();
        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            tags =
                    tagRepository.findAllByIdInAndUserIdAndDeletedAtIsNull(
                            request.getTagIds(), user.getId());
        }

        asset.setName(request.getName());
        asset.setDescription(request.getDescription());
        asset.setCategory(category);
        asset.setTags(tags);
        asset.setPurchaseDate(request.getPurchaseDate());
        asset.setPurchasePrice(
                request.getPurchasePrice() != null ? request.getPurchasePrice().getAmount() : null);
        asset.setPurchaseCurrency(
                request.getPurchasePrice() != null
                        ? request.getPurchasePrice().getCurrency()
                        : null);
        asset.setCurrentValue(
                request.getCurrentValue() != null ? request.getCurrentValue().getAmount() : null);
        asset.setCurrentValueCurrency(
                request.getCurrentValue() != null ? request.getCurrentValue().getCurrency() : null);

        Asset updated = assetRepository.save(asset);
        return mapToAssetResponse(updated);
    }

    @Transactional
    public AssetResponse updateAssetStatus(String userEmail, Long assetId, AssetStatus status) {
        User user = getUser(userEmail);
        Asset asset =
                assetRepository
                        .findByIdAndUserIdAndDeletedAtIsNull(assetId, user.getId())
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "Asset not found with id: " + assetId));

        asset.setStatus(status);
        Asset updated = assetRepository.save(asset);
        return mapToAssetResponse(updated);
    }

    @Transactional
    public void deleteAsset(String userEmail, Long assetId) {
        User user = getUser(userEmail);
        Asset asset =
                assetRepository
                        .findByIdAndUserIdAndDeletedAtIsNull(assetId, user.getId())
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "Asset not found with id: " + assetId));

        asset.setDeletedAt(LocalDateTime.now());
        assetRepository.save(asset);
    }

    private User getUser(String email) {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private AssetResponse mapToAssetResponse(Asset asset) {
        MoneyDto purchasePrice =
                (asset.getPurchasePrice() != null)
                        ? MoneyDto.builder()
                                .amount(asset.getPurchasePrice())
                                .currency(asset.getPurchaseCurrency())
                                .build()
                        : null;

        MoneyDto currentValue =
                (asset.getCurrentValue() != null)
                        ? MoneyDto.builder()
                                .amount(asset.getCurrentValue())
                                .currency(asset.getCurrentValueCurrency())
                                .build()
                        : null;

        CategoryResponse categoryResponse =
                (asset.getCategory() != null)
                        ? CategoryResponse.builder()
                                .id(asset.getCategory().getId())
                                .name(asset.getCategory().getName())
                                .parentId(
                                        asset.getCategory().getParent() != null
                                                ? asset.getCategory().getParent().getId()
                                                : null)
                                .entityType(asset.getCategory().getEntityType())
                                .build()
                        : null;

        Set<TagResponse> tagResponses =
                (asset.getTags() != null)
                        ? asset.getTags().stream()
                                .map(
                                        tag ->
                                                TagResponse.builder()
                                                        .id(tag.getId())
                                                        .name(tag.getName())
                                                        .color(tag.getColor())
                                                        .build())
                                .collect(Collectors.toSet())
                        : Collections.emptySet();

        return AssetResponse.builder()
                .id(asset.getId())
                .name(asset.getName())
                .description(asset.getDescription())
                .category(categoryResponse)
                .tags(tagResponses)
                .purchaseDate(asset.getPurchaseDate())
                .purchasePrice(purchasePrice)
                .currentValue(currentValue)
                .status(asset.getStatus())
                .createdAt(asset.getCreatedAt())
                .updatedAt(asset.getUpdatedAt())
                .build();
    }
}
