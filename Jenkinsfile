pipeline {
  agent any
  options { timestamps() }

  stages {
    stage('Checkout') {
      steps { checkout scm }
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
          docker compose version || docker-compose --version

          # Build + up
          if docker compose version >/dev/null 2>&1; then
            docker compose -f docker-compose.yml up -d --build
          else
            docker-compose -f docker-compose.yml up -d --build
          fi

          # Wait backend to be ready
          echo "Waiting for backend on http://localhost:8080 ..."
          for i in $(seq 1 30); do
            if curl -sSf http://localhost:8080/ >/dev/null; then
              echo "Backend is up ✅"
              exit 0
            fi
            sleep 2
          done

          echo "Backend did not become ready in time ❌"
          exit 1
        '''
      }
    }

    // 6. madde: en az 3 test senaryosu (ayrı stage)
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
        archiveArtifacts artifacts: 'target/*.jar', fingerprint: true, allowEmptyArchive: false
      }
    }
  }

  post {
    always {
      sh '''
        set +e
        if docker compose version >/dev/null 2>&1; then
          docker compose -f docker-compose.yml down
        else
          docker-compose -f docker-compose.yml down
        fi
      '''
    }
  }
}

