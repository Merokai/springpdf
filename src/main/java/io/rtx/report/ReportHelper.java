package io.rtx.report;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import io.rtx.sales.SalesEntity;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

@Component
public class ReportHelper {
    private static Color GREEN = new DeviceRgb(100, 150, 100);
    private static Color LGREEN = new DeviceRgb(100, 220, 100);
    private static Color YELLOW = new DeviceRgb(220, 220, 0);
    private static Color ORANGE = new DeviceRgb(220, 100, 0);
    private static Color RED = new DeviceRgb(220, 100, 100);
    private static Color BLUE = new DeviceRgb(100, 100, 220);
    private static Color LBLUE = new DeviceRgb(155, 200, 255);
    private static Color GREY = new DeviceRgb(200, 200, 200);


    // Returns a formatted table from a collection of sales
    Div salesDetails(Collection<SalesEntity> sales) {
        Div div = new Div();
        div.add(
                new Paragraph("Tableau des ventes")
                        .setBold()
                        .setItalic()
                        .setTextAlignment(TextAlignment.LEFT)
        );
        // new table
        Table table = new Table(UnitValue.createPercentArray(6))
                .setTextAlignment(TextAlignment.CENTER) // Center cell content
                .addHeaderCell(
                        new HeaderCell("Date"))
                .addHeaderCell(
                        new HeaderCell("Pays"))
                .addHeaderCell(
                        new HeaderCell("Produit"))
                .addHeaderCell(
                        new HeaderCell("Valeur"))
                .addHeaderCell(
                        new HeaderCell("Bénéfice"))
                .addHeaderCell(
                        new HeaderCell("Rentabilité"));

        sales.forEach(s -> {
            table.addCell(new CustomCell(s.getDate()));
            table.addCell(new CustomCell(s.getCountry()));
            table.addCell(new CustomCell(s.getProduct()));
            table.addCell(new CustomCell(s.getValue()));
            table.addCell(new CustomCell(s.getProfit()));
            table.addCell(new RentabilityCell(s.getRentability()));
        });

        table.setHorizontalAlignment(HorizontalAlignment.CENTER);
        table.setWidth(500);

        div.add(table);
        return div;
    }

    Div aggregatedSales(Collection<SalesEntity> totalSales) {

        long totalValue = totalSales.stream().map(SalesEntity::getValue).reduce(0L, Long::sum);
        long totalProfits = totalSales.stream().map(SalesEntity::getProfit).reduce(0L, Long::sum);

        return new Div()
                .add(
                        new Paragraph("Données agrégées")
                                .setBold()
                                .setItalic()
                                .setTextAlignment(TextAlignment.LEFT)
                )
                .add(
                        new Paragraph()
                                .setMarginLeft(36)
                                .add(
                                        new Paragraph()
                                                .setWidth(200)
                                                .add(
                                                        new Text("Nombre de ventes :")
                                                )
                                                .add("\n")
                                                .add(
                                                        new Text("Total chiffre d'affaire (CA) :")
                                                )
                                                .add("\n")
                                                .add(
                                                        new Text("Total Bénéfice :")
                                                )
                                                .add("\n")
                                                .add(
                                                        new Text("Rentabilité globale :")
                                                )
                                                .add("\n")
                                )
                                .add(
                                        new Paragraph().setTextAlignment(TextAlignment.RIGHT)
                                                .add(
                                                        new Text(String.format(Locale.FRANCE, "%,d", totalSales.size()))
                                                )
                                                .add("\n")
                                                .add(
                                                        new Text(String.format(Locale.FRANCE, "%,d", totalValue))
                                                )
                                                .add("\n")
                                                .add(
                                                        new Text(String.format(Locale.FRANCE, "%,d", totalProfits))
                                                )
                                                .add("\n")
                                                .add(
                                                        new Text(String.format(Locale.FRANCE, "%.1f %%", (float) 100 * totalProfits / totalValue))
                                                )
                                                .add("\n")
                                )
                );
    }

