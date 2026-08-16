package com.trackwise.scheduling.dto;

import com.trackwise.scheduling.model.Frequency;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecurrenceRuleRequest {

    @NotNull private Frequency frequency;

    @NotNull
    @Min(1)
    private Integer interval;

    @NotNull private LocalDate startDate;

    private LocalDate endDate;
}
