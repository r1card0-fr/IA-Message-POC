package com.csvcharts.model;

import java.util.List;
import java.util.Map;

/**
 * Classe que representa os dados de um arquivo CSV
 */
public class CSVData {
    private List<String> headers;
    private List<Map<String, String>> rows;

    public CSVData(List<String> headers, List<Map<String, String>> rows) {
        this.headers = headers;
        this.rows = rows;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public List<Map<String, String>> getRows() {
        return rows;
    }

    public void setRows(List<Map<String, String>> rows) {
        this.rows = rows;
    }

    public int getRowCount() {
        return rows.size();
    }

    public int getColumnCount() {
        return headers.size();
    }

    public String getValue(int rowIndex, String columnName) {
        if (rowIndex >= 0 && rowIndex < rows.size()) {
            return rows.get(rowIndex).get(columnName);
        }
        return null;
    }

    public List<String> getColumnValues(String columnName) {
        return rows.stream()
                .map(row -> row.get(columnName))
                .toList();
    }

    public boolean hasColumn(String columnName) {
        return headers.contains(columnName);
    }

    @Override
    public String toString() {
        return "CSVData{" +
                "headers=" + headers +
                ", rowCount=" + getRowCount() +
                ", columnCount=" + getColumnCount() +
                '}';
    }
} 