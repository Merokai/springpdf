package io.rtx.sales;

import lombok.Data;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class SalesEntity {

	private String country;

	private LocalDate date;

	@Id
	@GeneratedValue
	private Long id;

	private String product;

	private long profit;

	private long value;

	public float getRentability(){
		return (float) (profit * 100) / value;
	}
}
