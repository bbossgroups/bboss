package org.frameworkset.web.jquerypagine;

import java.sql.SQLException;

import org.frameworkset.util.annotations.PagerParam;
import org.frameworkset.util.annotations.RequestParam;
import org.frameworkset.web.demo.UserManagerException;
import org.frameworkset.web.demo.UserService;
import org.frameworkset.web.servlet.ModelMap;

import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.util.ListInfo;

public class JqueryPagineController {
	private UserService userService;
	public String main()
	{
		return "jquerypagine/main";
	}
	
	
	public String pagerqueryuser(@PagerParam(name=PagerParam.SORT ) String sortKey,
							@PagerParam(name=PagerParam.DESC,defaultvalue="true") boolean desc,
							@PagerParam(name=PagerParam.OFFSET) long offset,
							@PagerParam(name=PagerParam.PAGE_SIZE,defaultvalue="2") int pagesize,
							@RequestParam(name = "userName") String username ,
							@RequestParam(name = "name") String name ,		
							ModelMap model) throws UserManagerException {
//		try {
//			username = URLEncoder.encode(username,"UTF-8");
//		} catch (Exception e) {
//			
//			e.printStackTrace();
//		}
//		try {
//			if(username != null)
//			username = new String(username.getBytes("iso-8859-1"), "UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		ListInfo userTemp = userService.getUsersNullRowHandler(username,offset, pagesize);
		if(userTemp == null)
			model.getErrors().rejectValueWithErrorArgs("message", "user.not.exist",new Object[]{username});
		model.addAttribute("users", userTemp);
		return "jquerypagine/page";
	}
	
	public String pagerqueryuser1(@PagerParam(name=PagerParam.SORT ) String sortKey,
			@PagerParam(name=PagerParam.DESC,defaultvalue="true") boolean desc,
			@PagerParam(name=PagerParam.OFFSET) long offset,
			@PagerParam(name=PagerParam.PAGE_SIZE,defaultvalue="2") int pagesize,
			@RequestParam(name = "userName",decodeCharset = "UTF-8") String username ,
			ModelMap model) throws UserManagerException {

		ListInfo userTemp = userService.getUsersNullRowHandler(username,offset, pagesize);
		if(userTemp == null)
		model.getErrors().rejectValueWithErrorArgs("message", "user.not.exist",new Object[]{username});
		model.addAttribute("users", userTemp);
		return "jquerypagine/page1";
	}


	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	public String deleteusers(@RequestParam(name="id")int[] id){
		System.out.println(id.length);
		try {
			SQLExecutor.deleteByKeys("delete from tb_user where id=?", id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "redirect:jquerypagine/pagerqueryuser.htm";
	}
	

}
