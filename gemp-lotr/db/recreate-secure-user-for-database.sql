
DROP USER 'gempuser'@'localhost';

#Once you have ran this script, make sure that you alter the credentials used in gemp-lotr-common/.../gemp-lotr.properties, or else the server will crash on startup.
#It is highly recommended you run this script (obviously with a better user/password) on any production database.  The default one is in the code base, after all.
#Also recommend once you do so to avoid checking in the gemp-properties into git, which can be performed with the following command:
# git update-index --assume-unchanged ./gemp-lotr/gemp-lotr-common/src/main/resources/gemp-lotr.properties
CREATE USER 'some-other-username'@'172.28.1.2' IDENTIFIED BY 'a-really-secure-password';
GRANT USAGE, SELECT, INSERT, UPDATE, DELETE ON gemp_db.* TO 'some-other-username'@'172.28.1.2' WITH GRANT OPTION;
FLUSH PRIVILEGES;