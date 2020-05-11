package io.rtx.report;

import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import io.rtx.sales.SalesEntity;
import io.rtx.sales.SalesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;

@Service
public class ReportService {

    @Autowired
    private SalesRepository salesRepository;

    @Autowired
    private ReportHelper reportHelper;

    @Autowired
    private StatisticService statisticService;

    public void generateReport(OutputStream output) {
        PdfDocument pdf = new PdfDocument(new PdfWriter(output));
        Document document = new Document(pdf);

        document.add(new Paragraph("Hello World !"));
        document.add(new Paragraph(" Creation date: " + LocalDateTime.now().toString()));
        document.add(new Paragraph("Create with Java."));

        document.close();
    }

    public void generateActivityReport(OutputStream output, LocalDate start, LocalDate end) {
        start = start != null ? start : LocalDate.now().minusMonths(1);
        end = end != null ? end : LocalDate.now();
        List<SalesEntity> sales = salesRepository.findByDateBetween(start, end);
        Map<String, Long> caByCountry = statisticService.getCaByCountry(start, end);
        Map<String, Long> profitsByCountry = statisticService.getProfitsByCountry(start, end);


        sales.sort(Comparator.comparing(SalesEntity::getDate));

        PdfDocument pdf = new PdfDocument(new PdfWriter(output));
        Document document = new Document(pdf);

        // Title
        document.add(
                new Paragraph("Rapport d'activité")
                        .setBold()
                        .setTextAlignment(TextAlignment.CENTER)
        )

                // Dates
                .add(
                        new Paragraph(
                                "Du " + start.format(DateTimeFormatter.ISO_LOCAL_DATE)
                                        + " au " + end.format(DateTimeFormatter.ISO_LOCAL_DATE))
                                .setTextAlignment(TextAlignment.JUSTIFIED)
                )

                .add(new Paragraph())
                .add(new LineSeparator(new SolidLine(1)))

                // Données agrégées
                .add(reportHelper.aggregatedSales(sales))

                .add(new Paragraph())
                .add(new LineSeparator(new SolidLine(1)))

                // Par pays
                .add(reportHelper.aggregatedSalesByCountry(sales, caByCountry, profitsByCountry))

                .add(new Paragraph())
                .add(new LineSeparator(new SolidLine(1)))

                // Table
                .add(reportHelper.salesDetails(sales))


                .close();
    }

    public void generateActivityReport(OutputStream output) {

        LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);

        int year = oneMonthAgo.getYear();
        boolean isLeap = oneMonthAgo.isLeapYear();
        Month month = oneMonthAgo.getMonth();

        int lastDay = month.length(isLeap);

        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = LocalDate.of(year, month, lastDay);

        generateActivityReport(output, start, end);
    }

    public void generateAnalysisReport(OutputStream output, int year) throws IOException {
        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end = LocalDate.of(year, 12, 31);


        PdfDocument pdf = new PdfDocument(new PdfWriter(output));
        pdf.addEventHandler(PdfDocumentEvent.START_PAGE, event -> {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfPage page = docEvent.getPage();
            PdfDocument pdfDoc = docEvent.getDocument();

            PdfCanvas pdfCanvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);

            float x = 36;
            float y = page.getPageSize().getHeight() - 36;
            float w = page.getPageSize().getWidth() - 72;
            float h = 36;

            Rectangle rectangle = new Rectangle(x, y, w, h);

            Canvas canvas = new Canvas(pdfCanvas, pdfDoc, rectangle);
            Div header = new Div();
            Paragraph pTitle = new Paragraph();
            pTitle.setWidth(w);

            try {
                ImageData imageData = ImageDataFactory.create("ipi.jpg");
                Image image = new Image(imageData);
                image.setWidth(72);
                image.setHorizontalAlignment(HorizontalAlignment.LEFT);
                pTitle.add(image.setHorizontalAlignment(HorizontalAlignment.LEFT));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            pTitle.add(new Paragraph("Rapport annuel").setBold().setHorizontalAlignment(HorizontalAlignment.CENTER));
            pTitle.add(new Paragraph(String.format("%d", start.getYear())).setHorizontalAlignment(HorizontalAlignment.RIGHT));
            header.add(pTitle);
            canvas.add(header);

            canvas.close();
        });
        pdf.addEventHandler(PdfDocumentEvent.END_PAGE, event -> {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfPage page = docEvent.getPage();
            PdfDocument pdfDoc = docEvent.getDocument();
            int pageNumber = pdfDoc.getPageNumber(page);

            PdfCanvas pdfCanvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);

            float x = 36;
            float y = 36;
            float w = page.getPageSize().getWidth() - 72;
            float h = 36;

            Rectangle rectangle = new Rectangle(x, y, w, h);

            Canvas canvas = new Canvas(pdfCanvas, pdfDoc, rectangle);

            Paragraph p = new Paragraph("Page " + pageNumber);
            p.setTextAlignment(TextAlignment.CENTER);
            canvas.add(p);
            canvas.close();

        });

        Document document = new Document(pdf);
        document.setMargins(72, 36, 72, 36);


        Image caOverMonthsImage = reportHelper.renderLineChart(statisticService.getCaOverMonths(start, end), "CA over months", "Month", "CA");
        document.add(caOverMonthsImage);

        Image profitsOverMonthsImage = reportHelper.renderLineChart(statisticService.getProfitsOverMonths(start, end), "Profits over months", "Month", "Profits");
        document.add(profitsOverMonthsImage);

        Image caByCountryImage = reportHelper.renderPieChart(statisticService.getCaByCountry(start, end), "CA by country");
        document.add(caByCountryImage);

        Image profitsByCountryImage = reportHelper.renderBarChart(statisticService.getProfitsByCountry(start, end), "Profits by country", "Country", "Profits");
        document.add(profitsByCountryImage);

        Image caByProductImage = reportHelper.renderPieChart(statisticService.getCaByProduct(start, end), "CA by product");
        document.add(caByProductImage);

        Map<String, Long> productsAvgValue = statisticService.getAvgCaByProduct(start, end);
        Map<String, Float> productsAvgRenta = statisticService.getAvgRentaByProduct(start, end);
        Map<String, Float> productsMinRenta = statisticService.getMinRentaByProduct(start, end);
        Map<String, Float> productsMaxRenta = statisticService.getMaxRentaByProduct(start, end);

        Div productsInformations = reportHelper.productsInformations(productsAvgValue, productsAvgRenta, productsMinRenta, productsMaxRenta);
        document.add(productsInformations);

        Collection<SalesEntity> bestSales = statisticService.bestSales(start, end);
        Collection<SalesEntity> worstSales = statisticService.worstSales(start, end);

        document.add(new Paragraph("Meilleures ventes").setBold());

        com.itextpdf.layout.element.List bestSalesList = new com.itextpdf.layout.element.List();
        bestSales.forEach(s -> bestSalesList.add(String.format("Vente n°%d pour un profit de %,d", s.getId(), s.getProfit())));
        document.add(bestSalesList);


        document.add(new Paragraph("Pires ventes").setBold());

        com.itextpdf.layout.element.List worstSalesList = new com.itextpdf.layout.element.List();
        worstSales.forEach(s -> worstSalesList.add(String.format("Vente n°%d pour un profit de %,d", s.getId(), s.getProfit())));
        document.add(worstSalesList);

        document.close();
    }

    public void generateAnalysisReport(OutputStream output) throws IOException {
        generateAnalysisReport(output, LocalDate.now().minusYears(1).getYear());
    }
}
