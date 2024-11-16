package oleg.savin.statistics.repository;

import oleg.savin.statistics.entity.StatisticEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatisticRepository extends JpaRepository<StatisticEntity, Long> , JpaSpecificationExecutor<StatisticEntity> {
    List<StatisticEntity> findByUserId(Long userId);

}
