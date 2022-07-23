pipeline {
	agent none
	stages {
		stage('Clone Code') {
			agent {
				label 'master'
			}
			steps {
				echo "1.Git Clone Code"
				git url: "https://github.com/Subtly1234/ratelimit-service.git"
			}
		}

		stage('Maven Build') {
			agent {
				docker {
					image 'maven:latest'
					args '-v /root/.m2:/root/.m2'
				}
			}
			steps {
				echo "2.Maven Build Stage"
				sh 'mvn -B clan package -Dmaveen.test.skip=true'
			}
		}

		stage('Image Build') {
			agent {
				label 'master'
			}
			steps {
				echo "3.Image Build Stage"
				sh 'docker build -f Dockerfile --build-arg jar_name=target/ratelimit-service-0.0.1-SNAPSHOT.jar -t ratelimit-service:${BUILD_ID} . '
				sh 'docker tag ratelimit-service:${BUILD_ID} 172.29.4.26/library/ratelimit-service:${BUILD_ID}'
			}
		}

		stage('Push') {
			agent {
				label 'master'
			}
			steps {
				echo "4.Push Docker Image Stage"
				sh "docker login --username=nju07 172.29.4.26 -p nju072022"
				sh "docker push 172.29.4.26/library/ratelimit-service:${BUILD_ID}"
			}
		}
	}
}


node('slave') {
	container('jnlp-kubectl') {
		stage('Clone YAML') {
			echo "5. Git Clone YAML To Slave"
			git url: "https://github.com/Subtly1234/ratelimit-service.git"
		}

		stage('YAML') {
			echo "6. Change YAML File Stage"
			sh 'sed -i "s#{VERSION}#${BUILD_ID}#g" ./jenkins/scripts/ratelimit-service.yaml'
		}

		stage('Deploy') {
			echo "7. Deploy To K8s Stage"
			sh 'kubectl apply -f ./jenkins/scripts/ratelimit-service.yaml'
		}
	}
}