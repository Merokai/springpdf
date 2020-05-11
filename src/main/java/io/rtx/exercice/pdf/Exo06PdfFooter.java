package io.rtx.exercice.pdf;

import org.springframework.stereotype.Component;

import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Paragraph;

@Component
public class Exo06PdfFooter extends AbstractPdfExercice {

	public static final int LEFT_MARGIN = 36;
	public static final int RIGHT_MARGIN = 36;
	public static final int TOP_MARGIN = 36;
	public static final int BOTTOM_MARGIN = 36;
	public static final int FOOTER_HEIGHT = 72;

	@Override
	protected void buildDocument(Document document) throws Exception {
		PdfDocument pdf = document.getPdfDocument();
		pdf.addEventHandler(PdfDocumentEvent.END_PAGE, new FooterHandler());

		document.setMargins(TOP_MARGIN, RIGHT_MARGIN, BOTTOM_MARGIN + FOOTER_HEIGHT, LEFT_MARGIN);

		document.add(new Paragraph("Footer - First section"));
		document.add(new AreaBreak());
		document.add(new Paragraph("Footer - Second Section"));
		document.add(new AreaBreak());
		document.add(new Paragraph("Footer - Third Section"));
	}
}
