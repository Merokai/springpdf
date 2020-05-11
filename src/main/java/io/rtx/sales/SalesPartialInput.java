package io.rtx.sales;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class SalesPartialInput {

    @Size(min = 2, max = 2)
    @Pattern(regexp = "^[A-Z]{2}$")
    private String country;

    @Past
    private LocalDate date;

    @Size(min = 7, max = 7)
    @Pattern(regexp = "^[A-Z]{5}[0-9]{2}$")
    private String product;

    private Long profit;

    @Min(0)
    private Long value;

}
