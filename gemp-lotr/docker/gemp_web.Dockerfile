FROM httpd AS ApacheWebServer

RUN apt-get install -y socat && \
	socat TCP-LISTEN:80,fork TCP:build:80 & 
RUN apt-get install -y websocat && \
	websocat WS-LISTEN:8080,fork WS:build:8080 &
RUN apt-get update && \
	apt-get install -y watch && \
	apt-get install -y net-tools && \
	apt-get install -y iputils-ping && \
	apt-get install -y telnet
	


COPY ./httpd.conf /usr/local/apache2/conf/httpd.conf