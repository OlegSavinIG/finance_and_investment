import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repository.StatisticRepository;
import statistic.StatisticEntity;

import java.util.List;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticController {

    private final StatisticRepository statisticRepository;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<StatisticEntity>> getStatisticsByUserId(
            @PathVariable Long userId) {
        List<StatisticEntity> statistics = statisticRepository.findByUserId(userId);
        return ResponseEntity.ok(statistics);
    }
}
