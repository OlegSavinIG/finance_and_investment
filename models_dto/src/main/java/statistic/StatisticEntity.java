package statistic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import order.LongShort;

import java.time.LocalDateTime;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class StatisticEntity {
    private Long userId;
    private String ticker;
    private LongShort type;
    private Integer result;
    private LocalDateTime creationTime;
    private LocalDateTime closedTime;
    private LocalDateTime programCreationTime;
}
