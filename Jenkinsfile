pipeline {
  agent any

  options {
    timestamps()
  }

  stages {

    /* =======================================================
       1. CHECKOUT
       ======================================================= */
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    /* =======================================================
       2. BUILD
       ======================================================= */
    stage('Build') {
      steps {
        sh '''
          chmod +x mvnw
          ./mvnw clean package -DskipTests
        '''
      }
    }

    /* =======================================================
       3. UNIT TESTS
       ======================================================= */
    stage('Unit Tests') {
      steps {
        sh '''
          chmod +x mvnw
          ./mvnw test
        '''
      }
      post {
        always {
          junit 'target/surefire-reports/*.xml'
        }
      }
    }

    /* =======================================================
       4. INTEGRATION TESTS
       ======================================================= */
    stage('Integration Tests') {
      steps {
        sh '''
          chmod +x mvnw
          ./mvnw verify
        '''
      }
      post {
        always {
          junit 'target/failsafe-reports/*.xml'
        }
      }
    }

    /* =======================================================
       5. RUN SYSTEM (DOCKER)
       ======================================================= */
    stage('Run System (Docker)') {
      steps {
        sh '''
          docker-compose up -d --build
          sleep 20
        '''
      }
    }

    /* =======================================================
       6. SYSTEM TESTS (SELENIUM)
       ======================================================= */

    stage('System Test - Senaryo 1') {
      steps {
        sh '''
          chmod +x mvnw
          ./mvnw -Dtest=com.hastane.selenium.Senaryo1_* test
        '''
      }
    }

    stage('System Test - Senaryo 2') {
      steps {
        sh '''
          chmod +x mvnw
          ./mvnw -Dtest=com.hastane.selenium.Senaryo2_* test
        '''
      }
    }

    stage('System Test - Senaryo 3') {
      steps {
        sh '''
          chmod +x mvnw
          ./mvnw -Dtest=com.hastane.selenium.Senaryo3_* test
        '''
      }
    }

    stage('System Test - Senaryo 4 (UI Smoke)') {
      steps {
        sh '''
          chmod +x mvnw
          ./mvnw -Dtest=com.hastane.selenium.Senaryo4_* test
        '''
      }
    }

    stage('System Test - Senaryo 5 (Dashboard UI)') {
      steps {
        sh '''
          chmod +x mvnw
          ./mvnw -Dtest=com.hastane.selenium.Senaryo5_* test
        '''
      }
    }
  }

  /* =======================================================
     CLEANUP
     ======================================================= */
  post {
    always {
      sh '''
        docker-compose down -v || true
      '''
    }
  }
}

