DROP USER 'root'@'%';
CREATE USER 'springApp'@'192.170.%.%' IDENTIFIED BY 'sohaco12345@';
CREATE USER 'remoteUser'@'%' IDENTIFIED BY 'sohaco12345@';

GRANT ALL PRIVILEGES ON *.* TO 'springApp'@'192.170.%.%';
GRANT ALL PRIVILEGES ON *.* TO 'remoteUser'@'sohaco12345@';
FLUSH PRIVILEGE;