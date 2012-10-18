package test;
import java.sql.SQLException;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;


public class TestDataInfo extends DataInfoImpl {

	protected ListInfo getDataList(String sortKey, boolean desc) {
		ListInfo info = new ListInfo();
		DBUtil dbUtil = new DBUtil();
		try {
			dbUtil.executeSelect("select * from tableinfo ");
			info.setArrayDatas(dbUtil.getAllResults());
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return info;
	}

	protected ListInfo getDataList(String sortKey, boolean desc, long offSet,
			int pageItemsize) {
		
		ListInfo info = new ListInfo();
		DBUtil dbUtil = new DBUtil();
		try {
			dbUtil.executeSelect("select * from tableinfo ",offSet,pageItemsize);
			info.setArrayDatas(dbUtil.getAllResults());
			info.setTotalSize(dbUtil.getLongTotalSize());
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return info;
	}

}
