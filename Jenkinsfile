pipeline {
  agent any

  options {
    timestamps()
  }

  environment {
    MAVEN_OPTS = "-Dmaven.repo.local=/var/jenkins_home/.m2/repository"
  }

  stages {

    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Backend: Build') {
      steps {
        sh '''
          set -e
          chmod +x mvnw || true
          ./mvnw -q -DskipTests=true clean package
        '''
      }
    }

    stage('Backend: Unit Tests') {
      steps {
        sh '''
          set -e
          chmod +x mvnw || true
          ./mvnw -q test
        '''
      }
      post {
        always {
          // ✅ Unit test raporlarını Jenkins "Test Result" kısmında gösterir
          junit testResults: '**/target/surefire-reports/*.xml',
                allowEmptyResults: true
        }
      }
    }

    stage('Integration Tests') {
      steps {
        sh '''
          set -e
          chmod +x mvnw || true
          ./mvnw -q verify
        '''
      }
      post {
        always {
          // ✅ Integration test raporlarını Jenkins'te gösterir (Failsafe)
          junit testResults: '**/target/failsafe-reports/*.xml',
                allowEmptyResults: true
        }
      }
    }

    stage('Run System (Docker)') {
      steps {
        sh '''
          set -e
          docker-compose up -d --build
        '''
      }
    }

    stage('System Test - Senaryo 1') {
      steps {
        sh '''
          set -e
          # örnek: backend API smoke / curl vs
          echo "Senaryo 1 burada koşuyor"
        '''
      }
    }

    stage('System Test - Senaryo 2') {
      steps {
        sh '''
          set -e
          echo "Senaryo 2 burada koşuyor"
        '''
      }
    }

    stage('System Test - Senaryo 3') {
      steps {
        sh '''
          set -e
          echo "Senaryo 3 burada koşuyor"
        '''
      }
    }

    stage('System Test - Senaryo 4 (UI Smoke)') {
      steps {
        sh '''
          set -e
          # selenium-tests klasörün varsa oradan test koştur
          if [ -d "selenium-tests" ]; then
            cd selenium-tests
            chmod +x mvnw || true
            ./mvnw -q test
          else
            echo "selenium-tests klasörü yok, atlandı"
          fi
        '''
      }
      post {
        always {
          // ✅ Selenium test raporları (Jenkins’te görünür)
          junit testResults: 'selenium-tests/**/target/surefire-reports/*.xml',
                allowEmptyResults: true
        }
      }
    }

    stage('System Test - Senaryo 5 (Dashboard / API)') {
      steps {
        sh '''
          set -e
          echo "Senaryo 5 burada koşuyor"
        '''
      }
    }
  }

  post {
    always {
      sh '''
        set +e
        docker-compose down -v --remove-orphans || true
      '''
      // İstersen tüm raporları tek seferde de toplayabilirsin (opsiyonel)
      junit testResults: '**/target/surefire-reports/*.xml, **/target/failsafe-reports/*.xml, selenium-tests/**/target/surefire-reports/*.xml',
            allowEmptyResults: true
    }
  }
}
