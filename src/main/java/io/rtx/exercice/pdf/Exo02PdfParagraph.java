package io.rtx.exercice.pdf;

import org.springframework.stereotype.Component;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;

@Component
public class Exo02PdfParagraph extends AbstractPdfExercice {

	private static final String LOREM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit."
			+ " Sed non risus. Suspendisse lectus tortor, dignissim sit amet, adipiscing nec, ultricies sed, dolor."
			+ " Cras elementum ultrices diam. Maecenas ligula massa, varius a, semper congue, euismod non, mi."
			+ " Proin porttitor, orci nec nonummy molestie, enim est eleifend mi, non fermentum diam nisl sit amet erat."
			+ " Duis semper. Duis arcu massa, scelerisque vitae, consequat in, pretium a, enim. Pellentesque congue. Ut in risus volutpat libero pharetra tempor."
			+ " Cras vestibulum bibendum augue. Praesent egestas leo in pede. Praesent blandit odio eu enim. Pellentesque sed dui ut augue blandit sodales."
			+ " Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Aliquam nibh. Mauris ac mauris sed pede pellentesque fermentum."
			+ " Maecenas adipiscing ante non diam sodales hendrerit.";

	@Override
	protected void buildDocument(Document document) {
		Paragraph title = new Paragraph("Hello World !");
		title.setBold();
		title.setTextAlignment(TextAlignment.CENTER);
		document.add(title);

		for (int i = 0; i < 20; i++) {
			Paragraph p = new Paragraph(i + " : " + LOREM);
			p.setTextAlignment(TextAlignment.JUSTIFIED);
			document.add(p);
		}

		Paragraph created = new Paragraph("Create with Java.");
		Color color = new DeviceRgb(150, 150, 150);
		created.setFontColor(color);
		created.setItalic();
		document.add(created);
	}

}
