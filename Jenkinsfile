pipeline {
  agent any

  options {
    timestamps()
  }

  environment {
    MAVEN_OPTS = "-Dmaven.repo.local=.m2"
  }

  stages {

    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Build') {
      steps {
        sh '''
          set -e
          chmod +x mvnw
          ./mvnw -q -DskipTests clean package
        '''
      }
    }

    stage('Unit Tests (no selenium-tests)') {
      steps {
        sh '''
          set -e
          rm -rf selenium-tests/target || true
          # selenium-tests modülünü çalıştırma
          ./mvnw -q -pl '!selenium-tests' -am test
        '''
      }
      post {
        always {
          # SADECE root module raporlarını topla (selenium raporları asla gelmesin)
          junit allowEmptyResults: true, testResults: 'target/surefire-reports


  }

  post {
    success {
      echo '✅ PIPELINE SUCCESS'
    }
    failure {
      echo '❌ PIPELINE FAILED'
    }
    always {
      sh 'docker compose down -v || true'
    }
  }
}
