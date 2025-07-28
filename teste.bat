@echo off
echo ========================================
echo    CSV Charts Generator - Teste
echo ========================================
echo.

echo Compilando o projeto...
call mvn clean compile
if %errorlevel% neq 0 (
    echo Erro na compilacao!
    pause
    exit /b 1
)

echo.
echo Criando JAR executavel...
call mvn package
if %errorlevel% neq 0 (
    echo Erro na criacao do JAR!
    pause
    exit /b 1
)

echo.
echo ========================================
echo Testando diferentes tipos de graficos...
echo ========================================

echo.
echo 1. Testando grafico de barras...
java -jar target/csv-charts-generator-1.0.0.jar dados_exemplo.csv BAR Cidade Populacao

echo.
echo 2. Testando grafico de linha...
java -jar target/csv-charts-generator-1.0.0.jar dados_exemplo.csv LINE Cidade IDH

echo.
echo 3. Testando grafico de pizza...
java -jar target/csv-charts-generator-1.0.0.jar dados_exemplo.csv PIE Cidade Populacao

echo.
echo 4. Testando grafico de dispersao...
java -jar target/csv-charts-generator-1.0.0.jar dados_exemplo.csv SCATTER Area Populacao

echo.
echo ========================================
echo Testes concluidos!
echo Verifique os arquivos PNG gerados.
echo ========================================
pause 