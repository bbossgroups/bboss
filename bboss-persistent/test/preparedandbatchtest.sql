
create table td_reg_bank_acc_bak(
	   id number(10) primary key,
	   create_acc_time date,
	   starttime date,
	   endtime date 
)
/

alter table td_reg_bank_acc_bak add  clob1 clob;
alter table td_reg_bank_acc_bak add  clob2 clob;
alter table td_reg_bank_acc_bak add blob1 blob;
alter table td_reg_bank_acc_bak add BANK_ACC varchar2(500);

DROP SEQUENCE seq_bank_acc;

CREATE SEQUENCE seq_bank_acc
  START WITH 355
  MAXVALUE 999999999999999999999999999
  MINVALUE 0
  NOCYCLE
  CACHE 20
  NOORDER;
insert into tableinfo(table_name,table_id_name,table_id_increment,table_id_value,table_id_generator,table_id_type,table_id_prefix)
values('td_reg_bank_acc_bak','id',1,0,'seq_bank_acc','sequence',null);
