package com.trackwise.asset.controller;

import com.trackwise.asset.dto.AssetResponse;
import com.trackwise.asset.dto.CreateAssetRequest;
import com.trackwise.asset.dto.UpdateAssetStatusRequest;
import com.trackwise.asset.model.AssetStatus;
import com.trackwise.asset.service.AssetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/assets")
@RequiredArgsConstructor
public class AssetController {

    private final AssetService assetService;

    @PostMapping
    public ResponseEntity<AssetResponse> createAsset(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CreateAssetRequest request
    ) {
        AssetResponse response = assetService.createAsset(userDetails.getUsername(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<AssetResponse>> getAssets(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) AssetStatus status,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<AssetResponse> assets = assetService.getAssets(userDetails.getUsername(), status, pageable);
        return ResponseEntity.ok(assets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssetResponse> getAssetById(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id
    ) {
        AssetResponse asset = assetService.getAssetById(userDetails.getUsername(), id);
        return ResponseEntity.ok(asset);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssetResponse> updateAsset(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @Valid @RequestBody CreateAssetRequest request
    ) {
        AssetResponse updated = assetService.updateAsset(userDetails.getUsername(), id, request);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<AssetResponse> updateAssetStatus(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @Valid @RequestBody UpdateAssetStatusRequest request
    ) {
        AssetResponse updated = assetService.updateAssetStatus(userDetails.getUsername(), id, request.getStatus());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAsset(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id
    ) {
        assetService.deleteAsset(userDetails.getUsername(), id);
        return ResponseEntity.noContent().build();
    }
}
