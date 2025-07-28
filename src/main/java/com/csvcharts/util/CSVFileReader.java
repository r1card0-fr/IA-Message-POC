package com.csvcharts.util;

import com.csvcharts.model.CSVData;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utilitário para leitura de arquivos CSV
 */
public class CSVFileReader {
    private static final Logger logger = LoggerFactory.getLogger(CSVFileReader.class);

    /**
     * Lê um arquivo CSV e retorna os dados estruturados
     * 
     * @param filePath Caminho do arquivo CSV
     * @return Objeto CSVData com os dados do arquivo
     * @throws IOException Se houver erro na leitura do arquivo
     * @throws CsvException Se houver erro no parsing do CSV
     */
    public CSVData readCSV(String filePath) throws IOException, CsvException {
        Path path = Paths.get(filePath);
        
        if (!Files.exists(path)) {
            throw new IOException("Arquivo não encontrado: " + filePath);
        }

        logger.info("Lendo arquivo CSV: {}", filePath);

        try (com.opencsv.CSVReader reader = new com.opencsv.CSVReader(new FileReader(filePath))) {
            List<String[]> allRows = reader.readAll();
            
            if (allRows.isEmpty()) {
                throw new IOException("Arquivo CSV está vazio");
            }

            // Primeira linha são os cabeçalhos
            String[] headers = allRows.get(0);
            List<String> headerList = new ArrayList<>();
            for (String header : headers) {
                headerList.add(header.trim());
            }

            // Restante das linhas são os dados
            List<Map<String, String>> dataRows = new ArrayList<>();
            for (int i = 1; i < allRows.size(); i++) {
                String[] row = allRows.get(i);
                Map<String, String> rowMap = new HashMap<>();
                
                for (int j = 0; j < Math.min(headers.length, row.length); j++) {
                    rowMap.put(headers[j].trim(), row[j] != null ? row[j].trim() : "");
                }
                
                dataRows.add(rowMap);
            }

            CSVData csvData = new CSVData(headerList, dataRows);
            logger.info("CSV lido com sucesso: {} linhas de dados", dataRows.size());
            
            return csvData;

        } catch (IOException e) {
            logger.error("Erro ao ler arquivo CSV: {}", e.getMessage());
            throw e;
        } catch (CsvException e) {
            logger.error("Erro ao fazer parse do CSV: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Valida se um arquivo é um CSV válido
     * 
     * @param filePath Caminho do arquivo
     * @return true se o arquivo é um CSV válido
     */
    public boolean isValidCSV(String filePath) {
        try {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                return false;
            }

            String fileName = path.getFileName().toString().toLowerCase();
            return fileName.endsWith(".csv");

        } catch (Exception e) {
            logger.error("Erro ao validar arquivo CSV: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Obtém informações básicas sobre o arquivo CSV
     * 
     * @param filePath Caminho do arquivo
     * @return String com informações do arquivo
     */
    public String getFileInfo(String filePath) {
        try {
            CSVData csvData = readCSV(filePath);
            return String.format("Arquivo: %s\nLinhas: %d\nColunas: %d\nColunas: %s", 
                               filePath, 
                               csvData.getRowCount(), 
                               csvData.getColumnCount(),
                               String.join(", ", csvData.getHeaders()));
        } catch (Exception e) {
            return "Erro ao ler arquivo: " + e.getMessage();
        }
    }
} 