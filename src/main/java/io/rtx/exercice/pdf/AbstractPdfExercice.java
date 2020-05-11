package io.rtx.exercice.pdf;

import java.io.OutputStream;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;

public abstract class AbstractPdfExercice {

	public void generateReport(OutputStream output) throws Exception {
		PdfDocument pdf = new PdfDocument(new PdfWriter(output));
		Document document = new Document(pdf);

		buildDocument(document);

		document.close();
	}

	abstract protected void buildDocument(Document document) throws Exception;
}
