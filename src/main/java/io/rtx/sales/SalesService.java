package io.rtx.sales;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.transaction.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class SalesService {

    private final SalesRepository repo;

    @Autowired
    public SalesService(SalesRepository repo) {
        this.repo = repo;
    }

    public SalesEntity add(SalesInput sales) {
        SalesEntity entity = new SalesEntity();
        updateEntity(entity, sales);
        return repo.save(entity);
    }

    public boolean delete(long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public List<SalesEntity> findByCountry(String country) {
        return repo.findByCountry(country);
    }

    public List<SalesEntity> findByDate(LocalDate start, LocalDate end) {
        return repo.findByDateBetween(start, end);
    }

    public Optional<SalesEntity> findById(long id) {
        return repo.findById(id);
    }

    public List<SalesEntity> getAll() {
        return repo.findAll();
    }

    public List<String> listCountry() {
        return repo.listCountry();
    }

    public Optional<SalesEntity> update(long id, SalesInput sales) {
        Optional<SalesEntity> opt = repo.findById(id);

        if (opt.isPresent()) {
            SalesEntity entity = opt.get();
            updateEntity(entity, sales);
        }

        return opt;
    }

    private void updateEntity(SalesEntity entity, SalesInput input) {
        entity.setCountry(input.getCountry());
        entity.setDate(input.getDate());
        entity.setProduct(input.getProduct());
        entity.setProfit(input.getProfit());
        entity.setValue(input.getValue());
    }

    public List<SalesEntity> findByProduct(String product) {
        return repo.findByProduct(product);
    }

    public List<String> listProduct() {
        return repo.listProduct();
    }

    private void patchEntity(SalesEntity entity, SalesPartialInput input) {
        if (input.getCountry() != null) entity.setCountry(input.getCountry());
        if (input.getDate() != null) entity.setDate(input.getDate());
        if (input.getProduct() != null) entity.setProduct(input.getProduct());
        if (input.getProfit() != null) entity.setProfit(input.getProfit());
        if (input.getValue() != null) entity.setValue(input.getValue());
    }

    public Optional<SalesEntity> patch(long id, SalesPartialInput sales) {
        Optional<SalesEntity> opt = repo.findById(id);

        if (opt.isPresent()) {
            SalesEntity entity = opt.get();
            patchEntity(entity, sales);
        }

        return opt;
    }

    public List<SalesEntity> search(String country, String product, LocalDate start, LocalDate end) {
        return repo.search(country, product, start, end);
    }

    public List<SalesEntity> search2(String country, String product, LocalDate start, LocalDate end) {

        QSalesEntity sale = QSalesEntity.salesEntity;
        BooleanBuilder where = new BooleanBuilder();
        if (country != null) {
            where.and(sale.country.eq(country));
        }
        if (product != null) {
            where.and(sale.product.eq(product));
        }
        if (start != null) {
            where.and(sale.date.after(start));
        }
        if (end != null) {
            where.and(sale.date.before(end));
        }
        return StreamSupport.stream(repo.findAll(where).spliterator(), false).collect(Collectors.toList());
    }
}
