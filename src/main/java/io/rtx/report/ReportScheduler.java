package io.rtx.report;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ReportScheduler {

	@Autowired
	ReportService reportService;

	private static final long SECONDS_30 = 30 * 1000;

	// @Scheduled(fixedDelay = 5000) // 5.000 millisecond = 5s
	public void sayHello1() {
		System.out.println("Hello  5s : " + LocalTime.now());
	}

	// @Scheduled(fixedDelay = SECONDS_30)
	public void sayHello2() {
		System.out.println("Hello 30s : " + LocalTime.now());
	}

	// 1st day of month 00:00
	@Scheduled(cron = "0 0 0 1 * ?")
	public void monthlyReport() throws FileNotFoundException {
		reportService.generateActivityReport(new FileOutputStream(String.format("%d-%d-report.pdf", LocalDate.now().getYear(), LocalDate.now().getMonth().getValue())));
	}
}
