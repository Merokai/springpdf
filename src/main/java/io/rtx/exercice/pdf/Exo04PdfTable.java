package io.rtx.exercice.pdf;

import org.springframework.stereotype.Component;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

@Component
public class Exo04PdfTable extends AbstractPdfExercice {

	private static Color GREY = new DeviceRgb(150, 150, 150);

	private static Table basicTable() {
		Table table = new Table(10);

		// Header
		for (int i = 0; i < 10; i++) {
			if (i != 0) {
				table.addCell(Integer.toString(i));
			} else {
				table.addCell("X");
			}
		}

		for (int i = 1; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (j == 0) {
					table.addCell(Integer.toString(i));
				} else {
					String value = Integer.toString(i * j);
					table.addCell(value);
				}
			}
		}

		return table;
	}

	private static Table niceTable() {

		UnitValue[] columns = UnitValue.createPercentArray(10);
		Table table = new Table(columns);
		table.setWidth(300);

		table.addCell("X");
		for (int i = 1; i < 10; i++) {
			table.addCell(Integer.toString(i));
		}

		for (int i = 1; i < 10; i++) {
			table.addCell(Integer.toString(i));
			for (int j = 1; j < 10; j++) {
				int value = i * j;

				Cell cell = new Cell();

				Paragraph p = new Paragraph(Integer.toString(value));
				cell.add(p);
				Color backgroundColor = new DeviceRgb(i * 10 + 155, 255, j * 10 + 155);
				cell.setBackgroundColor(backgroundColor);

				table.addCell(cell);
			}
		}

		return table;
	}

	@Override
	protected void buildDocument(Document document) {
		Paragraph p = new Paragraph("Multiplication");
		p.setBold();
		p.setTextAlignment(TextAlignment.CENTER);
		document.add(p);

		document.add(new Paragraph("Basic Table"));

		Table table = basicTable();
		document.add(table);

		document.add(new Paragraph("Nice Table"));
		table = niceTable();

		table.setTextAlignment(TextAlignment.CENTER); // S'applique au contenu des cellules
		table.setHorizontalAlignment(HorizontalAlignment.CENTER); // S'applique � la table
		document.add(table);

		p = new Paragraph("Create with Java.");
		p.setFontColor(GREY);
		p.setItalic();
		document.add(p);

	}
}
