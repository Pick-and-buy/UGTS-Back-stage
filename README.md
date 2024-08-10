# ULHTS-Back-stage

## Start application
`mvn spring-boot:run`

## Build application
`mvn clean package`

## Docker guideline
### Build docker image
`docker build -t <account>/ulhts:0.9.0 .`
### Push docker image to Docker Hub
`docker image push <account>/ulhts:0.9.0`
### Create network:
`docker network create ulhts-network`
### Start MySQL in devteria-network
`docker run --network ulhts-network --name postgres -p 5432:5432 -e POSTGRES_PASSWORD=admin -d postgres:14.5`
### Run your application in devteria-network
`docker run --name ulhts --network ulhts-network -p 8080:8080 -e DBMS_CONNECTION=jdbc:postgresql://localhost:5432/ugts_db ulhts:0.9.0`

## Install Docker on ubuntu

# Add Docker's official GPG key:
sudo apt-get update
sudo apt-get install ca-certificates curl
sudo install -m 0755 -d /etc/apt/keyrings
sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
sudo chmod a+r /etc/apt/keyrings/docker.asc

# Add the repository to Apt sources:
echo \
"deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu \
$(. /etc/os-release && echo "$VERSION_CODENAME") stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

sudo apt-get update

sudo apt-get install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

sudo docker run hello-world