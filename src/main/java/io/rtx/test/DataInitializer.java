package io.rtx.test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.rtx.sales.SalesEntity;
import io.rtx.sales.SalesRepository;

@RestController
public class DataInitializer {

	private static final String BE = "BE";
	private static final String IT = "IT";
	private static final String FR = "FR";
	private static final String US = "US";
	private static final String SMBCA42 = "SMBCA42";
	private static final String XKCDA42 = "XKCDA42";
	private static final String VIDMV46 = "VIDMV46";
	private static final String RVMAT06 = "RVMAT06";
	private static final String SLOPU12 = "SLOPU12";
	private static final String DEPML25 = "DEPML25";
	@Autowired
	private SalesRepository repo;

	@GetMapping("api/test/initialize-data")
	public String init() {
		List<SalesEntity> sales = buildData();
		long count = repo.count();
		repo.deleteAll();

		repo.saveAll(sales);

		return "Delete " + count + " then Add " + sales.size() + " Sales in DB";
	}

	private List<SalesEntity> buildData() {
		List<SalesEntity> result = new ArrayList<SalesEntity>();

		add(result, "2019-01-08", US, DEPML25, 1154, 120);
		add(result, "2019-01-15", US, SLOPU12, 2678, -160);
		add(result, "2019-01-19", FR, RVMAT06, 1567, 300);
		add(result, "2019-01-22", IT, DEPML25, 4050, 250);
		add(result, "2019-01-27", US, VIDMV46, 8600, 360);
		add(result, "2019-01-30", BE, XKCDA42, 3500, 250);
		add(result, "2019-02-02", IT, SMBCA42, 6500, 480);
		add(result, "2019-02-15", FR, DEPML25, 2900, 250);
		add(result, "2019-02-19", US, RVMAT06, 5412, -50);
		add(result, "2019-02-20", US, DEPML25, 4980, 520);
		add(result, "2019-03-05", FR, RVMAT06, 1254, 80);
		add(result, "2019-03-15", FR, DEPML25, 2463, -50);
		add(result, "2019-03-20", US, DEPML25, 1245, 76);
		add(result, "2019-04-15", BE, DEPML25, 780, 95);
		add(result, "2019-04-16", IT, RVMAT06, 1460, -60);
		add(result, "2019-04-20", US, DEPML25, 5600, 390);
		add(result, "2019-04-21", US, SLOPU12, 3300, 50);
		add(result, "2019-04-26", US, XKCDA42, 600, 65);
		add(result, "2019-05-09", FR, DEPML25, 6450, 450);
		add(result, "2019-05-12", US, DEPML25, 2300, 190);
		add(result, "2019-05-15", FR, RVMAT06, 1705, 235);
		add(result, "2019-05-16", BE, SLOPU12, 3652, 46);
		add(result, "2019-06-01", BE, DEPML25, 1050, 76);
		add(result, "2019-06-08", BE, RVMAT06, 700, -43);
		add(result, "2019-06-12", US, VIDMV46, 15600, 1300);
		add(result, "2019-06-15", IT, VIDMV46, 12490, 1600);
		add(result, "2019-06-18", US, XKCDA42, 4325, 397);
		add(result, "2019-07-02", FR, RVMAT06, 5530, 600);
		add(result, "2019-07-19", US, SLOPU12, 900, 16);
		add(result, "2019-07-21", US, DEPML25, 9500, 1020);
		add(result, "2019-08-07", FR, DEPML25, 1685, -56);
		add(result, "2019-08-19", IT, RVMAT06, 2560, -86);
		add(result, "2019-08-21", FR, SLOPU12, 7890, 20);
		add(result, "2019-09-21", US, XKCDA42, 4520, 406);
		add(result, "2019-09-25", US, DEPML25, 5600, 450);
		add(result, "2019-09-27", US, DEPML25, 8950, 120);
		add(result, "2019-09-30", IT, VIDMV46, 10564, 800);
		add(result, "2019-09-30", FR, RVMAT06, 1265, 70);
		add(result, "2019-10-16", US, SLOPU12, 1895, 170);
		add(result, "2019-10-19", BE, SMBCA42, 2560, 300);
		add(result, "2019-10-21", IT, DEPML25, 4582, 289);
		add(result, "2019-10-25", US, SLOPU12, 1452, 953);
		add(result, "2019-11-02", US, RVMAT06, 4523, 370);
		add(result, "2019-11-05", BE, DEPML25, 2560, 420);
		add(result, "2019-11-09", FR, SLOPU12, 1245, -50);
		add(result, "2019-11-12", US, XKCDA42, 5600, 436);
		add(result, "2019-11-24", US, DEPML25, 1054, 98);
		add(result, "2019-12-06", FR, RVMAT06, 5320, 310);
		add(result, "2019-12-08", IT, SLOPU12, 2003, -5);
		add(result, "2019-12-12", US, DEPML25, 3102, 50);

		return result;
	}

	private void add(List<SalesEntity> result, String date, String country, String product, long value, long profit) {
		SalesEntity entity = new SalesEntity();
		entity.setCountry(country);
		LocalDate parsedDate = LocalDate.parse(date);
		entity.setDate(parsedDate);
		entity.setProduct(product);
		entity.setValue(value);
		entity.setProfit(profit);
		result.add(entity);
	}
}
