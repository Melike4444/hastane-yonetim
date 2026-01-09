pipeline {
  agent any

  environment {
    BASE_URL   = "http://host.docker.internal:8080"
    SELENIUM_URL = "http://host.docker.internal:4444/wd/hub"
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

    stage('Build Backend (skip tests)') {
      steps {
        sh '''
          chmod +x mvnw || true
          ./mvnw -DskipTests clean package
        '''
      }
    }

    stage('Docker Compose Up') {
      steps {
        sh '''
          docker compose down -v || true
          docker compose up -d --build
        '''
      }
    }

    stage('Wait App Ready') {
      steps {
        sh '''
          echo "Waiting for app to be ready at ${BASE_URL}"
          for i in $(seq 1 60); do
            if docker run --rm curlimages/curl:8.6.0 -fsS ${BASE_URL} >/dev/null; then
              echo "APP is ready"
              exit 0
            fi
            echo "Not ready yet... ($i)"
            sleep 2
          done
          echo "APP did not become ready"
          exit 1
        '''
      }
    }

    stage('Selenium Scenario 1') {
      steps {
        sh '''
          cd selenium-tests
          ../mvnw \
            -DbaseUrl=${BASE_URL} \
            -DremoteUrl=${SELENIUM_URL} \
            -Dtest=Senaryo1_UygulamaAciliyorMuTest \
            test
        '''
      }
    }

    stage('Selenium Scenario 2') {
      steps {
        sh '''
          cd selenium-tests
          ../mvnw \
            -DbaseUrl=${BASE_URL} \
            -DremoteUrl=${SELENIUM_URL} \
            -Dtest=Senaryo2_DoktorlarEndpointTest \
            test
        '''
      }
    }

    stage('Selenium Scenario 3') {
      steps {
        sh '''
          cd selenium-tests
          ../mvnw \
            -DbaseUrl=${BASE_URL} \
            -DremoteUrl=${SELENIUM_URL} \
            -Dtest=Senaryo3_RandevularEndpointTest \
            test
        '''
      }
    }

    stage('Selenium Scenario 4') {
      steps {
        sh '''
          cd selenium-tests
          ../mvnw \
            -DbaseUrl=${BASE_URL} \
            -DremoteUrl=${SELENIUM_URL} \
            -Dtest=Senaryo4_UiSmokeTest \
            test
        '''
      }
    }

    stage('Selenium Scenario 5') {
      steps {
        sh '''
          cd selenium-tests
          ../mvnw \
            -Dtest=Senaryo5_ApiSmokeTest \
            test
        '''
      }
    }
  }

  post {
    always {
      sh '''
        docker compose ps || true
        docker compose logs --no-color | tail -n 200 || true
      '''
    }
    cleanup {
      sh 'docker compose down -v || true'
    }
  }
}
