/**
 *  Copyright 2008 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.  
 */

package test.tree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.PageContext;

import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;

/**
 * 
 * <p>TestTree.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2009 </p>
 * 
 * @Date Jun 14, 2009
 * @author biaoping.yin
 * @version 1.0
 */
public class TestTree extends COMTree {
	
	
	@Override
	public void setPageContext(PageContext pageContext) {
		
		super.setPageContext(pageContext);
		TreeUtil.buildTreeDatas();
	}

	@Override
	public boolean hasSon(ITreeNode parent) {
		String uid = parent.getId();
		TreeNode node = TreeUtil.getTreeNode(uid);
		if(node.hasSon())
			return true;
		return false;
	}

	@Override
	public boolean setSon(ITreeNode parent, int curLevel) {
		String uid = parent.getId();
		TreeNode node = TreeUtil.getTreeNode(uid);
		List<TreeNode> nodes = node.getSons();
		
		/**
		 * 在添加每个节点时将节点的所有参数放置到一个Map对象中，但是以下的几个名称是树中保留的参数名称，不能作为其他参数：
			node_recursive:指定特定树节点的复选框是否具有递归选择的功能，值类型Boolean
			node_uprecursive:指定特定树节点的复选框是否具有递归选择上级复选框的功能，值类型Boolean
			node_partuprecursive：boolean值，指定特定节点的复选框是否具有部分递归
			node_checkboxname：单独指定节点前的的复选框的名称
			nodeLink:指定每个树节点的链接地址，可以是javascript函数，也可以是超链接
			node_linktarget：指定每个节点链接地址弹出窗口名称
			node_radioname：定义每个节点单选框名称
			node_checkboxchecked :boolean值 标识复选框是否选中
			node_checkboxdisabled：boolean值 标识节点前的复选框是否被禁用
			node_radiochecked：boolean值 标识节点前的单选按钮是否被选中
			node_radiodisabled：boolean值 标识节点前的单选按钮是否被禁用
		 */
//		Map<paramname,paramvalue> params = new HashMap();
		for(int i = 0; i < nodes.size(); i ++)
		{
		    Map params = new HashMap();
		    
			TreeNode son = nodes.get(i);
			if(son.level == 0)
			    params.put("node_linktarget", "#outline");
			else if(son.level == 1)
                params.put("node_linktarget", "#details");
			else if(son.level == 2)
                params.put("node_linktarget", "#footer");
			else
			{
			    params.put("node_linktarget", "#footer");
			    params.put("nodeLink", "javascript:alert('yes');");
			}
			
			//设置节点默认选中：
			
			params.put("node_checkboxchecked", new Boolean(true));
			
			    
			addNode(parent,
					son.getUid(), //treeid 
					son.getUid(), //tree node name
	                null,// node type
	                true, //show href,true 时树节点将带超链接，false时不带超链接
	                curLevel, //current level
	                (String) null,//备注
	                (String) son.getUid(), //radio value,单选框按钮
	                (String) son.getUid(), //复选框的值
	                params  //为节点连接指定url的参数<paramname,paramvalue>
	                );
		}
		return true;
	}

}
