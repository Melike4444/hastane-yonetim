pipeline {
  agent any

  environment {
    // Jenkins container içinde docker genelde /usr/bin altında
    DOCKER = "/usr/bin/docker"
    PATH = "/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin:/opt/homebrew/bin"
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

    stage('Sanity: Tools') {
      steps {
        sh '''
          set -e
          echo "PATH=$PATH"
          which java || true
          java -version || true
          chmod +x mvnw || true
          ./mvnw -v || true
          ${DOCKER} --version || true
          # Jenkins konteynerinde 'docker compose' olmayabilir, sorun değil
          ${DOCKER} compose version || true
        '''
      }
    }

    stage('Backend: Build') {
      steps {
        sh '''
          set -e
          chmod +x mvnw
          ./mvnw -B -DskipTests package
        '''
      }
    }

    stage('Backend: Unit Tests') {
      steps {
        sh '''
          set -e
          chmod +x mvnw
          # Sadece birim testler: IT'leri atlayalım
          ./mvnw -B -DskipITs=true test
        '''
      }
      post {
        always {
          junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
        }
      }
    }

    stage('Backend: Integration Tests') {
      steps {
        sh '''
          set -e
          chmod +x mvnw
          # Failsafe plugin ile *IT.java entegrasyon testlerini çalıştır
          ./mvnw -B -DskipITs=false verify
        '''
      }
      post {
        always {
          // Failsafe rapor klasörü
          junit allowEmptyResults: true, testResults: 'target/failsafe-reports/*.xml'
        }
      }
    }

    stage('Docker Up') {
      steps {
        sh '''
          set -e
          echo "Docker Up stage (CI): Jenkins konteynerinde 'docker compose' yok."
          echo "Bu aşamada sadece docker daemon'a erişimi ve çalışan container'ları gösteriyoruz."
          ${DOCKER} ps || true
        '''
      }
    }

    stage('Selenium Tests') {
      steps {
        sh '''
          set -e
          cd selenium-tests
          chmod +x ../mvnw
          # Headless + baseUrl ayarları already pom.xml içinde var
          ../mvnw -B test
        '''
      }
      post {
        always {
          junit allowEmptyResults: true, testResults: 'selenium-tests/target/surefire-reports/*.xml'
        }
      }
    }

  } // stages

  post {
    always {
      sh '''
        set +e
        echo "Post cleanup: docker down (CI'da docker compose yok, sadece log yazılıyor)"
        # Burada container kapatma işlemini lokalde manuel yapıyoruz,
        # Jenkins konteynerinde 'docker compose' olmadığı için komut çalıştırmıyoruz.
        true
      '''
    }
  }
}
