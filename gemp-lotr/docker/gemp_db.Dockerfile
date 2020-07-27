FROM mariadb AS MariaDB

#COPY ./database_script.sql /etc/gemp/database_script.sql
COPY ./database_script.sql /docker-entrypoint-initdb.d
