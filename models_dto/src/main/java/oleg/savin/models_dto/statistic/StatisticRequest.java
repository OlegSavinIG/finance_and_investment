package oleg.savin.models_dto.statistic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import oleg.savin.models_dto.order.LongShort;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class StatisticRequest implements Serializable {
    private Long userId;
    private String ticker;
    private LongShort type;
    private Integer result;
    private LocalDateTime creationTime;
    private LocalDateTime closedTime;
    private LocalDateTime programCreationTime;
}
