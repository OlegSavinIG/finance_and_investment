package repository;

import lombok.RequiredArgsConstructor;
import order.LongShort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import statistic.StatisticEntity;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StatisticRepository {

    private final JdbcTemplate jdbcTemplate;

    public void saveStatistic(StatisticEntity statistic) {
        String sql = "INSERT INTO statistic_entity (" +
                "id, ticker, type, result, creationTime, closedTime, userId) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                statistic.getId(),
                statistic.getTicker(),
                statistic.getType().name(),
                statistic.getResult(),
                statistic.getCreationTime(),
                statistic.getClosedTime(),
                statistic.getUserId()
        );
    }

    public List<StatisticEntity> findByUserId(Long userId) {
        String sql = "SELECT * FROM statistic_entity WHERE userId = ?";
        return jdbcTemplate.query(sql, new Object[]{userId}, (rs, rowNum) ->
                StatisticEntity.builder()
                        .id(rs.getLong("id"))
                        .ticker(rs.getString("ticker"))
                        .type(LongShort.valueOf(rs.getString("type")))
                        .result(rs.getInt("result"))
                        .creationTime(rs.getTimestamp("creationTime").toLocalDateTime())
                        .closedTime(rs.getTimestamp("closedTime").toLocalDateTime())
                        .userId(rs.getLong("userId"))
                        .build()
        );
    }
}
