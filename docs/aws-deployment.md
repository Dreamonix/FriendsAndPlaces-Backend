# AWS Elastic Beanstalk Deployment Setup

Diese Anleitung beschreibt, wie Sie Ihre GitHub Actions Pipeline für automatisches Deployment auf eine bereits existierende AWS Elastic Beanstalk Umgebung einrichten.

## Voraussetzungen

1. **AWS Elastic Beanstalk** Anwendung und Umgebung bereits erstellt
2. **GitHub Repository** mit dem Code
3. **IAM User** mit Deployment-Berechtigungen

## GitHub Repository Setup

### 1. GitHub Secrets konfigurieren

Fügen Sie folgende Secrets in Ihrem GitHub Repository hinzu:
(`Settings` > `Secrets and variables` > `Actions`)

**Erforderliche Secrets:**
- `AWS_ACCESS_KEY_ID`: Access Key des IAM Users
- `AWS_SECRET_ACCESS_KEY`: Secret Access Key des IAM Users  
- `AWS_REGION`: AWS Region Ihrer EB-Umgebung (z.B. `us-east-1`, `eu-central-1`)
- `EB_APPLICATION_NAME`: Name Ihrer existierenden Elastic Beanstalk Anwendung
- `EB_ENVIRONMENT_NAME`: Name Ihrer existierenden Elastic Beanstalk Umgebung

### 2. IAM Berechtigungen

Ihr IAM User benötigt mindestens folgende Berechtigungen:
- `elasticbeanstalk:CreateApplicationVersion`
- `elasticbeanstalk:UpdateEnvironment`
- `elasticbeanstalk:DescribeEnvironments`
- `elasticbeanstalk:DescribeApplicationVersions`
- `s3:GetObject`
- `s3:PutObject` (für den EB S3 Bucket)

## Deployment Process

Das Deployment wird automatisch ausgelöst bei:

1. **Push auf main/master Branch**: Vollständiger Test und Deployment
2. **Pull Request**: Nur Tests werden ausgeführt

### Pipeline Schritte:

1. **Test Job**:
   - Checkout Code
   - Setup JDK 17
   - PostgreSQL Service starten
   - Tests ausführen

2. **Build and Deploy Job** (nur bei main/master):
   - Code checkout
   - JDK 17 setup
   - Maven build (JAR erstellen)
   - Deployment-Paket erstellen (JAR + Procfile)
   - Deployment auf existierende Elastic Beanstalk Umgebung

## Lokale Entwicklung

```bash
# Anwendung lokal starten
./mvnw spring-boot:run

# Tests ausführen  
./mvnw test

# Deployment-Paket testen
./test-deployment.sh
```

## Monitoring und Debugging

- **AWS Console**: Elastic Beanstalk Dashboard für Deployment Status
- **CloudWatch Logs**: Anwendungs- und Server-Logs
- **Health Check**: Über EB Console verfügbar
- **API Dokumentation**: `/swagger-ui/index.html` (nach Deployment)

## Troubleshooting

### Häufige Probleme:

1. **Deployment fehlschlägt**:
   ```bash
   # Logs in AWS Console prüfen
   # GitHub Actions Logs überprüfen
   # Sicherstellen dass alle Secrets korrekt gesetzt sind
   ```

2. **Tests schlagen fehl**:
   ```bash
   # Lokal testen
   ./mvnw test
   
   # Test-Datenbank prüfen
   docker-compose up -d  # falls vorhanden
   ```

3. **JAR Build Probleme**:
   ```bash
   # Lokal builden
   ./mvnw clean package
   
   # Dependencies prüfen
   ./mvnw dependency:tree
   ```

## Erste Verwendung

1. **GitHub Secrets einrichten** (siehe oben)
2. **Pipeline testen**:
   ```bash
   # Lokales Test-Deployment
   ./test-deployment.sh
   ```
3. **Ersten Deployment starten**:
   ```bash
   git add .
   git commit -m "Add GitHub Actions deployment pipeline"
   git push origin main
   ```

Die Pipeline wird automatisch gestartet und deployed auf Ihre existierende Elastic Beanstalk Umgebung.
