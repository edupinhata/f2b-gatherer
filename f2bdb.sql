#DATABASE OF FAIL2BAN 

CREATE DATABASE IF NOT EXISTS F2B_LOGS_DB;

USE F2B_LOGS_DB;


CREATE TABLE LOGS(
	LOG_DATE DATE,
	LOG_TIME TIME,
	FOLDER VARCHAR(50),
	PID VARCHAR(20),
	STATUS VARCHAR(10),
	FILTER_NAME VARCHAR(50),
	ACTION VARCHAR(20),
	IP VARCHAR(16)
)





