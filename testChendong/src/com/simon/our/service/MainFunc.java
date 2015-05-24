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
		// 再对树的叶子节点按照delay_mu降序排序
		treeConstruction.sortLeafTreeNode(rootTreeNode);
		
		List<TreeNode> childrenList = rootTreeNode.getChildrenNodeList();
		for (int i = 0; i < childrenList.size(); i++) {
			System.out.println("第" + i + "个：" + childrenList.get(i).getCapacity());
		}
		
		// 树的遍历
		treeOperation.traverseTree(rootTreeNode);
		Integer numNodes = rootTreeNode.getCapacity();
		OurMethods ourMethods = new OurMethods();
		for (int k = 0; k <= numNodes; k++) {
			System.out.println("k = " + k);
			
			Integer optimalResTime = ourMethods.getOptimalResTime(rootTreeNode, k);
			System.out.println("最佳响应时间：" + optimalResTime);
			
			// 计算最佳响应时间下的monitors分布情况，即哪些点是被分配到monitor的
			List<String> nodeNamesList = ourMethods.getNodesMonitored(rootTreeNode, k);
			System.out.print("nodeNames:  ");
			for (int i = 0; i < nodeNamesList.size(); i++) {
				System.out.print(nodeNamesList.get(i) + "  ");
			}
			System.out.println();
			System.out.println();
		}
		
	}
}
