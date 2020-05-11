package io.rtx.exercice.pdf;

import org.springframework.stereotype.Component;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.HorizontalAlignment;

@Component
public class Exo05PdfImage extends AbstractPdfExercice {

	public static final String IMG_PATH = "ipi.jpg";

	@Override
	protected void buildDocument(Document document) throws Exception {
		document.add(new Paragraph("Hello World !"));

		ImageData imageData = ImageDataFactory.create(IMG_PATH);

		Image image = new Image(imageData);

		image.setWidth(200);
		image.setHorizontalAlignment(HorizontalAlignment.RIGHT);

		document.add(image);
		document.add(new Paragraph("Create with Java."));
	}
}
