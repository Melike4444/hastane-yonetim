kpipeline {
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

    // Docker varsa çalışır, yoksa pipeline bozulmaz
    stage('Docker: Build & Up (Optional)') {
      steps {
        sh '''
          set +e

          if ! command -v docker >/dev/null 2>&1; then
            echo "Docker bu Jenkins ortamında yok → Docker stage SKIP ✅"
            exit 0
          fi

          docker --version

          if docker compose version >/dev/null 2>&1; then
            COMPOSE="docker compose"
          elif command -v docker-compose >/dev/null 2>&1; then
            COMPOSE="docker-compose"
          else
            echo "Docker Compose yok → Docker stage SKIP ✅"
            exit 0
          fi

          $COMPOSE -f docker-compose.yml up -d --build

          echo "Backend bekleniyor (http://localhost:8080)..."
          for i in $(seq 1 20); do
            if curl -s http://localhost:8080 >/dev/null; then
              echo "Backend ayakta ✅"
              exit 0
            fi
            sleep 2
          done

          echo "Backend geç açıldı ama pipeline devam ediyor ✅"
          exit 0
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
            docker compose -f docker-compose.yml down
          elif command -v docker-compose >/dev/null 2>&1; then
            docker-compose -f docker-compose.yml down
          fi
        fi
      '''
    }
  }
}

