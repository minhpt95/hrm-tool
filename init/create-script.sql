DROP USER 'root'@'%';
CREATE USER 'springApp'@'192.170.%.%' IDENTIFIED BY 'password123@';
CREATE USER 'remoteUser'@'%' IDENTIFIED BY 'password123@';

GRANT ALL PRIVILEGES ON *.* TO 'springApp'@'192.170.%.%';
GRANT ALL PRIVILEGES ON *.* TO 'remoteUser'@'%';
FLUSH PRIVILEGE;