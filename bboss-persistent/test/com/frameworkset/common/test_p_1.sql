CREATE OR REPLACE PROCEDURE test_p(id in number,
	   	  		  				 name out varchar2
								 ,name1 out varchar2,test out number
								 ,nomatch out number) IS

/******************************************************************************
   NAME:       test
   PURPOSE:    

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        2008-10-27          1. Created this procedure.

   NOTES:

   Automatically available Auto Replace Keywords:
      Object Name:     test
      Sysdate:         2008-10-27
      Date and Time:   2008-10-27, 17:05:33, and 2008-10-27 17:05:33
      Username:         (set in TOAD Options, Procedure Editor)
      Table Name:       (set in the "New PL/SQL Object" dialog)

******************************************************************************/
BEGIN
   --tmpVar := 0;
   name := 'hello name';
   name1 := 'hello name1';
   test := id;
   --insert into test(id,name) values(SEQ_TEST.nextval,'name1');
   commit;
   
   
EXCEPTION
  WHEN NO_DATA_FOUND THEN
    NULL;
  WHEN OTHERS THEN
    -- Consider logging the error and then re-raise
    RAISE;
END test_p;
/