    Div aggregatedSalesByCountry(Collection<SalesEntity> totalSales, Map<String, Long> caByCountry, Map<String, Long> profitsByCountry) {

        long totalValue = totalSales.stream().map(SalesEntity::getValue).reduce(0L, Long::sum);
        long totalProfits = totalSales.stream().map(SalesEntity::getProfit).reduce(0L, Long::sum);

        Div div = new Div();
        Table table = new Table(UnitValue.createPercentArray(2 + caByCountry.size()));
        table
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setTextAlignment(TextAlignment.CENTER)

                .addHeaderCell(new Cell().setBorder(Border.NO_BORDER))
                .addHeaderCell(new HeaderCell("Total").setBackgroundColor(GREY));

        caByCountry.keySet().stream().sorted().forEach(k ->
                table.addHeaderCell(new HeaderCell(k))
        );

        table.addCell("CA");
        table.addCell(new CustomCell(totalValue).setBackgroundColor(GREY));
        caByCountry.keySet().stream().sorted().forEach(k ->
                table.addCell(new CustomCell((float) 100 * caByCountry.get(k) / totalValue))
        );
        table.addCell("Bénéfice");
        table.addCell(new CustomCell(totalProfits).setBackgroundColor(GREY));
        profitsByCountry.keySet().stream().sorted().forEach(k ->
                table.addCell(new CustomCell((float) 100 * profitsByCountry.get(k) / totalProfits))
        );
        table.addCell("Rentabilité");
        table.addCell(new CustomCell((float) 100 * totalProfits / totalValue).setBackgroundColor(GREY));
        profitsByCountry.keySet().stream().sorted().forEach(k ->
                table.addCell(new CustomCell((float) 100 * profitsByCountry.get(k) / caByCountry.get(k)))
        );


        div
                .add(
                        new Paragraph("Données par pays")
                                .setBold()
                                .setItalic()
                                .setTextAlignment(TextAlignment.LEFT)
                )
                .add(table)
                .add(new Paragraph("\n"));
        return div;
    }

