pipeline {
  agent any
  options { timestamps() }

  stages {
    stage('Checkout') {
      steps { checkout scm }
    }

    stage('Build & Test') {
      steps {
        sh 'chmod +x mvnw || true'
        sh './mvnw -v'
        sh './mvnw clean test'
        sh './mvnw -DskipTests package'
      }
    }

    stage('JUnit Report') {
      steps {
        junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
      }
    }

    stage('Archive Jar') {
      steps {
        archiveArtifacts artifacts: 'target/*.jar', fingerprint: true, allowEmptyArchive: false
      }
    }
  }
}

