oracle:
CREATE TABLE bboss_gencode
(
  id          VARCHAR2(40),
  tablename   VARCHAR2(20),
  dbname      VARCHAR2(40),
  CONTROLPARAMS      CLOB,
  FIELDINFOS      CLOB,
  author      VARCHAR2(40),
  company     VARCHAR2(40),
  createtime  TIMESTAMP(6),
  updatetime  TIMESTAMP(6)
)



ALTER TABLE bboss_gencode ADD (
  CONSTRAINT bboss_gencode_PK
  PRIMARY KEY
  (id));