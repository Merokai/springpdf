package io.rtx.exercice.pdf;

import org.springframework.stereotype.Component;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;

@Component
public class Exo03PdfText extends AbstractPdfExercice {

	private static Color GREY = new DeviceRgb(150, 150, 150);
	private static Color RED = new DeviceRgb(220, 100, 100);
	private static Color BLUE = new DeviceRgb(100, 100, 220);

	@Override
	protected void buildDocument(Document document) {
		Paragraph p = new Paragraph("Carrés");
		p.setBold();
		p.setTextAlignment(TextAlignment.CENTER);
		document.add(p);

		p = new Paragraph();
		for (int i = 1; i <= 20; i++) {
			int value = i * i;
			Text text = new Text(Integer.toString(value));
			if (i % 2 == 0) {
				text.setFontColor(BLUE);
			} else {
				text.setFontColor(RED);
			}
			if (i != 1) {
				p.add(", ");
			}
			p.add(text);
		}
		document.add(p);

		p = new Paragraph("Create with Java.");
		p.setFontColor(GREY);
		p.setItalic();
		document.add(p);

	}
}
