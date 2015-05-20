package com.simon.our.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.simon.our.pojo.TreeNode;
import com.simon.our.util.Constant;

/**
 * 树的相关操作
 * @author SSS
 *
 */
public class TreeOperation {

	/**
	 * 将一个节点集合添加到当前节点的孩子节点列表中
	 * @param currentTreeNode	数节点
	 * @param nodesList	节点的有序集合
	 */
	public void addNodeAsChild(TreeNode currentTreeNode, NodeList nodesList) {
		
		List<TreeNode> childrenNodeList = new ArrayList<TreeNode>();	// 设置子节点列表
		for (int i = 0; i < nodesList.getLength(); i++) {
			Node node = nodesList.item(i);
			if (node != null && node.getNodeType() == Node.ELEMENT_NODE) {
				TreeNode leafTreeNode = this.initLeafTreeNode(currentTreeNode, node);
				childrenNodeList.add(leafTreeNode);
			}
		}
		currentTreeNode.setChildrenNodeList(childrenNodeList);
	}
	
	/**
	 * 对于XML文档中的一个leaf节点，转换为树中的叶子节点。
	 * 转换的过程中，不要忘了该node的属性值
	 * @param parentTreeNode	要转为叶子节点的父节点
	 * @param XMLNode	XML文档中对应的leaf节点
	 * @return
	 */
	public TreeNode initLeafTreeNode(TreeNode parentTreeNode, Node XMLNode) {
		TreeNode treeNode = new TreeNode();
		
		treeNode.setNodeName(XMLNode.getTextContent());
		// 获得正常的响应值
		treeNode.setRespTimeValue(Integer.valueOf(XMLNode.getAttributes().getNamedItem("value").getNodeValue()));
		// 获得该节点的延迟的mu 和 sigma
		treeNode.setDelayMu(Integer.valueOf(XMLNode.getAttributes().getNamedItem("delay_mu").getNodeValue()));
		treeNode.setDelaySigma(Integer.valueOf(XMLNode.getAttributes().getNamedItem("delay_sigma").getNodeValue()));
		treeNode.setChildrenNodeList(null);
		treeNode.setAllocationResult(null);
		treeNode.setAlloMap(null);
		
		treeNode.setCapacity(1);	//叶子节点的容量为1，即它自身
		treeNode.setNodeType(Constant.LEAF);
		treeNode.setParentNode(parentTreeNode);
		
		return treeNode;
	}
	
	/**
	 * 创建一个“并发”非叶节点
	 * @param parentTreeNode
	 * @return
	 */
	public TreeNode initConcurTreeNode(TreeNode parentTreeNode) {
		TreeNode concurTreeNode = new TreeNode();
		
		concurTreeNode.setNodeName("concurrency");
		List<TreeNode> childrenList = new ArrayList<TreeNode>();
		concurTreeNode.setChildrenNodeList(childrenList);
		
		// 对于非叶节点，将两个哈希表初始化
		HashMap<Integer, Integer> allocationResult = new HashMap<Integer, Integer>();
		concurTreeNode.setAllocationResult(allocationResult);
		HashMap<Integer, ArrayList<Integer>> alloMap = new HashMap<Integer, ArrayList<Integer>>();
		concurTreeNode.setAlloMap(alloMap);
		
		concurTreeNode.setNodeType(Constant.CONCURRENCY);
		concurTreeNode.setParentNode(parentTreeNode);
		concurTreeNode.setCapacity(0);	// 刚开始生成树的时候，无法知道以该节点为树根的字数有多少个叶节点
		
		return concurTreeNode;
	}
	
	/**
	 * 创建一个“顺序”非叶节点
	 * @param parentTreeNode
	 * @param XMLNode
	 * @return
	 */
	public TreeNode initSequTreeNode(TreeNode parentTreeNode, Node XMLNode) {
		TreeNode sequTreeNode = new TreeNode();
		sequTreeNode.setNodeName("sequence");
		List<TreeNode> childrenList = new ArrayList<TreeNode>();
		sequTreeNode.setChildrenNodeList(childrenList);
		
		// 对于非叶节点，将两个哈希表初始化
		HashMap<Integer, Integer> allocationResult = new HashMap<Integer, Integer>();
		sequTreeNode.setAllocationResult(allocationResult);
		HashMap<Integer, ArrayList<Integer>> alloMap = new HashMap<Integer, ArrayList<Integer>>();
		sequTreeNode.setAlloMap(alloMap);
		
		sequTreeNode.setNodeType(Constant.SEQUENCE);
		sequTreeNode.setParentNode(parentTreeNode);
		sequTreeNode.setCapacity(0);	// 刚开始生成树的时候，无法知道以该节点为树根的字数有多少个叶节点
		
		// 另外，需要判断的是parentTreeNode是否是“选择”节点，如果是，要得到XMLNode的概率属性值，
		// 然后将得到的每一个值添加到parentTreeNode的概率列表中。
		if (parentTreeNode.getNodeName().equals(Constant.selectionName)) {
			Double probability = Double.valueOf(XMLNode.getAttributes().getNamedItem("probability").getNodeValue());
			List<Double> probList = parentTreeNode.getProbabilityList();
			if (probList == null) {	// 第一次的时候，是null
				probList = new ArrayList<Double>();
			}
			probList.add(probability);
			parentTreeNode.setProbabilityList(probList);
		}
		
		return sequTreeNode;
	}
	
