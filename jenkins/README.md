# Jenkins 분산 빌드

## 실행

```sh
cd master-slave
docker-compose up -d
```

```sh
docker-compose logs -f
```

### Master 노드에서 SSH 키 생성

```sh
docker exec -it jenkins-master mkdir /var/jenkins_home/.ssh
docker exec -it jenkins-master ssh-keygen -t ed25519 -f /var/jenkins_home/.ssh/jenkins_ed25519 -C "imcxsu@gmail.com" -N "markruler"
```

```sh
docker exec -it jenkins-master cat /var/jenkins_home/.ssh/jenkins_ed25519
-----BEGIN OPENSSH PRIVATE KEY-----
b3BlbnNzaC1rZXktdjEAAAAACmFlczI1Ni1jdHIAAAAGYmNyeXB0AAAAGAAAABAzr/2kkV
YC0lROXskSlRsuAAAAEAAAAAEAAAAzAAAAC3NzaC1lZDI1NTE5AAAAICwVRtUI9bMYxF0z
uAt51/2ogofRxLbui46vnfuAygtXAAAAoMt7LB2BF3wI0Yenh+A26OVcdIlUyg0Cd99v+1
ieWtT4ZnGQ7GoC/QBFMzcOypvGEwSWTk36sDFsdsuM+tWuqp5oFAE7TCwA0HaCo//J8IFf
I8wwCOMkzq4lIWRNbVpIr9XbmboqH6mW2Fp7XLya0noV09v3TclvTk0msqMtqPquZxOGEh
3ZKVYfw3N+OosWkKl6SlVNHMsq0VI8zIdoAJk=
-----END OPENSSH PRIVATE KEY-----
```

```sh
docker exec -it jenkins-master cat /var/jenkins_home/.ssh/jenkins_ed25519.pub
# ssh-ed25519 AAAAC3NzaC1lZDI1NTE5AAAAICwVRtUI9bMYxF0zuAt51/2ogofRxLbui46vnfuAygtX imcxsu@gmail.com
```

### 종료 후 Agent 노드에 Public Key 설정

```sh
docker-compose down
```

```yaml
jenkins-agent:
  # ...
  environment:
    JENKINS_AGENT_SSH_PUBKEY: "ssh-ed25519 AAAAC3NzaC1lZDI1NTE5AAAAICwVRtUI9bMYxF0zuAt51/2ogofRxLbui46vnfuAygtX imcxsu@gmail.com"
```

### 재실행

```sh
docker-compose up -d
```

```sh
docker exec -it jenkins-agent sh -c "printenv | grep JENKINS_AGENT_SSH_PUBKEY"
# JENKINS_AGENT_SSH_PUBKEY=ssh-ed25519 AAAAC3NzaC1lZDI1NTE5AAAAICwVRtUI9bMYxF0zuAt51/2ogofRxLbui46vnfuAygtX imcxsu@gmail.com
docker exec -it jenkins-agent sh -c "printenv | grep JENKINS_AGENT_HOME"
# JENKINS_AGENT_HOME=/home/jenkins
docker exec -it jenkins-agent sh -c "printenv | grep JAVA_HOME"
# JAVA_HOME=/usr/local/openjdk-11
```

`/home/jenkins/.ssh/authorized_keys` 파일을 직접 생성해도 된다.

```sh
docker exec -it jenkins-agent mkdir /home/jenkins/.ssh
docker exec -it jenkins-agent cat /home/jenkins/.ssh/authorized_keys
# ssh-ed25519 AAAAC3NzaC1lZDI1NTE5AAAAICwVRtUI9bMYxF0zuAt51/2ogofRxLbui46vnfuAygtX imcxsu@gmail.com
```

## 에이전트 연결

### 플러그인 설치

- [Locale](https://plugins.jenkins.io/locale/)
  - Manage Jenkins > Configure System > Locale > `en`
  - [x] Ignore browser preference and force this language to all users
- [SSH Build Agents](https://plugins.jenkins.io/ssh-slaves/)

### 노드 추가

> Manage Jenkins > Manage Nodes and Clouds > New Node

![Jenkins Node](../images/jenkins-node-config.png)

- Remote root directory: `/home/jenkins`
- Launch method
  - Launch agents via SSH
  - Host: jenkins-agent
  - Port: 22
  - Credentials: `SSH Username with private key` 생성 (master 노드의 private key, username:jenkins, Passphrase:markruler)
  - Host Key Verification Strategy: `Non verifying Verification Strategy`
  - JavaPath: `/usr/local/openjdk-11/bin/java`
  - Number of executors: 원하는 개수만큼

## 정리

```sh
docker-compose down
# docker volume prune -f
```
