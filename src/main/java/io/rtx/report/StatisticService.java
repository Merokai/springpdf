package io.rtx.report;

import io.rtx.sales.SalesEntity;
import io.rtx.sales.SalesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class StatisticService {

    @Autowired
    SalesRepository salesRepository;

    public <T, K extends Comparable, R extends Number> Map<K, R> groupBy(Collection<T> collection, Function<T, R> getter, Class<R> clazz, Function<T, K> key, BinaryOperator<R> operation) {
        // Extract keys (country, product, month...)
        // T Entity type (SalesEntity)
        // K Key type (Month, String (product), String (country))
        // R Attribute type (Long (profits, value), Float (rentability))

        // 0 (int) -> Integer -> Number -> R extends Number
        final R zero = clazz == Long.class ? (R) Long.valueOf(0) : clazz == Float.class ? (R) Float.valueOf(0) : (R) Integer.valueOf(0);

        Set<K> keys = collection.stream()
                .map(key)
                .collect(Collectors.toSet());
        // Group by key
        return keys.stream().collect(
                Collectors.toMap(
                        k -> k,
                        k -> collection.stream()
                                .filter(s -> key.apply(s).equals(k))
                                .map(getter)
                                .reduce(zero, operation)
                )
        );
    }

    public Map<Month, Long> getCaOverMonths(LocalDate start, LocalDate end) {
        Collection<SalesEntity> sales = salesRepository.findByDateBetween(start, end);
        return groupBy(sales, SalesEntity::getValue, Long.class, s -> s.getDate().getMonth(), Long::sum);
    }

    public Map<Month, Long> getProfitsOverMonths(LocalDate start, LocalDate end) {
        Collection<SalesEntity> sales = salesRepository.findByDateBetween(start, end);
        return groupBy(sales, SalesEntity::getProfit, Long.class, s -> s.getDate().getMonth(), Long::sum);
    }

    public Map<String, Long> getCaByCountry(LocalDate start, LocalDate end) {
        Collection<SalesEntity> sales = salesRepository.findByDateBetween(start, end);
        return groupBy(sales, SalesEntity::getValue, Long.class, SalesEntity::getCountry, Long::sum);
    }

    public Map<String, Long> getProfitsByCountry(LocalDate start, LocalDate end) {
        Collection<SalesEntity> sales = salesRepository.findByDateBetween(start, end);
        return groupBy(sales, SalesEntity::getProfit, Long.class, SalesEntity::getCountry, Long::sum);
    }

    public Map<String, Long> getCaByProduct(LocalDate start, LocalDate end) {
        Collection<SalesEntity> sales = salesRepository.findByDateBetween(start, end);
        return groupBy(sales, SalesEntity::getValue, Long.class, SalesEntity::getProduct, Long::sum);
    }

    public Map<String, Long> getProfitsByProduct(LocalDate start, LocalDate end) {
        Collection<SalesEntity> sales = salesRepository.findByDateBetween(start, end);
        return groupBy(sales, SalesEntity::getProfit, Long.class, SalesEntity::getProduct, Long::sum);
    }

    public Map<String, Long> getAvgCaByProduct(LocalDate start, LocalDate end) {
        Collection<SalesEntity> sales = salesRepository.findByDateBetween(start, end);
        Map<String, Long> totalCaByProduct = groupBy(sales, SalesEntity::getValue, Long.class, SalesEntity::getProduct, Long::sum);
        Map<String, Long> salesCountByProduct = groupBy(sales, x -> 1L, Long.class, SalesEntity::getProduct, Long::sum);

        Map<String, Long> avgCaByProduct = new HashMap<>();

        totalCaByProduct.forEach((k, v) -> {
            avgCaByProduct.put(k, totalCaByProduct.get(k) / salesCountByProduct.get(k));
        });
        return avgCaByProduct;
    }

    public Map<String, Float> getAvgRentaByProduct(LocalDate start, LocalDate end) {
        Collection<SalesEntity> sales = salesRepository.findByDateBetween(start, end);
        Map<String, Long> totalCaByProduct = groupBy(sales, SalesEntity::getValue, Long.class, SalesEntity::getProduct, Long::sum);
        Map<String, Long> totalProfitByProduct = groupBy(sales, SalesEntity::getProfit, Long.class, SalesEntity::getProduct, Long::sum);

        Map<String, Float> avgRentaByProduct = new HashMap<>();

        totalCaByProduct.forEach((k, v) -> {
            avgRentaByProduct.put(k, (float) 100 * totalProfitByProduct.get(k) / totalCaByProduct.get(k));
        });
        return avgRentaByProduct;
    }

    public Map<String, Long> getMinCaByProduct(LocalDate start, LocalDate end) {
        Collection<SalesEntity> sales = salesRepository.findByDateBetween(start, end);
        return groupBy(sales, SalesEntity::getProfit, Long.class, SalesEntity::getProduct, Math::min);
    }

    public Map<String, Long> getMaxCaByProduct(LocalDate start, LocalDate end) {
        Collection<SalesEntity> sales = salesRepository.findByDateBetween(start, end);
        return groupBy(sales, SalesEntity::getProfit, Long.class, SalesEntity::getProduct, Math::max);
    }

    public Map<String, Float> getMinRentaByProduct(LocalDate start, LocalDate end) {
        Collection<SalesEntity> sales = salesRepository.findByDateBetween(start, end);
        return groupBy(sales, SalesEntity::getRentability, Float.class, SalesEntity::getProduct, Math::min);
    }

    public Map<String, Float> getMaxRentaByProduct(LocalDate start, LocalDate end) {
        Collection<SalesEntity> sales = salesRepository.findByDateBetween(start, end);
        return groupBy(sales, SalesEntity::getRentability, Float.class, SalesEntity::getProduct, Math::max);
    }

    public Collection<SalesEntity> bestSales(LocalDate start, LocalDate end) {
        return salesRepository.getBestSales(start, end).subList(0, 3);
    }

    public Collection<SalesEntity> worstSales(LocalDate start, LocalDate end) {
        return salesRepository.getWorstSales(start, end).subList(0, 3);
    }

}