    public <K extends Comparable, T extends Number> Image renderBarChart(Map<K, T> data, String title, String xLabel, String yLabel) throws IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        // Need to specify the Map as TreeMap in order to get keys sorted
        TreeMap<K, T> sorted = new TreeMap<>();
        sorted.putAll(data);
        sorted.forEach((k, v) -> dataset.addValue(v, title, k));
        JFreeChart chart = customBarChart(title, xLabel, yLabel, dataset);
        return chartToImage(chart, 640, 480);
    }

    public <K extends Comparable, T extends Number> Image renderLineChart(Map<K, T> data, String title, String xLabel, String yLabel) throws IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        // Need to specify the Map as TreeMap in order to get keys sorted
        TreeMap<K, T> sorted = new TreeMap<>();
        sorted.putAll(data);
        sorted.forEach((k, v) -> dataset.addValue(v, title, k));
        JFreeChart chart = customLineChart(title, xLabel, yLabel, dataset);
        return chartToImage(chart, 640, 480);
    }

    public <K extends Comparable, T extends Number> Image renderPieChart(Map<K, T> data, String title) throws IOException {
        DefaultPieDataset dataset = new DefaultPieDataset();
        data.forEach(dataset::setValue);

        JFreeChart chart = customPieChart(title, dataset);
        return chartToImage(chart, 640, 480);
    }


    private static Image chartToImage(JFreeChart chart, int width, int height) throws IOException {
        BufferedImage buffImage = chart.createBufferedImage(width, height);
        byte[] bytes = ChartUtils.encodeAsPNG(buffImage);
        ImageData imageData = ImageDataFactory.create(bytes);
        Image image = new Image(imageData);
        return image;
    }

    private static JFreeChart customPieChart(String title, DefaultPieDataset dataset) {
        JFreeChart chart = ChartFactory.createPieChart(title, dataset);

        chart.setBackgroundPaint(new java.awt.Color(255, 255, 255));

        return chart;
    }

    private static JFreeChart customBarChart(String title, String xLabel, String yLabel, CategoryDataset dataset) {
        JFreeChart chart = ChartFactory.createBarChart(title, xLabel, yLabel, dataset);

        CategoryPlot plot = (CategoryPlot) chart.getPlot();

        plot.setBackgroundPaint(new java.awt.Color(255, 255, 255));
        chart.setBackgroundPaint(new java.awt.Color(255, 255, 255));

        return chart;
    }

    private static JFreeChart customLineChart(String title, String xLabel, String yLabel, CategoryDataset dataset) {
        JFreeChart chart = ChartFactory.createLineChart(title, xLabel, yLabel, dataset);

        CategoryPlot plot = (CategoryPlot) chart.getPlot();

        plot.setBackgroundPaint(new java.awt.Color(255, 255, 255));
        chart.setBackgroundPaint(new java.awt.Color(255, 255, 255));

        return chart;
    }

    public Div productsInformations(Map<String, Long> productsAvgValue, Map<String, Float> productsAvgRenta, Map<String, Float> productsMinRenta, Map<String, Float> productsMaxRenta) {
        Div div = new Div();

        div.add(
                new Paragraph("Synthèse d'information par produit")
                        .setBold()
                        .setItalic()
                        .setTextAlignment(TextAlignment.LEFT)
        );
        Table table = new Table(UnitValue.createPercentArray(5));
        table
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setTextAlignment(TextAlignment.CENTER)

                .addHeaderCell(new Cell().setBorder(Border.NO_BORDER))
                .addHeaderCell(new HeaderCell("Valeur Moyenne"))
                .addHeaderCell(new HeaderCell("Rentabilité Moyenne"))
                .addHeaderCell(new HeaderCell("Rentabilité Mini"))
                .addHeaderCell(new HeaderCell("Rentabilité Maxi"));

        productsAvgValue.keySet().stream().sorted().forEach(p -> {
            table.addCell(new CustomCell(p));
            table.addCell(new CustomCell(productsAvgValue.get(p)));
            table.addCell(new CustomCell(productsAvgRenta.get(p)));
            table.addCell(new CustomCell(productsMinRenta.get(p)));
            table.addCell(new CustomCell(productsMaxRenta.get(p)));
        });

        div.add(table);
        return div;
    }

    // Custom cells for table rendering
    class CustomCell extends Cell {
        CustomCell(float value) {
            super();
            add(
                    new Paragraph(
                            String.format("%.1f %%", value)
                    )
            );
            setTextAlignment(TextAlignment.CENTER);
        }

        CustomCell(long value) {
            super();
            add(
                    new Paragraph(
                            String.format("%,d", value)
                    )
            );
            if (value < 0) setFontColor(RED);
            setTextAlignment(TextAlignment.RIGHT);
        }

        CustomCell(String value) {
            super();
            add(
                    new Paragraph(
                            value
                    )
            );
            setTextAlignment(TextAlignment.CENTER);
        }

        CustomCell(LocalDate value) {
            super();
            add(
                    new Paragraph(
                            String.format("%tD", value)
                    )
            );
            setTextAlignment(TextAlignment.CENTER);
        }
    }

    class RentabilityCell extends CustomCell {
        RentabilityCell(float value) {
            super(value);
            setBackgroundColor(
                    value >= 10 ? GREEN :
                            value >= 5 ? LGREEN :
                                    value >= 2 ? YELLOW :
                                            value >= 0 ? ORANGE :
                                                    RED
            );
        }
    }

    class HeaderCell extends CustomCell {
        HeaderCell(String value) {
            super(value);
            setBackgroundColor(LBLUE);
            setBold();
        }
    }
}
