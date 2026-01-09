pipeline {
  agent any

  options { timestamps() }

  environment {
    MAVEN_OPTS = "-Dmaven.repo.local=.m2"
  }

  stages {

    stage('Checkout') {
      steps { checkout scm }
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

    stage('Unit Tests') {
      steps {
        sh '''
          set -e
          ./mvnw -q test -Dtest='!*selenium*'
        '''
      }
      post {
        always {
          junit '**/target/surefire-reports/*.xml'
        }
      }
    }

    stage('Integration Tests') {
      steps {
        sh '''
          set -e
          ./mvnw -q failsafe:integration-test failsafe:verify -Dtest='!*selenium*'
        '''
      }
      post {
        always {
          junit '**/target/failsafe-reports/*.xml'
        }
      }
    }

    stage('Docker Build') {
      steps {
        sh 'docker build -t hastane-yonetim:latest .'
      }
    }
  }

  post {
    always {
      sh 'docker compose down -v || true'
    }
  }
}
