package order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    @NotNull(message = "Ticker cant be empty")
    @NotBlank
    private String ticker;
    @NotNull
    @NotBlank
    private LongShort type;
    private Integer sum;
    private Integer result;
}