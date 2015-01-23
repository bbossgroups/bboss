package org.frameworkset.web.demo;

import java.util.List;

import org.frameworkset.util.annotations.RequestParam;
import org.frameworkset.web.servlet.ModelMap;

public class SiteDemoController {
	private List<SiteDemoBean> demo_sites;
	public String index(ModelMap model)
	{		
		model.addAttribute("demobeans", demo_sites);
		return "index";
	}
	
	/**
	 * 这是一个权限检测失败测试控制器方法
	 * authorfailed.htm
	 * @param model
	 */
	public void authorfailed(ModelMap model)
	{		
		
//		return "index";
	}
	
	public String detail(ModelMap model,@RequestParam(name="demoname") String demoname)
	{
		SiteDemoBean bean = null;
		for(int i = 0; i < demo_sites.size(); i ++)
		{
			SiteDemoBean bean_ = demo_sites.get(i);
			if(demoname.equals(bean_.getName()))
			{
				bean = bean_;
				break;
			}
			
		}
		model.addAttribute("demobean", bean);
		
		return "seconddetail";
	}

	/**
	 * @return the demo_sites
	 */
	public List<SiteDemoBean> getDemo_sites() {
		return demo_sites;
	}

	/**
	 * @param demoSites the demo_sites to set
	 */
	public void setDemo_sites(List<SiteDemoBean> demoSites) {
		demo_sites = demoSites;
	}

}
