package statistic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import order.LongShort;
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class StatisticResponse {
    private Long id;
    private String ticker;
    private LongShort type;
    private Integer result;
}
