package oleg.savin.statistic_dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import oleg.savin.order_dto.LongShort;

import java.time.LocalDateTime;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class StatisticRequest{
    private Long userId;
    private String ticker;
    private LongShort type;
    private Integer result;
    private LocalDateTime creationTime;
    private LocalDateTime closedTime;
    private LocalDateTime programCreationTime;
}
