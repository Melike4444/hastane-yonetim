pipeline {
  agent any
  options { timestamps() }

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
        junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml, selenium-tests/target/surefire-reports/*.xml'
      }
    }

    stage('Docker Up') {
      steps {
        sh '''
          set -e
          echo "Docker compose ile sistem ayağa kaldırılıyor..."
          docker compose -f docker-compose.yml up -d --build

          echo "Backend hazır mı kontrol ediliyor (http://localhost:8080/api/hastalar)?"
          for i in $(seq 1 60); do
            code=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/api/hastalar || true)
            if [ "$code" = "200" ]; then
              echo "Backend ayakta ✅"
              exit 0
            fi
            sleep 1
          done

          echo "Backend 60 saniyede ayağa kalkmadı ❌"
          docker compose -f docker-compose.yml logs --no-color || true
          exit 1
        '''
      }
    }

    stage('Selenium Scenario 1') {
      steps {
        sh '''
          set -e
          ./mvnw -f selenium-tests/pom.xml -Dtest=Senaryo1_UygulamaAciliyorMuTest test
        '''
      }
    }

    stage('Selenium Scenario 2') {
      steps {
        sh '''
          set -e
          ./mvnw -f selenium-tests/pom.xml -Dtest=Senaryo2_DoktorlarEndpointTest test
        '''
      }
    }

    stage('Selenium Scenario 3') {
      steps {
        sh '''
          set -e
          ./mvnw -f selenium-tests/pom.xml -Dtest=Senaryo3_RandevularEndpointTest test
        '''
      }
    }

    stage('Selenium Scenario 4') {
      steps {
        sh '''
          set -e
          ./mvnw -f selenium-tests/pom.xml -Dtest=Senaryo4_UiSmokeTest test
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
          docker compose -f docker-compose.yml down -v --remove-orphans
        '''
      }
    }

    stage('Archive Jar') {
      steps {
        archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
      }
    }
  }

  post {
    always {
      sh '''
        set +e
        if command -v docker >/dev/null 2>&1; then
          if docker compose version >/dev/null 2>&1; then
            docker compose -f docker-compose.yml down -v --remove-orphans
          elif command -v docker-compose >/dev/null 2>&1; then
            docker-compose -f docker-compose.yml down -v
          fi
        fi
      '''
    }
  }
}
