pipeline {
  agent any

  environment {
    // Jenkins container içinde docker CLI var; socket'i /var/run/docker.sock olarak bağladık varsayımıyla:
    DOCKER_HOST = "unix:///var/run/docker.sock"
    TESTCONTAINERS_DOCKER_SOCKET_OVERRIDE = "/var/run/docker.sock"

    // Mac Docker Desktop + Jenkins container senaryosunda en stabil host override:
    // Container içinden host’a erişim için:
    TESTCONTAINERS_HOST_OVERRIDE = "host.docker.internal"

    MAVEN_OPTS = "-Dmaven.repo.local=.m2/repository"
  }

  options {
    timestamps()
  }

  stages {

    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Backend: Build') {
      steps {
        sh '''
          set -e
          chmod +x mvnw || true
          ./mvnw -q -DskipTests clean package
        '''
      }
    }

    stage('Backend: Unit Tests') {
      steps {
        sh '''
          set -e
          chmod +x mvnw || true
          ./mvnw -q test
        '''
      }
      post {
        always {
          junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
        }
      }
    }

    stage('Backend: Integration Tests') {
      steps {
        sh '''
          set -e
          chmod +x mvnw || true

          echo "DOCKER_HOST=$DOCKER_HOST"
          echo "TESTCONTAINERS_DOCKER_SOCKET_OVERRIDE=$TESTCONTAINERS_DOCKER_SOCKET_OVERRIDE"
          echo "TESTCONTAINERS_HOST_OVERRIDE=$TESTCONTAINERS_HOST_OVERRIDE"

          # Integration testler (failsafe varsa verify ile çalışır)
          ./mvnw -q verify
        '''
      }
      post {
        always {
          junit allowEmptyResults: true, testResults: '**/target/failsafe-reports/*.xml, **/target/surefire-reports/*.xml'
        }
      }
    }

    stage('Selenium Tests (non-blocking)') {
      steps {
        sh '''
          set +e
          chmod +x mvnw || true

          # Selenium projesinde mvn yoksa bile mvnw ile garanti
          ./mvnw -q -pl selenium-tests test || true
          exit 0
        '''
      }
      post {
        always {
          junit allowEmptyResults: true, testResults: 'selenium-tests/target/surefire-reports/*.xml'
        }
      }
    }
  }

  post {
    always {
      echo "Pipeline finished."
    }
  }
}
