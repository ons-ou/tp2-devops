pipeline {
    agent any
    tools {
        jdk "jdk17"
        maven "maven"
    }
    environment {
        registry = "wwx2/tp2_devops"
        registryCredential = 'docker_hub'
        dockerImage = ''
    }
    stages {
        stage("Compile") {
            steps {
                script {
                    echo "Compiling..."
                    sh "mvn clean compile -DskipTests"
                }
            }
        }

        stage("Build") {
            steps {
                script {
                    echo "Building the JAR file..."
                    sh "mvn package -DskipTests"
                }
            }
        }

        stage("Test") {
            steps {
                script {
                    echo "Running tests..."
                    sh "mvn test -X"
                }
            }
        }

        stage("Code Quality Check") {
            steps {
                script {
                    echo "Running SonarQube Scanner..."
                    withSonarQubeEnv('SonarQube') { // Specify the server name configured in Jenkins
                        sh """
                            mvn verify sonar:sonar \
                            -Dsonar.projectKey=TP2_DevOps \
                            -Dsonar.projectName=TP2_DevOps
                        """
                    }
                }
            }
        }

        stage("Snyk Security Test") {
            steps {
                script {
                    echo 'Testing with Snyk...'
                    sh 'chmod +x ./mvnw'
                    sh './mvnw dependency:tree -DoutputType=dot --batch-mode --non-recursive --file="pom.xml"'
                }
                snykSecurity(
                    snykInstallation: 'snyk', // Ensure 'snyk' is configured as a tool in Jenkins
                    snykTokenId: 'snyk_cred2', // Ensure Snyk token is set up in Jenkins credentials
                    failOnIssues: false,
                    failOnError: false
                )
            }
        }

        stage("Building Docker Image") {
            steps {
                script {
                    echo "Building Docker image..."
                    dockerImage = docker.build("${registry}:${env.BUILD_NUMBER}")
                }
            }
        }

        stage("Upload Docker Image") {
            steps {
                script {
                    echo "Uploading Docker image to registry..."
                    docker.withRegistry('', registryCredential) {
                        dockerImage.push()
                    }
                    dockerImage.push("latest") // Push "latest" tag as well, if desired
                }
            }
        }
    }

    post {
        always {
            echo "Running post-build actions..."
            junit "target/surefire-reports/*.xml"
            cleanWs() // Clean workspace after the build
        }
    }
}
