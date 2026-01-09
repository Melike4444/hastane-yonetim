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
          chmod +x mvnw
          ./mvnw -q -DskipTests clean package
        '''
      }
    }

    stage('Unit Tests') {
      steps {
        sh '''
          ./mvnw -q test
        '''
      }
      post {
        always {
          junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
        }
      }
    }

    stage('Integration Tests') {
      steps {
        sh '''
          ./mvnw -q failsafe:integration-test failsafe:verify
        '''
      }
      post {
        always {
          junit allowEmptyResults: true, testResults: '**/target/failsafe-reports/*.xml'
        }
      }
    }

    stage('Docker Build') {
      steps {
        sh '''
          docker build -t hastane-yonetim:latest .
        '''
      }
    }

    // 🔴 Selenium şimdilik kapalı (ileride açacağız)
    /*
    stage('Selenium UI Tests') {
      steps {
        sh '''
          docker compose up -d
          ./mvnw -pl selenium-tests test
        '''
      }
    }
    */

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
