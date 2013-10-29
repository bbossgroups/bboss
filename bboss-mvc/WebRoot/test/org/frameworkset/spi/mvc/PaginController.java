package org.frameworkset.spi.mvc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.frameworkset.util.annotations.PagerParam;
import org.frameworkset.util.annotations.RequestParam;
import org.frameworkset.web.servlet.ModelAndView;
import org.frameworkset.web.servlet.ModelMap;

import test.pager.TableInfo;

import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.util.ListInfo;

/**
 * 
 * @author Administrator
 *
 */
public class PaginController {
	
	/**
	 * http://localhost:8080/bboss-mvc/pager/firstpagerdemo.html
	 * @param sortKey
	 * @param desc
	 * @param offset
	 * @param pagesize
	 * @return
	 */
	public ModelAndView firstpagerdemo(@PagerParam(name=PagerParam.SORT ) String sortKey,
			@PagerParam(name=PagerParam.DESC,defaultvalue="true") boolean desc,
			@PagerParam(name=PagerParam.OFFSET) long offset,
			@PagerParam(name=PagerParam.PAGE_SIZE,defaultvalue="2") int pagesize,
			@RequestParam(name="TABLE_NAME") String tablename
			
			)
	{			
		String sql = "select * from tableinfo";
		boolean usecondition = tablename != null && !tablename.equals("");
		if(usecondition)
			sql += " where TABLE_NAME like ?";
		
		ModelAndView view = new ModelAndView("/pager/pagerdemo");
		try {
			ListInfo datas = null;
			if(usecondition)
			{
				datas = SQLExecutor.queryListInfo(TableInfo.class, sql, offset, pagesize, "%" + tablename + "%");
			}
			else
			{
				datas = SQLExecutor.queryListInfo(TableInfo.class, sql, offset, pagesize);
			}
			view.addObject("pagedata", datas);
		
//			datas.setMaxPageItems(pagesize);
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		return view;
	}
	
	/**
	 * http://localhost:8080/bboss-mvc/pager/pagerdemo.html
	 * @param sortKey
	 * @param desc
	 * @param offset
	 * @param pagesize
	 * @return
	 */
	public ModelAndView pagerdemo(@PagerParam(name=PagerParam.SORT ) String sortKey,
			@PagerParam(name=PagerParam.DESC,defaultvalue="true") boolean desc,
			@PagerParam(name=PagerParam.OFFSET) long offset,
			@PagerParam(name=PagerParam.PAGE_SIZE,defaultvalue="2") int pagesize,
			@RequestParam(name="TABLE_NAME") String tablename,
			PageContext context,
			ModelMap model
			)
	{			
		String sql = "select * from tableinfo";
		boolean usecondition = tablename != null && !tablename.equals("");
		if(usecondition)
			sql += " where TABLE_NAME like ?";
		ListInfo datas = new ListInfo();
		PreparedDBUtil db = new PreparedDBUtil();
		try {
			db.preparedSelect(sql,offset,pagesize);
			if(usecondition)
				db.setString(1, "%" + tablename + "%");
			List<TableInfo> tables = db.executePreparedForList(TableInfo.class);
			
			datas.setTotalSize(db.getTotalSize());//设置总记录数
			datas.setDatas(tables);//设置当页数据
//			datas.setMaxPageItems(pagesize);
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		ModelAndView view = new ModelAndView("/pager/pagerdemo","pagedata", datas);
		return view;
	}
	
	public void testcn(HttpServletResponse response)
	{
		try {
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().print("中文");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
