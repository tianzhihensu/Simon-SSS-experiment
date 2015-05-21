package com.simon.our.service;

import java.util.HashMap;
import java.util.List;

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
		TreeOperation treeOperation = new TreeOperation();
		TreeNode rootTreeNode = treeOperation.initRootTreeNode();
		
		// 树的构建
		treeConstruction.traverseXMLDocument(rootTreeNode, rootElement);
		// 树构建好之后，每个节点的capacity的计算
		treeConstruction.capacitySet(rootTreeNode);
		List<TreeNode> childrenList = rootTreeNode.getChildrenNodeList();
		for (int i = 0; i < childrenList.size(); i++) {
			System.out.println("第" + i + "个：" + childrenList.get(i).getCapacity());
		}
		
		// 树的遍历
		treeOperation.traverseTree(rootTreeNode);
		
		int k = 5;
		OurMethods ourMethods = new OurMethods();
		Integer optimalResTime = ourMethods.getOptimalResTime(rootTreeNode, k);
		System.out.println(optimalResTime);
	}
}
