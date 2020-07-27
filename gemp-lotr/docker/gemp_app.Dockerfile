#FROM store/oracle/jdk:11 AS Java
# copy files manually from the FROM statement image into the new container
#FROM maven AS JavaBuild
#FROM mariadb AS MariaDB
#FROM httpd AS ApacheWebServer


	
	
#####################################################################
# The following is pulled from the openjdk dockerfile:
# https://github.com/docker-library/openjdk/blob/29c17de6c8c0df6304d0c80f569deea1d4b6a23e/14/jdk/oracle/Dockerfile
#####################################################################
	
	
#RUN apt-get update 
#	
#RUN set -eux; \
	#apt-get install -y \
		#gzip \
		#curl \
		#tar \
#
## jlink --strip-debug on 13+ needs objcopy: https://github.com/docker-library/openjdk/issues/351
## Error: java.io.IOException: Cannot run program "objcopy": error=2, No such file or directory
		#binutils \
## java.lang.UnsatisfiedLinkError: /usr/java/openjdk-12/lib/libfontmanager.so: libfreetype.so.6: cannot open shared object file: No such file or directory
## https://github.com/docker-library/openjdk/pull/235#issuecomment-424466077
		#libfreetype6  fontconfig \
	#; \
	#rm -rf /var/cache/yum
#
## Default to UTF-8 file.encoding
#ENV LANG en_US.UTF-8
#
#ENV JAVA_HOME /usr/java/openjdk-14
#ENV PATH $JAVA_HOME/bin:$PATH
#
## https://jdk.java.net/
## > Java Development Kit builds, from Oracle
#ENV JAVA_VERSION 14.0.2
#
#RUN set -eux; \
## amd64
	#downloadUrl=https://download.java.net/java/GA/jdk14.0.2/205943a0976c4ed48cb16f1043c5c647/12/GPL/openjdk-14.0.2_linux-x64_bin.tar.gz; \
	#downloadSha256=91310200f072045dc6cef2c8c23e7e6387b37c46e9de49623ce0fa461a24623d; \
#
	#curl -fkL -o /openjdk.tgz "$downloadUrl"; \
	#echo "$downloadSha256 */openjdk.tgz" | sha256sum -c -; \
#
	#mkdir -p "$JAVA_HOME"; \
	#tar --extract --file /openjdk.tgz --directory "$JAVA_HOME" --strip-components 1; \
	#rm /openjdk.tgz; \
#
## https://github.com/oracle/docker-images/blob/a56e0d1ed968ff669d2e2ba8a1483d0f3acc80c0/OracleJava/java-8/Dockerfile#L17-L19
	#ln -sfT "$JAVA_HOME" /usr/java/default; \
	#ln -sfT "$JAVA_HOME" /usr/java/latest; \
	#for bin in "$JAVA_HOME/bin/"*; do \
		#base="$(basename "$bin")"; \
		#[ ! -e "/usr/bin/$base" ]; \
		#update-alternatives --install "/usr/bin/$base" "$base" "$bin" 20000; \
	#done; \
#
## https://github.com/docker-library/openjdk/issues/212#issuecomment-420979840
## https://openjdk.java.net/jeps/341
	#java -Xshare:dump; \
#
## see "update-ca-trust" script which creates/maintains this cacerts bundle
	#rm -rf "$JAVA_HOME/lib/security/cacerts"; \
	#ln -sT /etc/pki/ca-trust/extracted/java/cacerts "$JAVA_HOME/lib/security/cacerts"; \
#
## basic smoke test
	#java --version; \
	#javac --version
