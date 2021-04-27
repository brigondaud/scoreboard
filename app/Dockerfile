FROM ubuntu:20.04

RUN apt-get update
RUN apt install -y curl
RUN apt install -y unzip
RUN apt install openjdk-8-jdk -y

RUN curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64-2.1.38.zip" -o "awscliv2.zip"

RUN unzip awscliv2.zip
RUN ./aws/install

RUN mkdir /scoreboard-service
WORKDIR /scoreboard-service
COPY . .
RUN chmod +x app/init.sh
ENTRYPOINT /scoreboard-service/app/init.sh