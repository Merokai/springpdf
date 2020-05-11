package io.rtx.sales;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.ApiOperation;

import javax.validation.Valid;

@RestController
@RequestMapping("api")
public class SalesController {

    @Autowired
    private SalesService service;

    @PostMapping("sales")
    @ApiOperation("Add a new Sales")
    public SalesEntity add(@RequestBody @Valid SalesInput sales) {
        return service.add(sales);
    }

    @DeleteMapping("sales/{id}")
    @ApiOperation(value = "Delete an existing Sales by id", notes = "Return 404 if Sales does not exists")
    public ResponseEntity<String> delete(@PathVariable long id) {
        boolean found = service.delete(id);
        if (found) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sales not found: " + id);
        }
    }

    @GetMapping("sales/searchByCountry")
    @ApiOperation("Return all Sales in a given country")
    public List<SalesEntity> findByCountry(@RequestParam String country) {
        return service.findByCountry(country);
    }

    @GetMapping("sales/searchByDate")
    @ApiOperation("Return all Sales between two given dates")
    public List<SalesEntity> findByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {

        if (end == null) {
            end = LocalDate.now();
        }
        return service.findByDate(start, end);
    }

    @GetMapping("sales/{id}")
    @ApiOperation("Return an existing Sales by id")
    public Optional<SalesEntity> getById(@PathVariable long id) {
        return service.findById(id);
    }

    @GetMapping("sales")
    @ApiOperation("Return all Sales")
    public List<SalesEntity> listSales() {
        return service.getAll();
    }

    @GetMapping("country")
    @ApiOperation("Return all Country")
    public List<String> listCountry() {
        return service.listCountry();
    }

    @PutMapping("sales/{id}")
    @ApiOperation("Update an existing Sales by id")
    public Optional<SalesEntity> update(@PathVariable long id, @RequestBody @Valid SalesInput sales) {
        return service.update(id, sales);
    }

    @GetMapping("sales/searchByProduct")
    @ApiOperation("Return all Sales for a given product")
    public List<SalesEntity> findByProduct(@RequestParam String product) {
        return service.findByProduct(product);
    }

    @GetMapping("product")
    @ApiOperation("Return all Product")
    public List<String> listProduct() {
        return service.listProduct();
    }

    @PatchMapping("sales/{id}")
    @ApiOperation("Update an existing Sales by id")
    public Optional<SalesEntity> patch(@PathVariable long id, @RequestBody @Valid SalesPartialInput sales) {
        return service.patch(id, sales);
    }

    @GetMapping("sales/search")
    public List<SalesEntity> search(@RequestParam(required = false) String country, @RequestParam(required = false) String product, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        country = country != null ? country : "%";
        product = product != null ? product : "%";
        start = start != null ? start : LocalDate.now();
        end = end != null ? end : LocalDate.now();

        return service.search(country, product, start, end);
    }

    @GetMapping("sales/search2")
    public List<SalesEntity> search2(@RequestParam(required = false) String country, @RequestParam(required = false) String product, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return service.search2(country, product, start, end);
    }
}
