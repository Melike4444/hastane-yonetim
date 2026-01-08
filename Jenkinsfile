pipeline {
  agent any

  options {
    timestamps()
  }

  environment {
    // Jenkins container içinden app'e ulaşmak için:
    APP_URL = 'http://host.docker.internal:8080'
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
          ./mvnw -q -DskipTests clean package
        '''
      }
    }

    stage('Unit Tests') {
      steps {
        sh '''
          set -e
          ./mvnw -q test
        '''
      }
    }

    stage('Docker Up') {
      steps {
        sh '''
          set -e
          docker compose -f docker-compose.yml up -d --build
          docker compose -f docker-compose.yml ps
        '''
      }
    }

    stage('Wait App Healthy') {
      steps {
        sh '''
          set -e
          echo "Waiting for app: $APP_URL"
          for i in $(seq 1 60); do
            if curl -fsS "$APP_URL" >/dev/null; then
              echo "App is up ✅"
              exit 0
            fi
            sleep 2
          done

          echo "App did not become healthy in time ❌"
          curl -i "$APP_URL" || true
          docker compose -f docker-compose.yml logs --no-color app || true
          exit 1
        '''
      }
    }

    stage('Selenium Scenario 1-5') {
      steps {
        sh '''
          set -e
          cd selenium-tests
          # testlere base url geçiyoruz
          mvn -q -DbaseUrl="$APP_URL" test
        '''
      }
    }
  }

  post {
    always {
      junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
      sh '''
        set +e
        docker compose -f docker-compose.yml down
        true
      '''
    }
  }
}

