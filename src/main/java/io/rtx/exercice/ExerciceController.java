package io.rtx.exercice;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.rtx.exercice.chart.Exo07ChartPie;
import io.rtx.exercice.chart.Exo08ChartLines;
import io.rtx.exercice.pdf.AbstractPdfExercice;
import io.rtx.exercice.pdf.Exo01PdfBase;
import io.rtx.exercice.pdf.Exo02PdfParagraph;
import io.rtx.exercice.pdf.Exo03PdfText;
import io.rtx.exercice.pdf.Exo04PdfTable;
import io.rtx.exercice.pdf.Exo05PdfImage;
import io.rtx.exercice.pdf.Exo06PdfFooter;
import io.rtx.exercice.pdf.Exo09PdfChart;

@RestController
public class ExerciceController {

	@Autowired
	private Exo07ChartPie pieChart;
	@Autowired
	private Exo08ChartLines linesChart;
	@Autowired
	private Exo01PdfBase pdf1_Base;
	@Autowired
	private Exo02PdfParagraph pdf2_Paragraph;
	@Autowired
	private Exo03PdfText pdf3_Text;
	@Autowired
	private Exo04PdfTable pdf4_Table;
	@Autowired
	private Exo05PdfImage pdf5_Image;
	@Autowired
	private Exo06PdfFooter pdf6_Footer;
	@Autowired
	private Exo09PdfChart pdf9_Chart;

	@RequestMapping(method = RequestMethod.GET, path = "hello1")
	public String hello1() {
		return "Hello world with generic @RequestMapping";
	}

	@GetMapping("hello2")
	public String hello2() {
		return "Hello world with specific @GetMapping";
	}

	@GetMapping("pfd")
	public void getPdf(HttpServletResponse response, @RequestParam int exo) throws Exception {
		AbstractPdfExercice exercice = null;

		switch (exo) {
		case 1:
			exercice = pdf1_Base;
			break;
		case 2:
			exercice = pdf2_Paragraph;
			break;
		case 3:
			exercice = pdf3_Text;
			break;
		case 4:
			exercice = pdf4_Table;
			break;
		case 5:
			exercice = pdf5_Image;
			break;
		case 6:
			exercice = pdf6_Footer;
			break;
		case 9:
			exercice = pdf9_Chart;
			break;
		}

		if (exercice == null) {
			throw new Exception("invalid exercice id. Valid number are: 1-6 and 9");
		}

		OutputStream output = response.getOutputStream();
		exercice.generateReport(output);
		output.close();
	}

	@GetMapping("chart/pie")
	public String pieChart() throws IOException {
		pieChart.createChart();
		return "Pie chart generated";
	}

	@GetMapping("chart/line")
	public String lineChart(HttpServletResponse response) throws IOException {
		linesChart.createChart();
		return "Lines charts generated";
	}

}
