package test.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * <p>TreeNode.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2009 </p>
 * 
 * @Date Jun 14, 2009
 * @author biaoping.yin
 * @version 1.0
 */
public class TreeNode {
	TreeNode parent;
	List<TreeNode> sons = new ArrayList();
	String uid;
	String name;
	int level;
	public TreeNode(TreeNode parent, String uid, String name,int level) {
		super();
		this.parent = parent;
		this.uid = uid;
		this.name = name;
		this.level = level;
	}
	public void addNode(TreeNode node)
	{
		this.sons.add(node);
	}
	public TreeNode getParent() {
		return parent;
	}
	public void setParent(TreeNode parent) {
		this.parent = parent;
	}
	public List<TreeNode> getSons() {
		return sons;
	}
	public void setSons(List<TreeNode> sons) {
		this.sons = sons;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean  hasSon()
	{
		return this.sons.size() > 0;
	}
    public int getLevel()
    {
        return level;
    }
    public void setLevel(int level)
    {
        this.level = level;
    }
	
	
	

}
