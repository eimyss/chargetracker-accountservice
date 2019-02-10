podTemplate(label: 'mypod', containers: [
    containerTemplate(name: 'git', image: 'alpine/git', ttyEnabled: true, command: 'cat'),
    containerTemplate(name: 'maven', image: 'maven:3.3.9-jdk-8-alpine', command: 'cat', ttyEnabled: true),
    containerTemplate(name: 'docker', image: 'docker', command: 'cat', ttyEnabled: true),
    containerTemplate(name: 'kubectl', image: 'jorgeacetozi/jenkins-slave-kubectl', command: 'cat', ttyEnabled: true)
],
    volumes: [
        hostPathVolume(mountPath: '/var/run/docker.sock', hostPath: '/var/run/docker.sock'),
    ]
) {
    node('mypod') {
        container('kubectl') {
            stage('Kube stuff') {
                withCredentials([string(credentialsId: 'kube-ca', variable: 'PW1')]) {
                    withKubeConfig(caCertificate: '${PW1}', clusterName: 'kubernetes', contextName: 'jenkins', credentialsId: '37047961-56be-4bf1-a36e-f4651a546fe4', serverUrl: 'https://192.168.123.243:6443') {
                        sh 'kubectl --insecure-skip-tls-verify="true"  get pods'
                    }
                }


            }
        }
        stage('Clone repository') {
            container('git') {
                sh 'git clone -b kubernetes https://github.com/eimyss/chargetracker-accountservice.git'
            }
        }
        stage('Maven Build') {
            container('maven') {
                dir('chargetracker-accountservice/') {
                    sh 'mvn clean install -Dmaven.test.skip'
                }
            }
        }

        stage('Check running containers') {
            container('docker') {
                // example to show you can run docker commands when you mount the socket
                sh 'docker ps'
                echo 'build nr is: $BUILD_NUMBER'
                dir('chargetracker-accountservice/') {
                    // sh 'docker build . -t eimyss/expenses-account:1.0.1'
                    sh 'docker build . -t eimyss/expenses-account:1.$BUILD_NUMBER'
                }
                withCredentials([string(credentialsId: 'docker-pass', variable: 'PW1')]) {
                    echo "My password is '${PW1}'!"
                    sh 'docker login -u=testuser -p=${PW1} docker-registry.eimantas.server'
                }
                sh 'docker tag eimyss/expenses-account:1.$BUILD_NUMBER docker-registry.eimantas.server/expenses-account:1.$BUILD_NUMBER'
                //  sh 'docker tag eimyss/expenses-account:1.0.1 docker-registry.eimantas.server/expenses-account:1.0.1'
                //sh 'docker login -u=testuser -p=testpassword docker-registry.eimantas.server'
                sh 'docker push docker-registry.eimantas.server/expenses-account:1.$BUILD_NUMBER'
                // sh 'docker push docker-registry.eimantas.server/expenses-account:1.0.1'
            }
        }
    }
}
