pipeline {
  environment {
  DOCKER_HOST = "tcp://dind:2375"
  DOCKER_TLS_CERTDIR = ""
  TESTCONTAINERS_HOST_OVERRIDE = "dind"
}

  agent any

  options {
  timestamps()
}


  environment {
    // Jenkins container içinde docker CLI genelde PATH'te olur.
    // Eğer sende farklıysa DOCKER="/usr/local/bin/docker" gibi ayarlayabilirsin.
    DOCKER = "docker"

    // UI (frontend) adresi: Selenium bununla açacak
    // Lokal: http://localhost:9091
    BASE_URL = "http://localhost:9091"

    // Backend container içinde dinlenen port (container içi)
    BACKEND_INTERNAL_PORT = "8080"
  }

  stages {

    stage('Checkout') {
      steps {
        checkout scm
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
      echo "Waiting for app INSIDE container: http://localhost:8080"

      # app container adı compose ile genelde hastane-yonetim-app-1 oluyor
      APP_C="hastane-yonetim-app-1"

      for i in $(seq 1 60); do
        if docker exec "$APP_C" curl -fsS http://localhost:8080 >/dev/null; then
          echo "Application is UP ✅"
          exit 0
        fi
        sleep 2
      done

      echo "App did not become healthy in time ❌"
      docker exec "$APP_C" curl -i http://localhost:8080 || true
      docker compose -f docker-compose.yml logs --no-color app || true
      exit 1
    '''
  }
}


    stage('Backend: Unit Tests') {
      steps {
        sh '''
          set -e
          chmod +x mvnw || true
          ./mvnw -q -DskipITs=true -DskipTests=false test
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
          ./mvnw -q -DskipITs=false verify
        '''
      }
      post {
        always {
          junit allowEmptyResults: true, testResults: '**/target/failsafe-reports/*.xml'
        }
      }
    }

    stage('UI: Selenium Tests') {
  steps {
    sh '''
      set -e
      echo "Running Selenium tests with baseUrl=http://dind:8080"
      chmod +x mvnw || true
      ./mvnw -q -f selenium-tests/pom.xml -DbaseUrl=http://dind:8080 test
    '''
  }
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
        echo "Docker compose logs (last 200 lines each):"
        docker compose ps || true
        docker compose logs --tail=200 || true
      '''
    }
    cleanup {
      sh '''
        echo "Docker compose down..."
        docker compose down -v || true
      '''
    }
  }
}
