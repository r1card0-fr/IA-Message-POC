package com.csvcharts;

import com.csvcharts.service.CSVChartService;
import com.csvcharts.model.ChartType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Classe principal da aplicação CSV Charts Generator
 */
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("=== CSV Charts Generator ===");
        logger.info("Versão: 1.0.0");
        logger.info("Java: {}", System.getProperty("java.version"));

        if (args.length > 0) {
            // Modo linha de comando
            processCommandLine(args);
        } else {
            // Modo interativo
            runInteractiveMode();
        }
    }

    private static void processCommandLine(String[] args) {
        if (args.length < 2) {
            System.out.println("Uso: java -jar csv-charts.jar <arquivo_csv> <tipo_grafico> [coluna_x] [coluna_y]");
            System.out.println("Tipos de gráfico disponíveis: BAR, LINE, PIE, SCATTER");
            System.exit(1);
        }

        String csvFile = args[0];
        String chartTypeStr = args[1];
        String xColumn = args.length > 2 ? args[2] : null;
        String yColumn = args.length > 3 ? args[3] : null;

        try {
            ChartType chartType = ChartType.valueOf(chartTypeStr.toUpperCase());
            CSVChartService service = new CSVChartService();
            service.generateChart(csvFile, chartType, xColumn, yColumn);
            logger.info("Gráfico gerado com sucesso!");
        } catch (Exception e) {
            logger.error("Erro ao processar arquivo: {}", e.getMessage());
            System.exit(1);
        }
    }

    private static void runInteractiveMode() {
        Scanner scanner = new Scanner(System.in);
        CSVChartService service = new CSVChartService();

        try {
            System.out.println("\n=== Gerador de Gráficos CSV ===");
            System.out.print("Digite o caminho do arquivo CSV: ");
            String csvFile = scanner.nextLine().trim();

            if (csvFile.isEmpty()) {
                System.out.println("Caminho do arquivo não pode estar vazio!");
                return;
            }

            System.out.println("\nTipos de gráfico disponíveis:");
            for (ChartType type : ChartType.values()) {
                System.out.println("- " + type.name());
            }

            System.out.print("Digite o tipo de gráfico: ");
            String chartTypeStr = scanner.nextLine().trim().toUpperCase();

            ChartType chartType;
            try {
                chartType = ChartType.valueOf(chartTypeStr);
            } catch (IllegalArgumentException e) {
                System.out.println("Tipo de gráfico inválido!");
                return;
            }

            System.out.print("Digite o nome da coluna X (ou pressione Enter para usar a primeira): ");
            String xColumn = scanner.nextLine().trim();
            if (xColumn.isEmpty()) xColumn = null;

            System.out.print("Digite o nome da coluna Y (ou pressione Enter para usar a segunda): ");
            String yColumn = scanner.nextLine().trim();
            if (yColumn.isEmpty()) yColumn = null;

            service.generateChart(csvFile, chartType, xColumn, yColumn);
            System.out.println("Gráfico gerado com sucesso!");

        } catch (Exception e) {
            logger.error("Erro durante a execução: {}", e.getMessage());
        } finally {
            scanner.close();
        }
    }
} 