import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import com.frameworkset.common.poolman.DBUtil;


public class TestDatabaseMetaData {
	public static void main(String[] args) throws SQLException
	{
		Connection con = DBUtil.getConection ();
		DatabaseMetaData dbmd = con.getMetaData();
      if (dbmd.supportsNamedParameters() == true)
      {
          System.out.println("NAMED PARAMETERS FOR CALLABLE"
                            + "STATEMENTS IS SUPPORTED");
      }

	}

}
