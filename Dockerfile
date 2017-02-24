# https://github.com/adzerk-oss/boot-clj-docker-image/blob/master/Dockerfile
FROM debian:jessie
MAINTAINER Charlie Hebert <charlie.hebert92@gmail.com>

ENV DEBIAN_FRONTEND noninteractive

# Prevent apt-get from prompting for confirmation (say yes to all)
# http://superuser.com/questions/164553/automatically-answer-yes-when-using-apt-get-install
RUN echo 'APT::Get::Assume-Yes "true";' >> /etc/apt/apt.conf.d/90forceyes

# Oracle Java 8 and Boot

RUN apt-get update \
    && apt-get install curl wget openssl ca-certificates \
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

# App
COPY . /wordroot
WORKDIR /wordroot

# PhantomJS
RUN ./bin/debian/install-phantomjs.sh

# download & install deps, cache REPL and web deps
RUN /usr/bin/boot package

ENTRYPOINT ["java", "-jar", "target/project.jar"]