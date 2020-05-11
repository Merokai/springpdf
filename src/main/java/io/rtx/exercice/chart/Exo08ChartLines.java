package io.rtx.exercice.chart;

import java.awt.Color;
import java.awt.GradientPaint;
import java.io.File;
import java.io.IOException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.stereotype.Component;

@Component
public class Exo08ChartLines {

	public void createChart() throws IOException {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		dataset.addValue(1200, "Nord", "2017");
		dataset.addValue(1300, "Nord", "2018");
		dataset.addValue(1800, "Nord", "2019");
		dataset.addValue(1600, "Nord", "2020");
		dataset.addValue(1700, "Sud", "2017");
		dataset.addValue(1500, "Sud", "2018");
		dataset.addValue(1550, "Sud", "2019");
		dataset.addValue(1400, "Sud", "2020");

		JFreeChart chart = basicChart(dataset);
		saveChart(chart, "LineChart.jpeg");

		JFreeChart niceChart = customChart(dataset);
		saveChart(niceChart, "LineChart2.jpeg");
	}

	private static void saveChart(JFreeChart chart, String fileName) throws IOException {
		int width = 640; /* Width of the image */
		int height = 480; /* Height of the image */
		File pieChart = new File(fileName);
		ChartUtils.saveChartAsJPEG(pieChart, chart, width, height);

		System.out.println("Chart created: " + fileName);
	}

	private static JFreeChart basicChart(CategoryDataset dataset) {
		JFreeChart chart = ChartFactory.createLineChart("�volution Ventes", "temps", "Chiffre affaire", dataset);
		return chart;
	}

	private static JFreeChart customChart(CategoryDataset dataset) {
		JFreeChart chart = ChartFactory.createLineChart("�volution Ventes", "temps", "Chiffre affaire", dataset);

		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		NumberAxis axis = (NumberAxis) plot.getRangeAxis();
		axis.setRange(1000, 2000);

		LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();

		// Dessine la premi�re s�rie en Violet
		renderer.setSeriesPaint(0, new Color(255, 0, 255));

		// Dessine la seconde s�rie en Vert
		renderer.setSeriesPaint(1, new Color(0, 255, 0));

		// Fond du graphique (zone de donn�e) en blanc
		plot.setBackgroundPaint(new Color(255, 255, 255));

		// Fond du graphique (donn�e + l�gende) en d�grad�
		GradientPaint gradient = new GradientPaint(0, 0, new Color(200, 200, 200), 0, 100, new Color(100, 100, 100),
				true);
		chart.setBackgroundPaint(gradient);

		return chart;
	}

}
