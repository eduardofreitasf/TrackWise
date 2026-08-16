package com.trackwise.scheduling.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.trackwise.scheduling.dto.RecurrenceRuleRequest;
import com.trackwise.scheduling.model.Frequency;
import com.trackwise.scheduling.model.RecurrenceRule;
import com.trackwise.scheduling.repository.RecurrenceRuleRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RecurrenceRuleServiceTest {

    @Mock private RecurrenceRuleRepository repository;

    @InjectMocks private RecurrenceRuleService service;

    @Test
    void createRule_ShouldCalculateNextOccurrenceAndSave() {
        RecurrenceRuleRequest request =
                RecurrenceRuleRequest.builder()
                        .frequency(Frequency.DAILY)
                        .interval(2)
                        .startDate(LocalDate.of(2023, 1, 1))
                        .build();

        RecurrenceRule expectedRule =
                RecurrenceRule.builder()
                        .id(1L)
                        .frequency(Frequency.DAILY)
                        .interval(2)
                        .startDate(LocalDate.of(2023, 1, 1))
                        .nextOccurrence(LocalDate.of(2023, 1, 3))
                        .build();

        when(repository.save(any(RecurrenceRule.class))).thenReturn(expectedRule);

        RecurrenceRule result = service.createRule(request);

        assertThat(result).isNotNull();
        assertThat(result.getNextOccurrence()).isEqualTo(LocalDate.of(2023, 1, 3));

        ArgumentCaptor<RecurrenceRule> captor = ArgumentCaptor.forClass(RecurrenceRule.class);
        verify(repository).save(captor.capture());
        RecurrenceRule saved = captor.getValue();
        assertThat(saved.getFrequency()).isEqualTo(Frequency.DAILY);
        assertThat(saved.getInterval()).isEqualTo(2);
        assertThat(saved.getStartDate()).isEqualTo(LocalDate.of(2023, 1, 1));
        assertThat(saved.getNextOccurrence()).isEqualTo(LocalDate.of(2023, 1, 3));
    }

    @Test
    void calculateNextOccurrence_Daily_ShouldAddDays() {
        LocalDate from = LocalDate.of(2023, 1, 1);
        LocalDate next = service.calculateNextOccurrence(Frequency.DAILY, 5, from);
        assertThat(next).isEqualTo(LocalDate.of(2023, 1, 6));
    }

    @Test
    void calculateNextOccurrence_Weekly_ShouldAddWeeks() {
        LocalDate from = LocalDate.of(2023, 1, 1);
        LocalDate next = service.calculateNextOccurrence(Frequency.WEEKLY, 2, from);
        assertThat(next).isEqualTo(LocalDate.of(2023, 1, 15));
    }

    @Test
    void calculateNextOccurrence_Monthly_ShouldAddMonths() {
        LocalDate from = LocalDate.of(2023, 1, 1);
        LocalDate next = service.calculateNextOccurrence(Frequency.MONTHLY, 1, from);
        assertThat(next).isEqualTo(LocalDate.of(2023, 2, 1));
    }

    @Test
    void calculateNextOccurrence_Quarterly_ShouldAddThreeMonths() {
        LocalDate from = LocalDate.of(2023, 1, 1);
        LocalDate next = service.calculateNextOccurrence(Frequency.QUARTERLY, 1, from);
        assertThat(next).isEqualTo(LocalDate.of(2023, 4, 1));
    }

    @Test
    void calculateNextOccurrence_Annually_ShouldAddYears() {
        LocalDate from = LocalDate.of(2023, 1, 1);
        LocalDate next = service.calculateNextOccurrence(Frequency.ANNUALLY, 2, from);
        assertThat(next).isEqualTo(LocalDate.of(2025, 1, 1));
    }

    @Test
    void calculateNextOccurrence_Custom_ShouldAddDays() {
        LocalDate from = LocalDate.of(2023, 1, 1);
        LocalDate next = service.calculateNextOccurrence(Frequency.CUSTOM, 10, from);
        assertThat(next).isEqualTo(LocalDate.of(2023, 1, 11));
    }

    @Test
    void advanceOccurrence_ShouldUpdateNextOccurrence() {
        RecurrenceRule rule =
                RecurrenceRule.builder()
                        .frequency(Frequency.DAILY)
                        .interval(1)
                        .startDate(LocalDate.of(2023, 1, 1))
                        .nextOccurrence(LocalDate.of(2023, 1, 2))
                        .build();

        service.advanceOccurrence(rule);

        assertThat(rule.getNextOccurrence()).isEqualTo(LocalDate.of(2023, 1, 3));
        verify(repository).save(rule);
    }

    @Test
    void advanceOccurrence_ShouldSetNullWhenPastEndDate() {
        RecurrenceRule rule =
                RecurrenceRule.builder()
                        .frequency(Frequency.DAILY)
                        .interval(1)
                        .startDate(LocalDate.of(2023, 1, 1))
                        .endDate(LocalDate.of(2023, 1, 2))
                        .nextOccurrence(LocalDate.of(2023, 1, 2))
                        .build();

        service.advanceOccurrence(rule);

        assertThat(rule.getNextOccurrence()).isNull();
        verify(repository).save(rule);
    }
}
