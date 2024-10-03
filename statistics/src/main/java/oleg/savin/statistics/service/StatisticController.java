package oleg.savin.statistics.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import oleg.savin.models_dto.statistic.StatisticEntity;

import java.util.List;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticController {

    private final StatisticRepository statisticRepository;

    @GetMapping("/oleg/savin/finance/user/{userId}")
    public ResponseEntity<List<StatisticEntity>> getStatisticsByUserId(
            @PathVariable Long userId) {
        List<StatisticEntity> statistics = statisticRepository.findByUserId(userId);
        return ResponseEntity.ok(statistics);
    }
}
