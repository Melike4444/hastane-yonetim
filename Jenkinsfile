pipeline {
  agent any

  environment {
    DOCKER = "/usr/local/bin/docker"
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

    stage('Backend: Unit Tests') {
      steps {
        sh '''
          set -e
          chmod +x mvnw || true
          ./mvnw -Dtest='!**/integration/**' test
        '''
      }
    }

    stage('Backend: Integration Tests') {
      steps {
        sh '''
          set -e
          chmod +x mvnw || true
          ./mvnw -Dtest='**/integration/**' test
        '''
      }
    }

    stage('Backend: Package') {
      steps {
        sh '''
          set -e
          chmod +x mvnw || true
          ./mvnw -DskipTests package
        '''
      }
    }

    stage('JUnit Report') {
      steps {
        junit allowEmptyResults: true,
              testResults: 'target/surefire-reports/*.xml, selenium-tests/target/surefire-reports/*.xml'
      }
    }

    stage('Docker Up') {
      steps {
        sh '''
          set -e
          echo "Docker compose ile sistem ayağa kaldırılıyor..."
          /usr/local/bin/docker compose -f docker-compose.yml up -d --build
        '''
      }
    }

    stage('Selenium Scenario 1') {
      steps {
        sh '''
          set -e
          ./mvnw -f selenium-tests/pom.xml \
            -Dtest=Senaryo1_UygulamaAciliyorMuTest test
        '''
      }
    }

    stage('Selenium Scenario 2') {
      steps {
        sh '''
          set -e
          ./mvnw -f selenium-tests/pom.xml \
            -Dtest=Senaryo2_DoktorlarEndpointTest test
        '''
      }
    }

    stage('Selenium Scenario 3') {
      steps {
        sh '''
          set -e
          ./mvnw -f selenium-tests/pom.xml \
            -Dtest=Senaryo3_RandevularEndpointTest test
        '''
      }
    }

    stage('Selenium Scenario 4') {
      steps {
        sh '''
          set -e
          ./mvnw -f selenium-tests/pom.xml \
            -Dtest=Senaryo4_UiSmokeTest test
        '''
      }
    }

    stage('Selenium Scenario 5') {
      steps {
        sh '''
          set -e
          ./mvnw -f selenium-tests/pom.xml \
            -Dtest=Senaryo5_HastalarEndpointTest \
            -DapiBaseUrl=http://localhost:8080 test
        '''
      }
    }

    stage('Docker Down') {
      steps {
        sh '''
          set +e
          /usr/local/bin/docker compose -f docker-compose.yml down -v --remove-orphans
        '''
      }
    }

  }

  post {
    always {
      sh '''
        set +e
        /usr/local/bin/docker compose -f docker-compose.yml down || true
      '''
    }
  }
}