#
## https://docs.oracle.com/javase/10/tools/jshell.htm
## https://docs.oracle.com/javase/10/jshell/
## https://en.wikipedia.org/wiki/JShell
##CMD ["jshell"]
#
######################################################################
#
#
#
######################################################################
## The following is pulled from the maven dockerfile:
## https://github.com/carlossg/docker-maven/blob/26ba49149787c85b9c51222b47c00879b2a0afde/openjdk-14/Dockerfile
######################################################################
#
#ARG MAVEN_VERSION=3.6.3
#ARG USER_HOME_DIR="/root"
#ARG SHA=c35a1803a6e70a126e80b2b3ae33eed961f83ed74d18fcd16909b2d44d7dada3203f1ffe726c17ef8dcca2dcaa9fca676987befeadc9b9f759967a8cb77181c0
#ARG BASE_URL=https://apache.osuosl.org/maven/maven-3/${MAVEN_VERSION}/binaries
#
#RUN mkdir -p /usr/share/maven /usr/share/maven/ref \
  #&& curl -fsSkL -o /tmp/apache-maven.tar.gz ${BASE_URL}/apache-maven-${MAVEN_VERSION}-bin.tar.gz \
  #&& echo "${SHA}  /tmp/apache-maven.tar.gz" | sha512sum -c - \
  #&& tar -xzf /tmp/apache-maven.tar.gz -C /usr/share/maven --strip-components=1 \
  #&& rm -f /tmp/apache-maven.tar.gz \
  #&& ln -s /usr/share/maven/bin/mvn /usr/bin/mvn
#
#ENV MAVEN_HOME /usr/share/maven
#ENV MAVEN_CONFIG "$USER_HOME_DIR/.m2"
#
##COPY mvn-entrypoint.sh /usr/local/bin/mvn-entrypoint.sh
##COPY settings-docker.xml /usr/share/maven/ref/
#
######################################################################


FROM alpine

RUN apk update; \
	apk update --repository=http://dl-cdn.alpinelinux.org/alpine/edge/testing; \
	apk add --no-cache procps; \
	apk add --no-cache net-tools; \
	apk add --no-cache iputils; \
	apk add --no-cache bash; \
	apk add --no-cache util-linux; \
	apk add --no-cache dpkg; \
	apk add --no-cache gzip; \
	apk add --no-cache curl; \
	apk add --no-cache tar; \
	apk add --no-cache binutils; \
	apk add --no-cache freetype; \
	apk add --no-cache fontconfig; \
	apk add --no-cache openjdk14-jdk --repository=http://dl-cdn.alpinelinux.org/alpine/edge/testing; 
		
		
		
#####################################################################
# The following is pulled from the maven dockerfile:
# https://github.com/carlossg/docker-maven/blob/26ba49149787c85b9c51222b47c00879b2a0afde/openjdk-14/Dockerfile
#####################################################################

ARG MAVEN_VERSION=3.6.3
ARG USER_HOME_DIR="/root"
ARG SHA=c35a1803a6e70a126e80b2b3ae33eed961f83ed74d18fcd16909b2d44d7dada3203f1ffe726c17ef8dcca2dcaa9fca676987befeadc9b9f759967a8cb77181c0
ARG BASE_URL=https://apache.osuosl.org/maven/maven-3/${MAVEN_VERSION}/binaries

RUN mkdir -p /usr/share/maven /usr/share/maven/ref \
  && curl -fsSkL -o /tmp/apache-maven.tar.gz ${BASE_URL}/apache-maven-${MAVEN_VERSION}-bin.tar.gz \
  && echo "${SHA}  /tmp/apache-maven.tar.gz" | sha512sum -c - \
  && tar -xzf /tmp/apache-maven.tar.gz -C /usr/share/maven --strip-components=1 \
  && rm -f /tmp/apache-maven.tar.gz \
  && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

ENV MAVEN_HOME /usr/share/maven
ENV MAVEN_CONFIG "$USER_HOME_DIR/.m2"

#COPY mvn-entrypoint.sh /usr/local/bin/mvn-entrypoint.sh
#COPY settings-docker.xml /usr/share/maven/ref/

#####################################################################


RUN apk add --no-cache openrc; \
	apk add --no-cache apache2
	
RUN nohup java -jar /etc/gemp-lotr/gemp-lotr-async/target/web.jar &

		# java prereqs
		#gzip \
		#curl \
		#tar \
		#libfreetype6  fontconfig \
		#openjdk14 --repository=http://dl-cdn.alpinelinux.org/alpine/edge/community
		
		# apache prereqs
		#bzip2 \
		#ca-certificates \
		#dirmngr \
		#dpkg-dev \
		#gcc \
		#gnupg \
		#libbrotli-dev \
		#libcurl4-openssl-dev \
		#libjansson-dev \
		#liblua5.2-dev \
		#libnghttp2-dev \
		#libpcre3-dev \
		#libssl-dev \
		#libxml2-dev \
		#make \
		#wget \
		#zlib1g-dev 
		

