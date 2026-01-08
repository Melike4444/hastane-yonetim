pipeline {
  agent any

  options {
    timestamps()
  }

  environment {
    APP_URL = 'http://app:8080'
  }

  stages {

    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Backend: Unit Tests') {
      steps {
        sh '''
          ./mvnw clean test
        '''
      }
    }

    stage('Backend: Package') {
      steps {
        sh '''
          ./mvnw -DskipTests package
        '''
      }
    }

    stage('Docker Up') {
      steps {
        sh '''
          set -e
          docker compose -f docker-compose.yml up -d --build
        '''
      }
    }

    stage('Wait App Healthy') {
      steps {
        sh '''
          set -e
          echo "Waiting for app: $APP_URL"

          for i in $(seq 1 60); do
            if curl -fsS "$APP_URL" > /dev/null; then
              echo "App is UP ✅"
              exit 0
            fi
            sleep 2
                      done

          echo "App did not become healthy in time ❌"
          curl -i "$APP_URL" || true

          echo "---- docker ps ----"
          docker ps || true

          echo "---- app logs ----"
          docker compose -f docker-compose.yml logs --no-color --tail=200 app || true

          echo "---- db logs ----"
          docker compose -f docker-compose.yml logs --no-color --tail=200 db || true

          exit 1
        '''
      }
    }

    stage('Selenium Scenario 1') {
      steps {
        sh '''
          set -e
          cd selenium-tests
          mvn -q -Dtest=Scenario1Test test
        '''
      }
    }

    stage('Selenium Scenario 2') {
      steps {
        sh '''
          set -e
          cd selenium-tests
          mvn -q -Dtest=Scenario2Test test
        '''
      }
    }

    stage('Selenium Scenario 3') {
      steps {
        sh '''
          set -e
          cd selenium-tests
          mvn -q -Dtest=Scenario3Test test
        '''
      }
    }

    stage('Selenium Scenario 4') {
      steps {
        sh '''
          set -e
          cd selenium-tests
          mvn -q -Dtest=Scenario4Test test
        '''
      }
    }

    stage('Selenium Scenario 5') {
      steps {
        sh '''
          set -e
          cd selenium-tests
          mvn -q -Dtest=Scenario5Test test
        '''
      }
    }
  }

  post {
    always {
      junit '**/target/surefire-reports/*.xml'

      sh '''
        set +e
        docker compose -f docker-compose.yml down
        true
      '''
    }
  }
}


