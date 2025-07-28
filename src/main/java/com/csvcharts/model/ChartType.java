package com.csvcharts.model;

/**
 * Enum que define os tipos de gráficos disponíveis
 */
public enum ChartType {
    BAR("Gráfico de Barras"),
    LINE("Gráfico de Linha"),
    PIE("Gráfico de Pizza"),
    SCATTER("Gráfico de Dispersão");

    private final String description;

    ChartType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return name() + " - " + description;
    }
} 