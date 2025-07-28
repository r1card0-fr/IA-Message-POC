package com.csvcharts.service;

import com.csvcharts.model.CSVData;
import com.csvcharts.model.ChartType;
import com.csvcharts.util.CSVFileReader;
import com.csvcharts.util.ChartGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Serviço principal que coordena a leitura do CSV e geração de gráficos
 */
public class CSVChartService {
    private static final Logger logger = LoggerFactory.getLogger(CSVChartService.class);
    
    private final CSVFileReader csvReader;
    private final ChartGenerator chartGenerator;

    public CSVChartService() {
        this.csvReader = new CSVFileReader();
        this.chartGenerator = new ChartGenerator();
    }

    /**
     * Gera um gráfico a partir de um arquivo CSV
     * 
     * @param csvFilePath Caminho do arquivo CSV
     * @param chartType Tipo de gráfico a ser gerado
     * @param xColumn Nome da coluna X (opcional)
     * @param yColumn Nome da coluna Y (opcional)
     */
    public void generateChart(String csvFilePath, ChartType chartType, String xColumn, String yColumn) {
        try {
            logger.info("Iniciando processamento do arquivo: {}", csvFilePath);
            
            // Lê o arquivo CSV
            CSVData csvData = csvReader.readCSV(csvFilePath);
            logger.info("CSV lido com sucesso: {} linhas, {} colunas", 
                       csvData.getRowCount(), csvData.getColumnCount());

            // Determina as colunas a serem usadas
            String finalXColumn = determineColumn(csvData, xColumn, 0);
            String finalYColumn = determineColumn(csvData, yColumn, 1);

            logger.info("Usando colunas: X='{}', Y='{}'", finalXColumn, finalYColumn);

            // Gera o gráfico
            String outputPath = generateOutputPath(csvFilePath, chartType);
            chartGenerator.generateChart(csvData, chartType, finalXColumn, finalYColumn, outputPath);
            
            logger.info("Gráfico gerado com sucesso: {}", outputPath);
            System.out.println("Gráfico salvo em: " + outputPath);

        } catch (Exception e) {
            logger.error("Erro ao gerar gráfico: {}", e.getMessage(), e);
            throw new RuntimeException("Falha ao gerar gráfico: " + e.getMessage(), e);
        }
    }

    /**
     * Determina qual coluna usar baseado no parâmetro fornecido ou índice padrão
     */
    private String determineColumn(CSVData csvData, String columnName, int defaultIndex) {
        if (columnName != null && !columnName.trim().isEmpty()) {
            if (csvData.hasColumn(columnName)) {
                return columnName;
            } else {
                logger.warn("Coluna '{}' não encontrada, usando coluna padrão", columnName);
            }
        }
        
        if (defaultIndex < csvData.getHeaders().size()) {
            return csvData.getHeaders().get(defaultIndex);
        }
        
        throw new IllegalArgumentException("Não foi possível determinar a coluna a ser usada");
    }

    /**
     * Gera o caminho de saída para o gráfico
     */
    private String generateOutputPath(String csvFilePath, ChartType chartType) {
        Path csvPath = Paths.get(csvFilePath);
        String fileName = csvPath.getFileName().toString();
        String baseName = fileName.substring(0, fileName.lastIndexOf('.'));
        
        // Se o arquivo está no diretório atual, usar o diretório atual
        Path parentDir = csvPath.getParent();
        if (parentDir == null) {
            parentDir = Paths.get(".");
        }
        
        return parentDir.resolve(baseName + "_" + chartType.name().toLowerCase() + ".png").toString();
    }

    /**
     * Lista as colunas disponíveis no arquivo CSV
     */
    public void listColumns(String csvFilePath) {
        try {
            CSVData csvData = csvReader.readCSV(csvFilePath);
            System.out.println("\nColunas disponíveis no arquivo:");
            for (int i = 0; i < csvData.getHeaders().size(); i++) {
                System.out.printf("%d. %s%n", i + 1, csvData.getHeaders().get(i));
            }
        } catch (Exception e) {
            logger.error("Erro ao listar colunas: {}", e.getMessage());
            throw new RuntimeException("Falha ao listar colunas: " + e.getMessage(), e);
        }
    }
} 