package io.rtx.exercice.pdf;

import org.springframework.stereotype.Component;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

@Component
public class Exo01PdfBase extends AbstractPdfExercice {

	@Override
	protected void buildDocument(Document document) {
		document.add(new Paragraph("Hello World !"));
		document.add(new Paragraph("Create with Java."));
	}
}
