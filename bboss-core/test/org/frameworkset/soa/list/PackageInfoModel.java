package org.frameworkset.soa.list;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PackageInfoModel implements Serializable{

	private static final long serialVersionUID = 2015082511301232L;
	
	private BaseInfoModel baseInfo;
	private int smalli = 0;
	public int getSmalli() {
		return smalli;
	}

	public void setSmalli(int smalli) {
		this.smalli = smalli;
	}

	public Integer getBigi() {
		return bigi;
	}

	public void setBigi(Integer bigi) {
		this.bigi = bigi;
	}

	private Integer bigi ;
	private Integer refbigi ;
	private Integer otherbigi ;
	public Integer getRefbigi() {
		return refbigi;
	}

	public void setRefbigi(Integer refbigi) {
		this.refbigi = refbigi;
	}

	public Integer getOtherbigi() {
		return otherbigi;
	}

	public void setOtherbigi(Integer otherbigi) {
		this.otherbigi = otherbigi;
	}

	public BaseInfoModel getBaseInfo() {
		return baseInfo;
	}

	public void setBaseInfo(BaseInfoModel baseInfo) {
		this.baseInfo = baseInfo;
	}

	public BaseInfoModel getRefbaseInfo() {
		return refbaseInfo;
	}

	public void setRefbaseInfo(BaseInfoModel refbaseInfo) {
		this.refbaseInfo = refbaseInfo;
	}

	public BaseInfoModel getOtherbaseInfo() {
		return otherbaseInfo;
	}

	public void setOtherbaseInfo(BaseInfoModel otherbaseInfo) {
		this.otherbaseInfo = otherbaseInfo;
	}

	public List<ComponentRelModel> getRefcomponentRels() {
		return refcomponentRels;
	}

	public void setRefcomponentRels(List<ComponentRelModel> refcomponentRels) {
		this.refcomponentRels = refcomponentRels;
	}

	public List<ComponentRelModel> getOthercomponentRels() {
		return othercomponentRels;
	}

	public void setOthercomponentRels(List<ComponentRelModel> othercomponentRels) {
		this.othercomponentRels = othercomponentRels;
	}

	public List<ComponentRelModel> getComponentRels() {
		return componentRels;
	}

	private BaseInfoModel refbaseInfo;
	private BaseInfoModel otherbaseInfo;
	private List<ComponentRelModel> componentRels;
	private List<ComponentRelModel> refcomponentRels;
	private List<ComponentRelModel> othercomponentRels;
	private List<PackageChannelRelModel> packageChannelRels;
	
//	private List<PackageGroupRefModel> packageGroupRefs;
//	
//	private List<PackageKindRelModel> packageKindRels;
//	
//	private List<PackageProviderRefModel> packageProviderRefs;
//	
//	private List<PackageReleaseRelModel> packageReleaseRels;
//	
//	private List<PackageResourceRefModel> packageResourceRefs;
//	
//	private List<PackageRightRelModel> packageRightRels;
//	
//	private List<PackageTransRelModel> packageTransRels;
//	
//	private List<PdGroupPackageModel> pdGroupPackages;
//	
//	private List<PdPackageRelModel> pdPackageRels;
	
	public PackageInfoModel() {
//		baseInfo = new BaseInfoModel();
//		componentRels = new ArrayList<ComponentRelModel>();
//		packageChannelRels = new ArrayList<PackageChannelRelModel>();
//		packageGroupRefs = new ArrayList<PackageGroupRefModel>(); 
//		packageKindRels = new ArrayList<PackageKindRelModel>();
//		packageProviderRefs = new ArrayList<PackageProviderRefModel>(); 
//		packageReleaseRels = new ArrayList<PackageReleaseRelModel>(); 
//		packageResourceRefs = new ArrayList<PackageResourceRefModel>(); 
//		packageRightRels = new ArrayList<PackageRightRelModel>();
//		packageTransRels = new ArrayList<PackageTransRelModel>();
//		pdGroupPackages = new ArrayList<PdGroupPackageModel>(); 
//		pdPackageRels = new ArrayList<PdPackageRelModel>(); 
	}

//	public BaseInfoModel getBaseInfo() {
//		return baseInfo;
//	}

//	public void setBaseInfo(BaseInfoModel baseInfo) {
//		this.baseInfo = baseInfo;
//	}
//
//	public List<ComponentRelModel> getComponentRels() {
//		return componentRels;
//	}

	public void setComponentRels(List<ComponentRelModel> componentRels) {
		this.componentRels = componentRels;
	}

	public List<PackageChannelRelModel> getPackageChannelRels() {
		return packageChannelRels;
	}

	public void setPackageChannelRels(
			List<PackageChannelRelModel> packageChannelRels) {
		this.packageChannelRels = packageChannelRels;
	}

//	public List<PackageGroupRefModel> getPackageGroupRefs() {
//		return packageGroupRefs;
//	}
//
//	public void setPackageGroupRefs(List<PackageGroupRefModel> packageGroupRefs) {
//		this.packageGroupRefs = packageGroupRefs;
//	}
//
//	public List<PackageKindRelModel> getPackageKindRels() {
//		return packageKindRels;
//	}
//
//	public void setPackageKindRels(List<PackageKindRelModel> packageKindRels) {
//		this.packageKindRels = packageKindRels;
//	}
//
//	public List<PackageProviderRefModel> getPackageProviderRefs() {
//		return packageProviderRefs;
//	}
//
//	public void setPackageProviderRefs(
//			List<PackageProviderRefModel> packageProviderRefs) {
//		this.packageProviderRefs = packageProviderRefs;
//	}
//
//	public List<PackageReleaseRelModel> getPackageReleaseRels() {
//		return packageReleaseRels;
//	}
//
//	public void setPackageReleaseRels(
//			List<PackageReleaseRelModel> packageReleaseRels) {
//		this.packageReleaseRels = packageReleaseRels;
//	}
//
//	public List<PackageResourceRefModel> getPackageResourceRefs() {
//		return packageResourceRefs;
//	}
//
//	public void setPackageResourceRefs(
//			List<PackageResourceRefModel> packageResourceRefs) {
//		this.packageResourceRefs = packageResourceRefs;
//	}
//
//	public List<PackageRightRelModel> getPackageRightRels() {
//		return packageRightRels;
//	}
//
//	public void setPackageRightRels(List<PackageRightRelModel> packageRightRels) {
//		this.packageRightRels = packageRightRels;
//	}
//
//	public List<PackageTransRelModel> getPackageTransRels() {
//		return packageTransRels;
//	}
//
//	public void setPackageTransRels(List<PackageTransRelModel> packageTransRels) {
//		this.packageTransRels = packageTransRels;
//	}
//
//	public List<PdGroupPackageModel> getPdGroupPackages() {
//		return pdGroupPackages;
//	}
//
//	public void setPdGroupPackages(List<PdGroupPackageModel> pdGroupPackages) {
//		this.pdGroupPackages = pdGroupPackages;
//	}
//
//	public List<PdPackageRelModel> getPdPackageRels() {
//		return pdPackageRels;
//	}
//
//	public void setPdPackageRels(List<PdPackageRelModel> pdPackageRels) {
//		this.pdPackageRels = pdPackageRels;
//	}
}
