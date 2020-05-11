package io.rtx.exercice.pdf;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;

public class FooterHandler implements IEventHandler {

	public void handleEvent(Event event) {
		PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
		PdfPage page = docEvent.getPage();
		PdfDocument pdfDoc = docEvent.getDocument();
		int pageNumber = pdfDoc.getPageNumber(page);

		PdfCanvas pdfCanvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);

		float x = Exo06PdfFooter.LEFT_MARGIN;
		float y = Exo06PdfFooter.BOTTOM_MARGIN;
		float w = page.getPageSize().getWidth() - Exo06PdfFooter.LEFT_MARGIN - Exo06PdfFooter.RIGHT_MARGIN;
		float h = Exo06PdfFooter.FOOTER_HEIGHT;

		Rectangle rectangle = new Rectangle(x, y, w, h);

		Canvas canvas = new Canvas(pdfCanvas, pdfDoc, rectangle);

		Paragraph p = new Paragraph("Page " + pageNumber);
		p.setTextAlignment(TextAlignment.CENTER);
		p.setBackgroundColor(new DeviceRgb(200, 255, 200));
		canvas.add(p);

		canvas.close();
	}

}