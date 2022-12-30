package com.sneakershop.kiva.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sneakershop.kiva.entity.Statistic;
import com.sneakershop.kiva.model.dto.StatisticDTO;

import java.util.List;

@Repository
public interface StatisticRepository extends JpaRepository<Statistic, Long> {

    @Query(name = "getStatistic30Day",nativeQuery = true)
    List<StatisticDTO> getStatistic30Day();

    @Query(name = "getStatisticDayByDay",nativeQuery = true)
    List<StatisticDTO> getStatisticDayByDay(String toDate, String formDate);

    @Query(value = "SELECT * FROM statistic  WHERE date_format(created_at,'%Y-%m-%d') = date_format(NOW(),'%Y-%m-%d')",nativeQuery = true)
    Statistic findByCreatedAT();


}
