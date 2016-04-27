package org.frameworkset.soa.list;

import java.util.ArrayList;
import java.util.List;

import org.frameworkset.soa.ObjectSerializable;
import org.junit.Test;

public class ListSerialTest {

	public ListSerialTest() {
		// TODO Auto-generated constructor stub
	}
	@Test
	public void test() throws Exception
	{
		org.frameworkset.soa.list.PackageInfoModel info = new PackageInfoModel();
		BaseInfoModel baseInfo = new BaseInfoModel();
		baseInfo.setBrand("asdaf");
		info.setBaseInfo(baseInfo);
		info.setRefbaseInfo(baseInfo);
		baseInfo = new BaseInfoModel();
		baseInfo.setBrand("otherasdaf");
		info.setOtherbaseInfo(baseInfo);
		info.setBigi(new Integer(2));
		info.setRefbigi(info.getBigi());
		info.setOtherbigi(new Integer(2));
		info.setSmalli(2);
		List<ComponentRelModel> componentRels = new ArrayList<ComponentRelModel>();
		
		List<PackageChannelRelModel> componentRels2 = new ArrayList<PackageChannelRelModel>();
		System.out.println(componentRels.hashCode() ==(componentRels2.hashCode()));
		System.out.println(componentRels.equals(componentRels2));
		ComponentRelModel dd = new ComponentRelModel();
		dd.setComponentId("");
//		componentRels.add(dd);
		info.setComponentRels(componentRels);
		info.setRefcomponentRels(componentRels);
		List<ComponentRelModel> otherComponentRels = new ArrayList<ComponentRelModel>();
		info.setOthercomponentRels(otherComponentRels);
		List<PackageChannelRelModel> packageChannelRels = new ArrayList<PackageChannelRelModel>();
		PackageChannelRelModel tt = new PackageChannelRelModel();
		tt.setEndDate("ddd");
		packageChannelRels.add(tt);
		info.setPackageChannelRels(packageChannelRels);
//		List<PackageGroupRefModel> packageGroupRefs = new ArrayList<PackageGroupRefModel>(); 
//		info.setPackageGroupRefs(packageGroupRefs);
//		List<PackageKindRelModel> packageKindRels = new ArrayList<PackageKindRelModel>();
//		info.setPackageKindRels(packageKindRels);
//		List<PackageProviderRefModel> packageProviderRefs = new ArrayList<PackageProviderRefModel>(); 
//		info.setPackageProviderRefs(packageProviderRefs);
//		List<PackageReleaseRelModel> packageReleaseRels = new ArrayList<PackageReleaseRelModel>(); 
//		info.setPackageReleaseRels(packageReleaseRels);
//		List<PackageResourceRefModel> packageResourceRefs = new ArrayList<PackageResourceRefModel>(); 
//		info.setPackageResourceRefs(packageResourceRefs);
//		List<PackageRightRelModel> packageRightRels = new ArrayList<PackageRightRelModel>();
//		info.setPackageRightRels(packageRightRels);
//		List<PackageTransRelModel> packageTransRels = new ArrayList<PackageTransRelModel>();
//		info.setPackageTransRels(packageTransRels);
//		List<PdGroupPackageModel> pdGroupPackages = new ArrayList<PdGroupPackageModel>(); 
//		PdGroupPackageModel f = new PdGroupPackageModel()
//				;
//		f.setDepart_name("dddd");
//		pdGroupPackages.add(f);
//		info.setPdGroupPackages(pdGroupPackages);
//		List<PdPackageRelModel> pdPackageRels = new ArrayList<PdPackageRelModel>(); 
//		PdPackageRelModel m = new PdPackageRelModel();
//		m.setDepart_name("dsadf");
//		pdPackageRels.add(m);
//		info.setPdPackageRels(pdPackageRels);
		String xml = ObjectSerializable.toXML(info);
		System.out.println(xml);
		PackageInfoModel test1_ =  ObjectSerializable.toBean(xml,PackageInfoModel.class);
		System.out.println("");
	}

}
