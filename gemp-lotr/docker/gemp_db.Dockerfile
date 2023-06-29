FROM mariadb:10.5 AS MariaDB

COPY ./database_script.sql /docker-entrypoint-initdb.d
COPY ./docker/initial_user_setup.sql /docker-entrypoint-initdb.d
