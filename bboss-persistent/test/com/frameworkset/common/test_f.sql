CREATE OR REPLACE FUNCTION test_f(id number,name out varchar2,name1 out varchar2) RETURN NUMBER IS
tmpVar NUMBER;
/******************************************************************************
   NAME:       test_f
   PURPOSE:    

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        2008-10-27          1. Created this function.

   NOTES:

   Automatically available Auto Replace Keywords:
      Object Name:     test_f
      Sysdate:         2008-10-27
      Date and Time:   2008-10-27, 21:00:39, and 2008-10-27 21:00:39
      Username:         (set in TOAD Options, Procedure Editor)
      Table Name:       (set in the "New PL/SQL Object" dialog)

******************************************************************************/
BEGIN
   tmpVar := id;
   name := 'function name hello.';
   name1 := 'function name1 hello.';
   RETURN tmpVar;
   EXCEPTION
     WHEN NO_DATA_FOUND THEN
       NULL;
     WHEN OTHERS THEN
       -- Consider logging the error and then re-raise
       RAISE;
END test_f;



/