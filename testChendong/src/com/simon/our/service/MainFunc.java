package com.simon.our.service;

import org.w3c.dom.Element;

import com.simon.our.pojo.TreeNode;
import com.simon.our.util.Constant;

public class MainFunc {

	public static void main(String []args) {
		TreeConstruction treeConstruction = new TreeConstruction();
		String filePath = "./graphDescription.xml";
		XMLProcession xmlProcession = new XMLProcession();
		Element rootElement = xmlProcession.getRootElement(filePath);
		
		// 创建整个数的头结点,即树的根。该节点是一个“顺序”的非叶节点
		TreeNode rootTreeNode = new TreeNode();
		rootTreeNode.setNodeName("root");
		rootTreeNode.setChildrenNodeList(null);
		rootTreeNode.setNodeType(Constant.SEQUENCE);
		rootTreeNode.setParentNode(null);
		// 树的构建
		treeConstruction.traverseXMLDocument(rootTreeNode, rootElement);
		// 树的遍历
		TreeOperation treeOperation = new TreeOperation();
		treeOperation.traverseTree(rootTreeNode);
		
		
	}
}
