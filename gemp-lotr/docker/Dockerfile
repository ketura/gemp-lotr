#FROM store/oracle/jdk:11 AS Java
# copy files manually from the FROM statement image into the new container
FROM maven AS MavenJavaBuildTools
#FROM mariadb AS MariaDB
#FROM httpd AS ApacheWebServer

ENV LANG en_US.UTF-8

ENV JAVA_HOME /usr/java/openjdk-14
ENV PATH $JAVA_HOME/bin:$PATH
ENV JAVA_VERSION 14.0.2

ENV MAVEN_HOME /usr/share/maven
ENV MAVEN_CONFIG "$USER_HOME_DIR/.m2"

WORKDIR /usr/java/
COPY ./openjdk-14 /usr/java/openjdk-14
#RUN apt-get update && apt-get -y install git

#WORKDIR /etc/

#RUN git clone https://github.com/MarcinSc/gemp-lotr.git && \
	# The github files are all nested way too deep
	#cp -R /etc/gemp-lotr/gemp-lotr/* /etc/gemp-lotr && \
	#rm -rf /etc/gemp-lotr/gemp-lotr/
	
	
#WORKDIR /etc/gemp-lotr

CMD ["/bin/bash"]
#"/bin/bash"
#ENTRYPOINT ["/bin/bash"]

#ENV JAVA_HOME /usr/java/openjdk-14
# 1. Install tools that are needed to build your application.
# 2. Install dependencies, libraries and packages.
# 3. Build your application.
