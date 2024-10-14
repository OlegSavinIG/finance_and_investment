package oleg.savin.order_dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import oleg.savin.annotation.ValidTimeRange;

import java.time.LocalDateTime;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@ValidTimeRange(
        startField = "creationTime", endField = "closedTime",
        message = "Creation time must be before closed time")
public class OrderRequest {
    @NotNull(message = "Ticker cant be empty")
    @NotBlank
    private String ticker;
    @NotNull
    @NotBlank
    private LongShort type;
    @NotNull
    @NotBlank
    private Integer sum;
    private Integer result;
    @JsonFormat(shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime closedTime;
}