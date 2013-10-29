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
import java.util.Map;

/**
 * <p>TreeUtil.java</p>
 * <p> Description: 构建树形的数据结构</p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2009 </p>
 * 
 * @Date Jun 14, 2009
 * @author biaoping.yin
 * @version 1.0
 */
public class TreeUtil {
	private static Map<String,TreeNode> treedatas = new HashMap();
	private static int maxlevel = 3;
	private static int nodes  =3;
	
	public static TreeNode getTreeNode(String id)
	{
		return treedatas.get(id);
	}
	public static TreeNode buildTreeDatas()
	{
		TreeNode root = treedatas.get("root");
		if(root != null )
			return root;
		synchronized(treedatas)
		{
			root = treedatas.get("root");
			if(root != null )
				return root;
			root = new TreeNode(null,"root","根节点",0);
			
			buildSonTreeDatas(root,1 );
			
			treedatas.put("root", root);
			return root;
		}
//		return treedatas.get("root");
		
	}
	
	public static void buildSonTreeDatas(TreeNode parent,int level )
	{
		if(level >maxlevel)
			return;
		for(int i = 0; i < nodes; i ++)
		{
			TreeNode node = new TreeNode(parent,parent.getUid() + "-" + i,parent.getUid() + "-" + i,level);
			buildSonTreeDatas(node,level + 1);
			parent.addNode(node);
			treedatas.put(parent.getUid() + "-" + i, node);
		}
		
	}

}
