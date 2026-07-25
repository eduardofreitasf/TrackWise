package com.trackwise.asset.dto;

import com.trackwise.asset.model.AssetStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAssetStatusRequest {

    @NotNull(message = "Status is required")
    private AssetStatus status;
}
