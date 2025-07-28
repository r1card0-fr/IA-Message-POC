package com.csvcharts.util;

import com.csvcharts.model.CSVData;
import com.csvcharts.model.ChartType;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Gerador de gráficos usando JFreeChart
 */
public class ChartGenerator {
    private static final Logger logger = LoggerFactory.getLogger(ChartGenerator.class);
    
    private static final int DEFAULT_WIDTH = 800;
    private static final int DEFAULT_HEIGHT = 600;

    /**
     * Gera um gráfico baseado nos dados CSV
     * 
     * @param csvData Dados do CSV
     * @param chartType Tipo de gráfico
     * @param xColumn Coluna X
     * @param yColumn Coluna Y
     * @param outputPath Caminho de saída
     */
    public void generateChart(CSVData csvData, ChartType chartType, String xColumn, String yColumn, String outputPath) {
        try {
            logger.info("Gerando gráfico do tipo: {}", chartType);
            
            JFreeChart chart = createChart(csvData, chartType, xColumn, yColumn);
            
            // Salva o gráfico como imagem
            saveChartAsImage(chart, outputPath);
            
            logger.info("Gráfico salvo em: {}", outputPath);
            
        } catch (Exception e) {
            logger.error("Erro ao gerar gráfico: {}", e.getMessage(), e);
            throw new RuntimeException("Falha ao gerar gráfico: " + e.getMessage(), e);
        }
    }

    /**
     * Cria o gráfico baseado no tipo especificado
     */
    private JFreeChart createChart(CSVData csvData, ChartType chartType, String xColumn, String yColumn) {
        switch (chartType) {
            case BAR:
                return createBarChart(csvData, xColumn, yColumn);
            case LINE:
                return createLineChart(csvData, xColumn, yColumn);
            case PIE:
                return createPieChart(csvData, xColumn, yColumn);
            case SCATTER:
                return createScatterChart(csvData, xColumn, yColumn);
            default:
                throw new IllegalArgumentException("Tipo de gráfico não suportado: " + chartType);
        }
    }

    /**
     * Cria gráfico de barras
     */
    private JFreeChart createBarChart(CSVData csvData, String xColumn, String yColumn) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        for (Map<String, String> row : csvData.getRows()) {
            String xValue = row.get(xColumn);
            String yValue = row.get(yColumn);
            
            if (xValue != null && yValue != null && !xValue.isEmpty() && !yValue.isEmpty()) {
                try {
                    double yNumeric = Double.parseDouble(yValue);
                    dataset.addValue(yNumeric, "Valores", xValue);
                } catch (NumberFormatException e) {
                    logger.warn("Valor não numérico ignorado: {}", yValue);
                }
            }
        }

        return ChartFactory.createBarChart(
            "Gráfico de Barras - " + yColumn + " vs " + xColumn,
            xColumn,
            yColumn,
            dataset,
            PlotOrientation.VERTICAL,
            false, true, false
        );
    }

    /**
     * Cria gráfico de linha
     */
    private JFreeChart createLineChart(CSVData csvData, String xColumn, String yColumn) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        for (Map<String, String> row : csvData.getRows()) {
            String xValue = row.get(xColumn);
            String yValue = row.get(yColumn);
            
            if (xValue != null && yValue != null && !xValue.isEmpty() && !yValue.isEmpty()) {
                try {
                    double yNumeric = Double.parseDouble(yValue);
                    dataset.addValue(yNumeric, "Valores", xValue);
                } catch (NumberFormatException e) {
                    logger.warn("Valor não numérico ignorado: {}", yValue);
                }
            }
        }

        return ChartFactory.createLineChart(
            "Gráfico de Linha - " + yColumn + " vs " + xColumn,
            xColumn,
            yColumn,
            dataset,
            PlotOrientation.VERTICAL,
            false, true, false
        );
    }

    /**
     * Cria gráfico de pizza
     */
    private JFreeChart createPieChart(CSVData csvData, String xColumn, String yColumn) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        
        for (Map<String, String> row : csvData.getRows()) {
            String xValue = row.get(xColumn);
            String yValue = row.get(yColumn);
            
            if (xValue != null && yValue != null && !xValue.isEmpty() && !yValue.isEmpty()) {
                try {
                    double yNumeric = Double.parseDouble(yValue);
                    dataset.setValue(xValue, yNumeric);
                } catch (NumberFormatException e) {
                    logger.warn("Valor não numérico ignorado: {}", yValue);
                }
            }
        }

        return ChartFactory.createPieChart(
            "Gráfico de Pizza - " + yColumn + " por " + xColumn,
            dataset,
            true, true, false
        );
    }

    /**
     * Cria gráfico de dispersão
     */
    private JFreeChart createScatterChart(CSVData csvData, String xColumn, String yColumn) {
        DefaultXYDataset dataset = new DefaultXYDataset();
        
        List<Double> xValues = new java.util.ArrayList<>();
        List<Double> yValues = new java.util.ArrayList<>();
        
        for (Map<String, String> row : csvData.getRows()) {
            String xValue = row.get(xColumn);
            String yValue = row.get(yColumn);
            
            if (xValue != null && yValue != null && !xValue.isEmpty() && !yValue.isEmpty()) {
                try {
                    double xNumeric = Double.parseDouble(xValue);
                    double yNumeric = Double.parseDouble(yValue);
                    xValues.add(xNumeric);
                    yValues.add(yNumeric);
                } catch (NumberFormatException e) {
                    logger.warn("Valor não numérico ignorado: x={}, y={}", xValue, yValue);
                }
            }
        }

        if (!xValues.isEmpty()) {
            double[][] data = new double[2][xValues.size()];
            for (int i = 0; i < xValues.size(); i++) {
                data[0][i] = xValues.get(i);
                data[1][i] = yValues.get(i);
            }
            dataset.addSeries("Dados", data);
        }

        JFreeChart chart = ChartFactory.createScatterPlot(
            "Gráfico de Dispersão - " + yColumn + " vs " + xColumn,
            xColumn,
            yColumn,
            dataset,
            PlotOrientation.VERTICAL,
            false, true, false
        );

        // Configura o renderer para mostrar apenas pontos
        XYPlot plot = (XYPlot) chart.getPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(false, true);
        renderer.setSeriesPaint(0, Color.BLUE);
        plot.setRenderer(renderer);

        return chart;
    }

    /**
     * Salva o gráfico como imagem PNG
     */
    private void saveChartAsImage(JFreeChart chart, String outputPath) throws IOException {
        File outputFile = new File(outputPath);
        
        // Cria o diretório se não existir
        File parentDir = outputFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        // Salva como PNG
        ChartUtils.saveChartAsPNG(outputFile, chart, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        
        logger.info("Gráfico salvo como PNG: {}x{}", DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }
} 