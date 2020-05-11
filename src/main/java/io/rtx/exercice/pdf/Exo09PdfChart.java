package io.rtx.exercice.pdf;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.stereotype.Component;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.HorizontalAlignment;

@Component
public class Exo09PdfChart extends AbstractPdfExercice {

	private static Image chartToImage(JFreeChart chart, int width, int height) throws IOException {
		BufferedImage buffImage = chart.createBufferedImage(width, height);
		byte[] bytes = ChartUtils.encodeAsPNG(buffImage);
		ImageData imageData = ImageDataFactory.create(bytes);
		Image image = new Image(imageData);
		return image;
	}

	private static CategoryDataset createDataSet() {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		dataset.addValue(1200, "Nord", "2017");
		dataset.addValue(1300, "Nord", "2018");
		dataset.addValue(1800, "Nord", "2019");
		dataset.addValue(1600, "Nord", "2020");
		dataset.addValue(1700, "Sud", "2017");
		dataset.addValue(1500, "Sud", "2018");
		dataset.addValue(1550, "Sud", "2019");
		dataset.addValue(1400, "Sud", "2020");
		return dataset;
	}

	@Override
	protected void buildDocument(Document document) throws Exception {
		document.add(new Paragraph("Pdf + Graph"));

		CategoryDataset dataset = createDataSet();
		JFreeChart chart = ChartFactory.createLineChart("�volution Ventes", "temps", "Chiffre affaire", dataset);
		Image image = chartToImage(chart, 640, 480);
		image.setWidth(500);
		image.setHorizontalAlignment(HorizontalAlignment.RIGHT);
		document.add(image);

		document.add(new Paragraph("Create with Java."));
	}
}
