# CSV Charts Generator

Projeto Java 21 para gerar gráficos a partir de arquivos CSV.

## Características

- **Java 21**: Utiliza as últimas funcionalidades do Java
- **Múltiplos tipos de gráfico**: Barras, Linha, Pizza e Dispersão
- **Interface flexível**: Modo linha de comando e interativo
- **Bibliotecas robustas**: OpenCSV para leitura de dados e JFreeChart para gráficos

## Tipos de Gráficos Suportados

- **BAR**: Gráfico de barras
- **LINE**: Gráfico de linha
- **PIE**: Gráfico de pizza
- **SCATTER**: Gráfico de dispersão

## Pré-requisitos

- Java 21 ou superior
- Maven 3.6 ou superior

## Instalação e Compilação

1. Clone ou baixe o projeto
2. Navegue até o diretório do projeto:
   ```bash
   cd D:\Ternium\POC
   ```

3. Compile o projeto:
   ```bash
   mvn clean compile
   ```

4. Crie o JAR executável:
   ```bash
   mvn package
   ```

## Uso

### Modo Interativo

Execute o JAR sem argumentos:
```bash
java -jar target/csv-charts-generator-1.0.0.jar
```

O programa irá solicitar:
- Caminho do arquivo CSV
- Tipo de gráfico
- Colunas X e Y (opcional)

### Modo Linha de Comando

```bash
java -jar target/csv-charts-generator-1.0.0.jar <arquivo_csv> <tipo_grafico> [coluna_x] [coluna_y]
```

**Exemplos:**

```bash
# Gráfico de barras usando primeira e segunda coluna
java -jar target/csv-charts-generator-1.0.0.jar dados_exemplo.csv BAR

# Gráfico de linha especificando colunas
java -jar target/csv-charts-generator-1.0.0.jar dados_exemplo.csv LINE Cidade Populacao

# Gráfico de pizza
java -jar target/csv-charts-generator-1.0.0.jar dados_exemplo.csv PIE Cidade IDH

# Gráfico de dispersão
java -jar target/csv-charts-generator-1.0.0.jar dados_exemplo.csv SCATTER Area Populacao
```

## Formato do Arquivo CSV

O arquivo CSV deve ter:
- Primeira linha com cabeçalhos
- Dados separados por vírgula
- Valores numéricos para colunas Y (exceto gráfico de pizza)

**Exemplo:**
```csv
Cidade,Populacao,Area,IDH
São Paulo,12325232,1521,0.805
Rio de Janeiro,6747815,1200,0.799
```

## Estrutura do Projeto

```
D:\Ternium\POC\
├── src/main/java/com/csvcharts/
│   ├── Main.java                 # Classe principal
│   ├── model/
│   │   ├── ChartType.java        # Enum dos tipos de gráfico
│   │   └── CSVData.java          # Modelo dos dados CSV
│   ├── service/
│   │   └── CSVChartService.java  # Serviço principal
│   └── util/
│       ├── CSVReader.java        # Leitor de CSV
│       └── ChartGenerator.java   # Gerador de gráficos
├── src/main/resources/
│   └── logback.xml              # Configuração de logging
├── dados_exemplo.csv            # Arquivo de exemplo
├── pom.xml                      # Configuração Maven
└── README.md                    # Este arquivo
```

## Dependências

- **OpenCSV 5.8**: Para leitura de arquivos CSV
- **JFreeChart 1.5.4**: Para geração de gráficos
- **Apache Commons Lang 3.12.0**: Utilitários
- **SLF4J + Logback**: Sistema de logging

## Saída

Os gráficos são salvos como arquivos PNG no mesmo diretório do arquivo CSV de entrada, com o nome:
`<nome_arquivo>_<tipo_grafico>.png`

**Exemplo:** `dados_exemplo_bar.png`

## Logs

O programa gera logs detalhados durante a execução. Os logs são exibidos no console e também salvos em arquivo.

## Tratamento de Erros

- Validação de arquivo CSV
- Verificação de colunas existentes
- Tratamento de valores não numéricos
- Criação automática de diretórios de saída

## Contribuição

Para contribuir com o projeto:
1. Faça um fork
2. Crie uma branch para sua feature
3. Commit suas mudanças
4. Push para a branch
5. Abra um Pull Request

## Licença

Este projeto está sob a licença MIT. 