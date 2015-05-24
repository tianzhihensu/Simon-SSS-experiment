package com.simon.our.service;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.simon.our.pojo.TreeNode;
import com.simon.our.util.Constant;

/**
 * 依据解析xml文档，生成树结构
 * @author SSS
 *
 */
public class TreeConstruction {

	TreeOperation treeOperation = null;
	/**
	 * 遍历XML文档，这是一个递归的过程。
	 * 另外需要注意的一点是：每次运行本函数的时候，如果检查到所有的子节点都是叶节点，那就说明这个父节点一定是“顺序”的非叶节点
	 * 整体来看，整个结构图就是一个顺序的。
	 * 终止条件如下：
	 * 	1、该子树的所有节点都是叶子节点，则添加完所有的子节点后，返回。
	 * @param treeNode	一棵树的当前节点
	 * @param element	待解析的子树
	 */
	public void traverseXMLDocument(TreeNode treeNode, Element element) {
		NodeList nodesList = element.getChildNodes();
		if (isAllLeafNodes(nodesList)) {
			System.out.println("该节点的所有子节点都是叶节点。");
			treeOperation = new TreeOperation();
			treeOperation.addNodeAsChild(treeNode, nodesList);
			return;
		}
		
		treeOperation = new TreeOperation();
		
		// 这里,如果子节点中存在叶节点，就在下面的循环外新增加一个“顺序”的非叶节点，用来保存当前节点的子节点当中所有的叶节点
		TreeNode sequForLeafTreeNode = new TreeNode();
		if (isHasLeafNode(nodesList)) {
			sequForLeafTreeNode = treeOperation.initSequTreeNode(treeNode, null);
			treeOperation.addChildToTreeNode(treeNode, sequForLeafTreeNode);
		}
		
		// 如果不全都是leaf，则说明还有下一层的节点，即存在非叶节点
		for (int i = 0; i < nodesList.getLength(); i++) {	// 遍历当前层次下的各个子节点
			Node XMLNode = nodesList.item(i);
			
			if (XMLNode != null && XMLNode.getNodeType() == Node.ELEMENT_NODE) {
				if (XMLNode.getNodeName().equals(Constant.leafName)) {	//叶节点
					TreeNode leafTreeNode = treeOperation.initLeafTreeNode(treeNode, XMLNode);
					treeOperation.addChildToTreeNode(sequForLeafTreeNode, leafTreeNode);
					
				} else if (XMLNode.getNodeName().equals(Constant.concurrenyName)) {	// “并发”的非叶节点
					// 先创建一个树的“并发”非叶节点
					TreeNode concurTreeNode = treeOperation.initConcurTreeNode(treeNode);
					// 将此“并发”非叶节点添加到子节点列表中
					treeOperation.addChildToTreeNode(treeNode, concurTreeNode);
					
					Element concurElement = (Element) XMLNode;
					traverseXMLDocument(concurTreeNode, concurElement);	// “并发”的递归
					
				} else if (XMLNode.getNodeName().equals(Constant.sequenceName)) {		// “顺序”的非叶节点
					// 先创建一个树的“顺序”非叶节点
					TreeNode sequTreeNode = treeOperation.initSequTreeNode(treeNode, XMLNode);
					// 将此“顺序”非叶节点添加到子节点列表中
					treeOperation.addChildToTreeNode(treeNode, sequTreeNode);
					
					Element sequElement = (Element) XMLNode;
					traverseXMLDocument(sequTreeNode, sequElement);
					
				} else if (XMLNode.getNodeName().equals(Constant.selectionName)) {	// “选择”的非叶节点
					// 先创建一个树的“选择”非叶节点
					TreeNode seleTreeNode = treeOperation.initSeleTreeNode(treeNode);
					// 将此“选择”非叶节点添加到子节点列表中
					treeOperation.addChildToTreeNode(treeNode, seleTreeNode);
					
					Element seleElement = (Element) XMLNode;
					traverseXMLDocument(seleTreeNode, seleElement);
					
				} else if (XMLNode.getNodeName().equals(Constant.loopName)) {
					// 先创建一个树的“循环”非叶节点
					TreeNode loopTreeNode = treeOperation.initLoopTreeNode(treeNode, XMLNode);
					// 将此“选择”非叶节点添加到子节点列表中
					treeOperation.addChildToTreeNode(treeNode, loopTreeNode);
					
					Element loopElement = (Element) XMLNode;
					traverseXMLDocument(loopTreeNode, loopElement);
					
				}
			}
		}
		
	}
	
