pipeline {
  agent any
  options { timestamps() }

  stages {

    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Backend: Test & Package') {
      steps {
        sh '''
          set -e
          chmod +x mvnw || true
          ./mvnw -v
          ./mvnw clean test
          ./mvnw -DskipTests package
        '''
      }
    }

    stage('JUnit Report') {
      steps {
        junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
      }
    }

    stage('Docker: Build & Up') {
      steps {
        sh '''
          set -e
          docker --version

          # Docker Compose komutunu belirle
          if docker compose version >/dev/null 2>&1; then
            COMPOSE="docker compose"
          elif command -v docker-compose >/dev/null 2>&1; then
            COMPOSE="docker-compose"
          else
            echo "Docker Compose bulunamadı. Docker Desktop açık mı?"
            exit 1
          fi

          # Container'ları build et ve ayağa kaldır
          $COMPOSE -f docker-compose.yml up -d --build

          # Backend ayağa kalkana kadar bekle
          echo "Backend bekleniyor (http://localhost:8080)..."
          for i in $(seq 1 30); do
            if curl -s http://localhost:8080 >/dev/null; then
              echo "Backend ayakta ✅"
              exit 0
            fi
            sleep 2
          done

          echo "Backend zamanında ayağa kalkmadı ❌"
          exit 1
        '''
      }
    }

    stage('Selenium Scenario 1') {
      steps {
        sh '''
          set -e
          chmod +x mvnw || true
          ./mvnw -f selenium-tests/pom.xml -Dtest=Senaryo1_UygulamaAciliyorMuTest test
        '''
      }
    }

    stage('Selenium Scenario 2') {
      steps {
        sh '''
          set -e
          chmod +x mvnw || true
          ./mvnw -f selenium-tests/pom.xml -Dtest=Senaryo2_DoktorlarEndpointTest test
        '''
      }
    }

    stage('Selenium Scenario 3') {
      steps {
        sh '''
          set -e
          chmod +x mvnw || true
          ./mvnw -f selenium-tests/pom.xml -Dtest=Senaryo3_RandevularEndpointTest test
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

        if docker compose version >/dev/null 2>&1; then
          COMPOSE="docker compose"
        elif command -v docker-compose >/dev/null 2>&1; then
          COMPOSE="docker-compose"
        else
          echo "Docker Compose yok, down atlanıyor."
          exit 0
        fi

        $COMPOSE -f docker-compose.yml down
      '''
    }
  }
}