	/**
	 * 创建一个“选择”非叶节点
	 * @param parentTreeNode
	 * @return
	 */
	public TreeNode initSeleTreeNode(TreeNode parentTreeNode) {
		TreeNode seleTreeNode = new TreeNode();
		seleTreeNode.setNodeName("selection");
		List<TreeNode> childrenList = new ArrayList<TreeNode>();
		seleTreeNode.setChildrenNodeList(childrenList);
		

		// 对于非叶节点，将两个哈希表初始化
		HashMap<Integer, Integer> allocationResult = new HashMap<Integer, Integer>();
		seleTreeNode.setAllocationResult(allocationResult);
		HashMap<Integer, ArrayList<Integer>> alloMap = new HashMap<Integer, ArrayList<Integer>>();
		seleTreeNode.setAlloMap(alloMap);
		
		seleTreeNode.setNodeType(Constant.SELECTION);
		seleTreeNode.setParentNode(parentTreeNode);
		seleTreeNode.setCapacity(0);	// 刚开始生成树的时候，无法知道以该节点为树根的子树有多少个叶节点
		
		return seleTreeNode;
	}
	
	/**
	 * 创建一个“循环”非叶节点
	 * @param parentTreeNode
	 * @param XMLNode
	 * @return
	 */
	public TreeNode initLoopTreeNode(TreeNode parentTreeNode, Node XMLNode) {
		TreeNode loopTreeNode = new TreeNode();
		//得到最大循环次数
		Integer loopMax = Integer.valueOf(XMLNode.getAttributes().getNamedItem("loopMax").getNodeValue());
		loopTreeNode.setLoopMax(loopMax);
		
		// 根据最大循环次数，得到一个概率列表
		List<Double> probabilityList = new ArrayList<Double>();
		for (int i = 0; i < loopMax; i++) {
			Double prob = Double.valueOf(XMLNode.getAttributes().getNamedItem("probAttr_" + (i + 1)).getNodeValue());
			probabilityList.add(prob);
		}
		loopTreeNode.setProbabilityList(probabilityList);
		loopTreeNode.setNodeName(Constant.loopName);
		loopTreeNode.setNodeType(Constant.LOOP);
		List<TreeNode> childrenList = new ArrayList<TreeNode>();
		
		// 对于非叶节点，将两个哈希表初始化
		HashMap<Integer, Integer> allocationResult = new HashMap<Integer, Integer>();
		loopTreeNode.setAllocationResult(allocationResult);
		HashMap<Integer, ArrayList<Integer>> alloMap = new HashMap<Integer, ArrayList<Integer>>();
		loopTreeNode.setAlloMap(alloMap);
		
		loopTreeNode.setChildrenNodeList(childrenList);
		loopTreeNode.setParentNode(parentTreeNode);	
		loopTreeNode.setCapacity(0);	// 刚开始生成树的时候，无法知道以该节点为树根的子树有多少个叶节点
		
		return loopTreeNode;
	}
	
	/**
	 * 添加一个新的节点到父节点的子节点列表中 
	 * 这里对子节点的处理非常简单，只是认为一个顺序结构，里面没有其他的复杂结构
	 * @param treeNode
	 * @param childTreeNode
	 */
	public void addChildToTreeNode(TreeNode treeNode, TreeNode childTreeNode) {
		List<TreeNode> childrenList = treeNode.getChildrenNodeList();
		if (childrenList == null) {	//如果是null，则说明还没有赋值，首先创建一个子节点的list，然后赋值给改节点
			childrenList = new ArrayList<TreeNode>();
			treeNode.setChildrenNodeList(childrenList);
		}
		childrenList.add(childTreeNode);	// 添加新的子节点到原来的子节点列表中
		treeNode.setChildrenNodeList(childrenList);
	}
	
	/**
	 * 对树的遍历，深度优先
	 * @param treeNode
	 */
	public void traverseTree(TreeNode treeNode) {
		if (treeNode.getNodeType().equals(Constant.LEAF)) {
			String nodeName = treeNode.getNodeName();
			System.out.println("leaf: " + nodeName);
			
			return;
		}
		
		// 输入该非叶节点
		System.out.println("non-leaf: " + treeNode.getNodeName());
		
		List<TreeNode> childrenList = treeNode.getChildrenNodeList();
		for (int i = 0; i < childrenList.size(); i++) {
			TreeNode childNode = childrenList.get(i);
			traverseTree(childNode);
		}
	}
	
}
