package oleg.savin.statistics.controller;

import lombok.RequiredArgsConstructor;
import oleg.savin.statistics.entity.StatisticSearchCriteria;
import oleg.savin.statistics.repository.StatisticRepository;
import oleg.savin.statistics.entity.StatisticEntity;
import oleg.savin.statistics.service.StatisticService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticController {

    private final StatisticService statisticService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<StatisticEntity>> getStatisticsByUserId(
            @PathVariable Long userId) {
        List<StatisticEntity> statistics = statisticService.findByUserId(userId);
        return ResponseEntity.ok(statistics);
    }
    @GetMapping("/search")
    public ResponseEntity<List<StatisticEntity>> searchStatistics(
            @RequestBody StatisticSearchCriteria criteria) {
        List<StatisticEntity> statistics = statisticService.findByCriteria(criteria);
        return ResponseEntity.ok(statistics);
    }

}
