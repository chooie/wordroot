# https://github.com/adzerk-oss/boot-clj-docker-image/blob/master/Dockerfile
FROM debian:stretch
MAINTAINER Charlie Hebert <charlie.hebert92@gmail.com>

ENV DEBIAN_FRONTEND noninteractive


# Oracle Java 8 and Boot

RUN echo "deb http://packages.linuxmint.com debian import" >> /etc/apt/sources.list \
    && apt-get update \
    && apt-get install -y curl wget openssl ca-certificates \
    && cd /tmp \
    && wget -qO jdk8.tar.gz \
       --header "Cookie: oraclelicense=accept-securebackup-cookie" \
       http://download.oracle.com/otn-pub/java/jdk/8u112-b15/jdk-8u112-linux-x64.tar.gz \
    && tar xzf jdk8.tar.gz -C /opt \
    && mv /opt/jdk* /opt/java \
    && rm /tmp/jdk8.tar.gz \
    && update-alternatives --install /usr/bin/java java /opt/java/bin/java 100 \
    && update-alternatives --install /usr/bin/javac javac /opt/java/bin/javac 100 \
    && wget -O /usr/bin/boot https://github.com/boot-clj/boot-bin/releases/download/latest/boot.sh \
    && chmod +x /usr/bin/boot

ENV JAVA_HOME /opt/java

# SlimerJS

RUN apt-get --allow-unauthenticated install -y firefox

# App
COPY . /wordroot
WORKDIR /wordroot

EXPOSE 8000

# download & install deps, cache REPL and web deps
RUN /usr/bin/boot package

ENTRYPOINT ["java", "-jar", "target/project.jar"]