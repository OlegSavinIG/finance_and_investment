package oleg.savin.statistics.controller;

import lombok.RequiredArgsConstructor;
import oleg.savin.statistic_dto.StatisticResponse;
import oleg.savin.statistics.entity.StatisticSearchCriteria;
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
    public ResponseEntity<List<StatisticResponse>> getStatisticsByUserId(
            @PathVariable Long userId) {
        return ResponseEntity.ok(statisticService.findByUserId(userId));
    }
    @GetMapping("/search")
    public ResponseEntity<List<StatisticResponse>> searchStatistics(
            @RequestBody StatisticSearchCriteria criteria) {
        return ResponseEntity.ok(statisticService.findByCriteria(criteria));
    }

}