	/**
	 * 遍历该有序集合中是否都是叶子节点，判断标准就是该节点的getNodeName()函数的返回值
	 * @param nodesList
	 * @return
	 */
	public Boolean isAllLeafNodes(NodeList nodesList) {
		Boolean flag = true;	// 默认该NodeList全部是叶子节点
		for (int i = 0; i < nodesList.getLength(); i++) {
			Node XMLNode = nodesList.item(i);
			if (XMLNode != null && XMLNode.getNodeType() == Node.ELEMENT_NODE) {
				String nodeName = XMLNode.getNodeName();
				if (!nodeName.equals("leaf")) {		// 非叶节点
					flag = false;
					break;
				}
			}
		}
		return flag;
	}
	
	/**
	 * 判断在所有的子节点当中，是否存在至少一个叶子节点，是的话返回true
	 * @param nodesList
	 * @return
	 */
	public Boolean isHasLeafNode(NodeList nodesList) {
		Boolean flag = false;	// 默认没有
		for (int i = 0; i < nodesList.getLength(); i++) {
			Node XMLNode = nodesList.item(i);
			if (XMLNode != null && XMLNode.getNodeType() == Node.ELEMENT_NODE) {
				if (XMLNode.getNodeName().equals("leaf")) {
					flag = true;
					break;
				}
			}
		}
		
		return flag;
	}
	
	/**
	 * 计算树中每个节点的capacity
	 * @param treeNode
	 */
	public void capacitySet(TreeNode treeNode) {
		Integer nums = 0;
		if (isAllLeafTreeNode(treeNode)) {
			nums = treeNode.getChildrenNodeList().size();
			treeNode.setCapacity(nums);
			
			return ;
		}
		
		// 不是叶子节点，那就是非叶节点
		List<TreeNode> childrenList = treeNode.getChildrenNodeList();
		for (int i = 0; i < childrenList.size(); i++) {
			capacitySet(childrenList.get(i));
			nums += childrenList.get(i).getCapacity();
		}
		treeNode.setCapacity(nums);
		
	}
	
	/**
	 * 对树中的所有叶子节点都进行排序
	 * @param treeNode
	 */
	public void sortLeafTreeNode(TreeNode treeNode) {
		if (isAllLeafTreeNode(treeNode)) {	// 该节点是顺序节点
			treeOperation = new TreeOperation();
			treeOperation.sort(treeNode);
			
			return;
		}
		
		List<TreeNode> childrenNodesList = treeNode.getChildrenNodeList();
		for (int i = 0; i < childrenNodesList.size(); i++) {
			TreeNode childTreeNode = childrenNodesList.get(i);
			sortLeafTreeNode(childTreeNode);
		}
	}
	
	/**
	 * 判断该节点的子节点是否都是叶子节点
	 * @param treeNode
	 * @return	是的话，返回true
	 */
	public Boolean isAllLeafTreeNode(TreeNode treeNode) {
		Boolean flag = true;
		List<TreeNode> childrenList = treeNode.getChildrenNodeList();
		for (int i = 0; i < childrenList.size(); i++) {
			if (!(childrenList.get(i).getNodeType().equals(Constant.LEAF))) {	// 如果某个孩子节点的nodeType的标记不是
				flag = false;													// Constant.LEAF，那就置flag为false
				break;
			}
		}
		
		return flag;
	}
	
}
