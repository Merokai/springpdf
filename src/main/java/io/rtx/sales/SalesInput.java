package io.rtx.sales;


import javax.validation.constraints.*;
import java.time.LocalDate;

public class SalesInput {

    @NotBlank
    @Size(min = 2, max = 2)
	@Pattern(regexp = "^[A-Z]{2}$")
    private String country;

    @Past
    private LocalDate date;

    @NotBlank
    @Size(min = 7, max = 7)
	@Pattern(regexp = "^[A-Z]{5}[0-9]{2}$")
    private String product;

    private long profit;

    @Min(0)
    private long value;

    public String getCountry() {
        return country;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getProduct() {
        return product;
    }

    public long getProfit() {
        return profit;
    }

    public long getValue() {
        return value;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public void setProfit(long profit) {
        this.profit = profit;
    }

    public void setValue(long value) {
        this.value = value;
    }

}
