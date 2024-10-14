package oleg.savin.statistics.controller;

import lombok.RequiredArgsConstructor;
import oleg.savin.statistics.repository.StatisticRepository;
import oleg.savin.statistics.entity.StatisticEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticController {

    private final StatisticRepository statisticRepository;
    @GetMapping("/test")
    public String testEndpoint() {
        return "Controller is working!";
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<StatisticEntity>> getStatisticsByUserId(
            @PathVariable Long userId) {
        List<StatisticEntity> statistics = statisticRepository.findByUserId(userId);
        return ResponseEntity.ok(statistics);
    }
}
