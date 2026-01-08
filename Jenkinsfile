pipeline {
  agent any

 environment {
  DOCKER_HOST = "tcp://host.docker.internal:2375"
  DOCKER_TLS_CERTDIR = ""
  TESTCONTAINERS_HOST_OVERRIDE = "host.docker.internal"
}
./mvnw -q -f selenium-tests/pom.xml -DbaseUrl=http://dind:8080 test


  options {
    timestamps()
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
          ./mvnw -q -DskipITs=true test
        '''
      }
      post {
        always {
          junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
        }
      }
    }

    stage('Docker Up') {
      steps {
        sh '''
          set -e
          echo "Docker compose up..."
          docker compose up -d --build
          docker ps
        '''
      }
    }

    stage('Wait App Healthy') {
      steps {
        sh '''
          set -e
          APP_C="hastane-yonetim-app-1"
          echo "Waiting for app INSIDE container: http://localhost:8080"
          for i in $(seq 1 60); do
            if docker exec "$APP_C" curl -fsS http://localhost:8080 >/dev/null; then
              echo "Application is UP ✅"
              exit 0
            fi
            sleep 2
          done
          echo "App did not become healthy in time ❌"
          docker logs --tail=200 "$APP_C" || true
          exit 1
        '''
      }
    }

    stage('Backend: Integration Tests') {
      steps {
        sh '''
          set -e
          chmod +x mvnw || true
          ./mvnw -q -DskipITs=false verify
        '''
      }
      post {
        always {
          junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml, target/failsafe-reports/*.xml'
        }
      }
    }

    stage('UI: Selenium Tests') {
      steps {
        sh '''
          set -e
          echo "Running Selenium tests with baseUrl=http://dind:8080"
          chmod +x mvnw || true
         ./mvnw -q -f selenium-tests/pom.xml -DbaseUrl=http://host.docker.internal:8080 test

        '''
      }
      post {
        always {
          junit allowEmptyResults: true, testResults: 'selenium-tests/target/surefire-reports/*.xml'
          archiveArtifacts allowEmptyArchive: true, artifacts: 'selenium-tests/target/surefire-reports/**'
        }
      }
    }
  }

  post {
    always {
      sh '''
        set +e
        echo "Docker compose logs (last 200 lines each):"
        docker compose ps || true
        docker compose logs --tail=200 || true
        echo "Docker compose down..."
        docker compose down -v || true
      '''
    }
  }
}
