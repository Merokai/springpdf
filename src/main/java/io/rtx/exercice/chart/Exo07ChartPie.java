package io.rtx.exercice.chart;

import java.io.File;
import java.io.IOException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.stereotype.Component;

@Component
public class Exo07ChartPie {

	public void createChart() throws IOException {
		DefaultPieDataset dataset = new DefaultPieDataset();
		dataset.setValue("France", new Double(66));
		dataset.setValue("Allemagne", new Double(81));
		dataset.setValue("Italie", new Double(60));
		dataset.setValue("Espagne", new Double(46));
		dataset.setValue("Pologne", new Double(38));
		
		JFreeChart chart = ChartFactory.createPieChart("Population", // chart title
				dataset, // data
				true, // include legend
				true, false);

		int width = 640; /* Width of the image */
		int height = 480; /* Height of the image */
		File pieChart = new File("PieChart.jpeg");
		ChartUtils.saveChartAsJPEG(pieChart, chart, width, height);
		
		System.out.println("Chart created");
	}

}
