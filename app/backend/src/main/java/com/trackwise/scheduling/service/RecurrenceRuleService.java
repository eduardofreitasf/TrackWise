package com.trackwise.scheduling.service;

import com.trackwise.scheduling.dto.RecurrenceRuleRequest;
import com.trackwise.scheduling.dto.RecurrenceRuleResponse;
import com.trackwise.scheduling.model.Frequency;
import com.trackwise.scheduling.model.RecurrenceRule;
import com.trackwise.scheduling.repository.RecurrenceRuleRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecurrenceRuleService {

    private final RecurrenceRuleRepository repository;

    @Transactional
    public RecurrenceRule createRule(RecurrenceRuleRequest request) {
        LocalDate nextOccurrence =
                calculateNextOccurrence(
                        request.getFrequency(), request.getInterval(), request.getStartDate());

        RecurrenceRule rule =
                RecurrenceRule.builder()
                        .frequency(request.getFrequency())
                        .interval(request.getInterval())
                        .startDate(request.getStartDate())
                        .endDate(request.getEndDate())
                        .nextOccurrence(nextOccurrence)
                        .build();

        return repository.save(rule);
    }

    public LocalDate calculateNextOccurrence(
            Frequency frequency, int interval, LocalDate fromDate) {
        return switch (frequency) {
            case DAILY -> fromDate.plusDays(interval);
            case WEEKLY -> fromDate.plusWeeks(interval);
            case MONTHLY -> fromDate.plusMonths(interval);
            case QUARTERLY -> fromDate.plusMonths(3L * interval);
            case ANNUALLY -> fromDate.plusYears(interval);
            case CUSTOM -> fromDate.plusDays(interval);
        };
    }

    @Transactional
    public void advanceOccurrence(RecurrenceRule rule) {
        if (rule.getNextOccurrence() == null) {
            return;
        }

        LocalDate next =
                calculateNextOccurrence(
                        rule.getFrequency(), rule.getInterval(), rule.getNextOccurrence());

        if (rule.getEndDate() != null && next.isAfter(rule.getEndDate())) {
            rule.setNextOccurrence(null);
        } else {
            rule.setNextOccurrence(next);
        }

        repository.save(rule);
    }

    public RecurrenceRuleResponse mapToResponse(RecurrenceRule rule) {
        return RecurrenceRuleResponse.builder()
                .id(rule.getId())
                .frequency(rule.getFrequency())
                .interval(rule.getInterval())
                .startDate(rule.getStartDate())
                .endDate(rule.getEndDate())
                .nextOccurrence(rule.getNextOccurrence())
                .build();
    }
}
