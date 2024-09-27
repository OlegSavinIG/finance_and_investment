package service;

import exceptions.StatisticRepositoryException;
import lombok.RequiredArgsConstructor;
import order.LongShort;
import org.springframework.dao.DataAccessException;
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
                "userId, ticker, type, result, creationTime, closedTime) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try {
            jdbcTemplate.update(sql,
                    statistic.getUserId(),
                    statistic.getTicker(),
                    statistic.getType().name(),
                    statistic.getResult(),
                    statistic.getCreationTime(),
                    statistic.getClosedTime()
            );
        } catch (DataAccessException e) {
            throw new StatisticRepositoryException("Failed to save statistic entity", e);
        }
    }

    public List<StatisticEntity> findByUserId(Long userId) {
        String sql = "SELECT * FROM statistic_entity WHERE userId = ?";

        try {
            return jdbcTemplate.query(sql, new Object[]{userId}, (rs, rowNum) ->
                    StatisticEntity.builder()
                            .userId(rs.getLong("userId"))
                            .ticker(rs.getString("ticker"))
                            .type(LongShort.valueOf(rs.getString("type")))
                            .result(rs.getInt("result"))
                            .creationTime(rs.getTimestamp("creationTime").toLocalDateTime())
                            .closedTime(rs.getTimestamp("closedTime").toLocalDateTime())
                            .build()
            );
        } catch (DataAccessException e) {
            throw new StatisticRepositoryException("Failed to fetch statistics for userId: " + userId, e);
        }
    }
}
