package com.trackwise.scheduling.dto;

import com.trackwise.scheduling.model.Frequency;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecurrenceRuleResponse {
    private Long id;
    private Frequency frequency;
    private Integer interval;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate nextOccurrence;
}