#####################################################################
# The following is pulled from the openjdk dockerfile:
# https://github.com/docker-library/openjdk/blob/29c17de6c8c0df6304d0c80f569deea1d4b6a23e/14/jdk/oracle/Dockerfile
#####################################################################


## Default to UTF-8 file.encoding
#ENV LANG en_US.UTF-8
#
#ENV JAVA_HOME /usr/java/openjdk-14
#ENV PATH $JAVA_HOME/bin:$PATH
#
## https://jdk.java.net/
## > Java Development Kit builds, from Oracle
#ENV JAVA_VERSION 14.0.2
#
#RUN set -eux; \
## amd64
	#downloadUrl=https://download.java.net/java/GA/jdk14.0.2/205943a0976c4ed48cb16f1043c5c647/12/GPL/openjdk-14.0.2_linux-x64_bin.tar.gz; \
	#downloadSha256=91310200f072045dc6cef2c8c23e7e6387b37c46e9de49623ce0fa461a24623d; \
#
	#curl -fkL -o /openjdk.tgz "$downloadUrl"; \
	#echo "$downloadSha256 */openjdk.tgz" | sha256sum -c -; \
#
	#mkdir -p "$JAVA_HOME"; \
	#tar --extract --file /openjdk.tgz --directory "$JAVA_HOME" --strip-components 1; \
	#rm /openjdk.tgz; 

## https://github.com/oracle/docker-images/blob/a56e0d1ed968ff669d2e2ba8a1483d0f3acc80c0/OracleJava/java-8/Dockerfile#L17-L19
	#ln -sfT "$JAVA_HOME" /usr/java/default; \
	#ln -sfT "$JAVA_HOME" /usr/java/latest; \
	#for bin in "$JAVA_HOME/bin/"*; do \
		#base="$(basename "$bin")"; \
		#[ ! -e "/usr/bin/$base" ]; \
		#update-alternatives --install "/usr/bin/$base" "$base" "$bin" 20000; \
	#done; \
#	

# https://github.com/docker-library/openjdk/issues/212#issuecomment-420979840
# https://openjdk.java.net/jeps/341
	#java -Xshare:dump; \

# see "update-ca-trust" script which creates/maintains this cacerts bundle
	#rm -rf "$JAVA_HOME/lib/security/cacerts"; \
	#ln -sT /etc/pki/ca-trust/extracted/java/cacerts "$JAVA_HOME/lib/security/cacerts"; 

# basic smoke test
	#java --version; \
	#javac --version

# https://docs.oracle.com/javase/10/tools/jshell.htm
# https://docs.oracle.com/javase/10/jshell/
# https://en.wikipedia.org/wiki/JShell
#CMD ["jshell"]

#####################################################################

		
		




#RUN yum install -y telnet && \
	#yum install -y iputils
#
#
#RUN yum install -y socat && \
	#socat TCP-LISTEN:3306,fork TCP:db:3306 & 
#RUN socat TCP-LISTEN:80,fork TCP:web:80 & 
#RUN yum install -y websocat && \
	#websocat WS-LISTEN:8080,fork WS:web:8080 & 
	
#RUN apt-get install -y websocat && \
#	websocat WS-LISTEN:8080,fork WS:build:8080 &





#WORKDIR /usr/java/
#COPY ./openjdk-14 /usr/java/openjdk-14
#RUN apt-get update && apt-get -y install git

#WORKDIR /etc/

#RUN git clone https://github.com/MarcinSc/gemp-lotr.git && \
	# The github files are all nested way too deep
	#cp -R /etc/gemp-lotr/gemp-lotr/* /etc/gemp-lotr && \
	#rm -rf /etc/gemp-lotr/gemp-lotr/
	
	
#WORKDIR /etc/gemp-lotr

#CMD java -jar /etc/gemp-lotr/gemp-lotr-async/target/web.jar &
#"/bin/bash"
#ENTRYPOINT ["/bin/bash"]

#ENV JAVA_HOME /usr/java/openjdk-14
# 1. Install tools that are needed to build your application.
# 2. Install dependencies, libraries and packages.
# 3. Build your application.


