pipeline {
  agent any
  options { timestamps() }

  stages {
    stage('Checkout') {
      steps { checkout scm }
    }

    stage('Backend: Unit Tests') {
      steps {
        sh '''
          set -e
          chmod +x mvnw
          ./mvnw -Dtest=!**/integration/** test
        '''
      }
    }

    stage('Backend: Integration Tests') {
      steps {
        sh '''
          set -e
          chmod +x mvnw
          ./mvnw -Dtest=**/integration/** test
        '''
      }
    }

    stage('Backend: Package') {
      steps {
        sh '''
          set -e
          chmod +x mvnw
          ./mvnw -DskipTests package
        '''
      }
    }

    stage('Docker Up') {
      steps {
        sh '''
          set +e
          docker compose -f docker-compose.yml up -d --build
          set -e
        '''
      }
    }

    stage('Wait App Healthy') {
      steps {
        sh '''
          set -e
          for i in $(seq 1 40); do
            if curl -fsS http://localhost:8080 >/dev/null; then
              echo "APP OK"
              exit 0
            fi
            echo "Waiting app... ($i)"
            sleep 2
          done
          echo "App did not become healthy"
          exit 1
        '''
      }
    }

    stage('Selenium Scenario 1') {
      steps {
        sh '''
          set -e
          chmod +x mvnw
          ./mvnw -pl selenium-tests -Dtest=Senaryo1_* test
        '''
      }
    }

    stage('Selenium Scenario 2') {
      steps {
        sh '''
          set -e
          chmod +x mvnw
          ./mvnw -pl selenium-tests -Dtest=Senaryo2_* test
        '''
      }
    }

    stage('Selenium Scenario 3') {
      steps {
        sh '''
          set -e
          chmod +x mvnw
          ./mvnw -pl selenium-tests -Dtest=Senaryo3_* test
        '''
      }
    }

    stage('Selenium Scenario 4') {
      steps {
        sh '''
          set -e
          chmod +x mvnw
          ./mvnw -pl selenium-tests -Dtest=Senaryo4_* test
        '''
      }
    }

    stage('Selenium Scenario 5') {
      steps {
        sh '''
          set -e
          chmod +x mvnw
          ./mvnw -pl selenium-tests -Dtest=Senaryo5_* test
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

