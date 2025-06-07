#!/bin/bash

# Local deployment package test script
set -e

echo "🚀 Testing deployment package creation..."

# Clean previous builds
echo "🧹 Cleaning previous builds..."
./mvnw clean

# Run tests
echo "🧪 Running tests..."
./mvnw test

# Build application
echo "🔨 Building application..."
./mvnw package -DskipTests

# Create deployment package
echo "📦 Creating deployment package..."
mkdir -p deploy
cp target/*.jar deploy/application.jar
cp Procfile deploy/

# Create ZIP
cd deploy && zip -r ../deploy.zip . && cd ..

echo "✅ Deployment package created: deploy.zip"
echo "📄 Package contents:"
unzip -l deploy.zip

# Cleanup
rm -rf deploy

echo "🎉 Ready for deployment!"
echo ""
echo "📋 Next steps:"
echo "1. Set up GitHub Secrets in your repository:"
echo "   - AWS_ACCESS_KEY_ID"
echo "   - AWS_SECRET_ACCESS_KEY" 
echo "   - AWS_REGION"
echo "   - EB_APPLICATION_NAME"
echo "   - EB_ENVIRONMENT_NAME"
echo "2. Push to main/master branch to trigger deployment"
