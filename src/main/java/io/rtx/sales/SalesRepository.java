package io.rtx.sales;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface SalesRepository extends JpaRepository<SalesEntity, Long>, QuerydslPredicateExecutor<SalesEntity> {

    List<SalesEntity> findByCountry(String product);

    @Query(value = "SELECT s from SalesEntity s WHERE s.date BETWEEN :startDate AND :endDate")
    List<SalesEntity> findByDateBetween(LocalDate startDate, LocalDate endDate);

    @Query(value = "SELECT DISTINCT s.country from SalesEntity s")
    List<String> listCountry();

    @Query(value = "SELECT s from SalesEntity s WHERE s.product LIKE :product")
    List<SalesEntity> findByProduct(String product);

    @Query(value = "SELECT DISTINCT s.product from SalesEntity s")
    List<String> listProduct();

    @Query("SELECT s FROM SalesEntity s WHERE s.country LIKE :country AND s.product LIKE :product AND s.date BETWEEN :startDate AND :endDate")
    List<SalesEntity> search(String country, String product, LocalDate startDate, LocalDate endDate);

    @Query("SELECT s FROM SalesEntity s WHERE s.date BETWEEN :startDate AND :endDate ORDER BY s.profit DESC")
    List<SalesEntity> getBestSales(LocalDate startDate, LocalDate endDate);

    @Query("SELECT s FROM SalesEntity s WHERE s.date BETWEEN :startDate AND :endDate ORDER BY s.profit ASC")
    List<SalesEntity> getWorstSales(LocalDate startDate, LocalDate endDate);
}
