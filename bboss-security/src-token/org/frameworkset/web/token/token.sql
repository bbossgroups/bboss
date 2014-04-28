
-----------------------------------------------------------------------------
-- authtemptokens
-----------------------------------------------------------------------------
DROP TABLE authtemptokens CASCADE CONSTRAINTS;

CREATE TABLE authtemptokens
(
    ID VARCHAR2(100) NOT NULL,
    token VARCHAR2(100),
    signtoken VARCHAR2(1000),
    createTime NUMBER(20),
    lastVistTime NUMBER(20),
    livetime NUMBER(20),
    appid VARCHAR2(100),
    secret VARCHAR2(100),
    validate_ VARCHAR2(10)
);

ALTER TABLE authtemptokens
    ADD CONSTRAINT authtemptokens_PK
PRIMARY KEY (ID);






-----------------------------------------------------------------------------
-- dualtokens
-----------------------------------------------------------------------------
DROP TABLE dualtokens CASCADE CONSTRAINTS;

CREATE TABLE dualtokens
(
    ID VARCHAR2(100) NOT NULL,
    token VARCHAR2(100),
    signtoken VARCHAR2(1000),
    createTime NUMBER(20),
    lastVistTime NUMBER(20),
    livetime NUMBER(20),
    appid VARCHAR2(100),
    secret VARCHAR2(100),
    validate_ VARCHAR2(10)
);

ALTER TABLE dualtokens
    ADD CONSTRAINT dualtokens_PK
PRIMARY KEY (ID);






-----------------------------------------------------------------------------
-- temptokens
-----------------------------------------------------------------------------
DROP TABLE temptokens CASCADE CONSTRAINTS;

CREATE TABLE temptokens
(
    ID VARCHAR2(100) NOT NULL,
    token VARCHAR2(1000),
    createTime NUMBER(20),
    livetime NUMBER(20),
    validate_ VARCHAR2(10)
);

ALTER TABLE temptokens
    ADD CONSTRAINT temptokens_PK
PRIMARY KEY (ID);






-----------------------------------------------------------------------------
-- eckeypairs
-----------------------------------------------------------------------------
DROP TABLE eckeypairs CASCADE CONSTRAINTS;

CREATE TABLE eckeypairs
(
    appid VARCHAR2(100) NOT NULL,
    privateKey VARCHAR2(1500),
    createTime NUMBER(20),
    publicKey VARCHAR2(1500)
);

ALTER TABLE eckeypairs
    ADD CONSTRAINT eckeypairs_PK
PRIMARY KEY (appid);






-----------------------------------------------------------------------------
-- tickets
-----------------------------------------------------------------------------
DROP TABLE tickets CASCADE CONSTRAINTS;

CREATE TABLE tickets
(
    token VARCHAR2(100) NOT NULL,
    ticket VARCHAR2(1500),
    createTime NUMBER(20),
    livetime NUMBER(20),
    appid VARCHAR2(100),
    lastVistTime NUMBER(20)
);

ALTER TABLE tickets
    ADD CONSTRAINT tickets_PK
PRIMARY KEY (token);















