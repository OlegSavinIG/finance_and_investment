package oleg.savin.statistics.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import oleg.savin.order_dto.LongShort;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticSearchCriteria {
    private Long userId;
    private String ticker;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer result;
    private LongShort type;
}
