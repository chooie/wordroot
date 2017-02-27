# https://github.com/adzerk-oss/boot-clj-docker-image/blob/master/Dockerfile
FROM debian:jessie
MAINTAINER Charlie Hebert <charlie.hebert92@gmail.com>

ENV DEBIAN_FRONTEND noninteractive

# Prevent apt-get from prompting for confirmation (say yes to all)
# http://superuser.com/questions/164553/automatically-answer-yes-when-using-apt-get-install
RUN echo 'APT::Get::Assume-Yes "true";' >> /etc/apt/apt.conf.d/90forceyes

# Java 8 and Boot
ENV JAVA_HOME /opt/java
RUN \
  apt-get update && \
  apt-get install --no-install-recommends apt-utils \
                                          curl \
                                          wget \
                                          openssl \
                                          ca-certificates && \
  cd /tmp && \
  wget -qO jdk8.tar.gz \
       --header "Cookie: oraclelicense=accept-securebackup-cookie" \
       http://download.oracle.com/otn-pub/java/jdk/8u112-b15/jdk-8u112-linux-x64.tar.gz && \
  tar xzf jdk8.tar.gz -C /opt && \
  mv /opt/jdk* /opt/java && \
  rm /tmp/jdk8.tar.gz && \
  update-alternatives --install /usr/bin/java java /opt/java/bin/java 100 && \
  update-alternatives --install /usr/bin/javac javac /opt/java/bin/javac 100 && \

  wget -O /usr/bin/boot \
       https://github.com/boot-clj/boot-bin/releases/download/latest/boot.sh && \
  chmod +x /usr/bin/boot


# PhantomJS
ENV PHANTOMJS_VERSION 1.9.7
RUN \
  apt-get --no-install-recommends install git libfreetype6 libfontconfig bzip2 && \
  mkdir -p /srv/var && \
  wget -q --no-check-certificate -O /tmp/phantomjs-1.9.7-linux-x86_64.tar.bz2 https://bitbucket.org/ariya/phantomjs/downloads/phantomjs-1.9.7-linux-x86_64.tar.bz2 && \
  tar -xjf /tmp/phantomjs-1.9.7-linux-x86_64.tar.bz2 -C /tmp && \
  rm -f /tmp/phantomjs-1.9.7-linux-x86_64.tar.bz2 && \
  mv /tmp/phantomjs-1.9.7-linux-x86_64/ /srv/var/phantomjs && \
  ln -s /srv/var/phantomjs/bin/phantomjs /usr/bin/phantomjs && \
  git clone https://github.com/n1k0/casperjs.git /srv/var/casperjs && \
  ln -s /srv/var/casperjs/bin/casperjs /usr/bin/casperjs

# Clean up
RUN \
  apt-get autoremove && \
  apt-get clean all

# App
WORKDIR /wordroot

ENTRYPOINT ["./bin/build-and-run.sh"]