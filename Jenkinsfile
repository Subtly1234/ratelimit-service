pipeline {
	agent none
	stages {
		stage('Clone Code') {
			agent {
				label 'master'
			}
			steps {
			    sh 'curl "http://p.nju.edu.cn/portal_io/login" --data "username=191250081&password=lzj669657ABC"'
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
				sh 'mvn -B clean package -Dmaveen.test.skip=true'
			}
		}

		stage('Image Build') {
			agent {
				label 'master'
			}
			steps {
				echo "3.Image Build Stage"
				sh 'docker build -f Dockerfile --build-arg jar_name=target/ratelimit-service-0.0.1-SNAPSHOT.jar -t ratelimit-service:${BUILD_ID} . '
				sh 'docker tag ratelimit-service:${BUILD_ID} harbor.edu.cn/nju07/ratelimit-service:${BUILD_ID}'
			}
		}

		stage('Push') {
			agent {
				label 'master'
			}
			steps {
				echo "4.Push Docker Image Stage"
				sh "docker login --username=nju07 harbor.edu.cn -p nju072022"
				sh "docker push harbor.edu.cn/nju07/ratelimit-service:${BUILD_ID}"
			}
		}
	}
}


node('slave') {
	container('jnlp-kubectl') {
		stage('Clone YAML') {
		    sh 'curl "http://p.nju.edu.cn/portal_io/login" --data "username=191250081&password=lzj669657ABC"'
			echo "5. Git Clone YAML To Slave"
			git url: "https://github.com/Subtly1234/ratelimit-service.git"
		}

		stage('YAML') {
			echo "6. Change YAML File Stage"
			sh 'sed -i "s#{VERSION}#${BUILD_ID}#g" ./jenkins/scripts/ratelimit-service.yaml'
		}

		stage('Deploy') {
			echo "7. Deploy To K8s Stage"
			sh 'kubectl apply -f ./jenkins/scripts/ratelimit-service.yaml -n nju07'
			sh 'kubectl apply -f ./jenkins/scripts/ratelimit-serviceMonitor.yaml'
		}
	}
}
