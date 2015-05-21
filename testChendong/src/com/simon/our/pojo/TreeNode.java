package com.simon.our.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TreeNode {

	private String nodeName = null;		// 保存该树节点里的内容，服务名称 or 通信链路
	private Integer nodeType = null;	// 该节点的类型，是否是叶子节点，有四种类型:leaf，concurrency，sequence，loop
	private TreeNode parentNode = null;	// 该节点的父节点
	private List<TreeNode> childrenNodeList = null;	// 该节点的子节点
	
	// 以下是叶节点需要用到的一些属性
	private Integer respTimeValue = null;
	private Integer delayMu = null;
	private Integer delaySigma = null;
	
	// 以下是非叶节点需要用到的一些属性（待定）
	private HashMap<Integer, Integer> allocationResult = null;	// 第一个参数：分配给该子树的momitor的数量， 第二个参数：获得的最优响应时间
	private HashMap<Integer, List<Integer>> alloMap = null;	//分配k个monitor时，各个子节点的个数分配情况
	
	private List<Double> probabilityList = null;	// 存放概率的数组，主要用于存放“选择”（其实就包含了循环）非叶节点
	private Integer loopMax = null;		// loop节点的最大循环次数
	
	private Integer capacity = null;	// 每个节点都有一个容量限制，即最多只能放多少个monitors
	
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public String getNodeName() {
		return this.nodeName;
	}
	
	public void setNodeType(Integer nodeType) {
		this.nodeType = nodeType;
	}
	public Integer getNodeType() {
		return this.nodeType;
	}
	
	public void setParentNode(TreeNode parentNode) {
		this.parentNode = parentNode;
	}
	public TreeNode getParentNode() {
		return this.parentNode;
	}
	
	public void setChildrenNodeList(List<TreeNode> childrenNodeList) {
		this.childrenNodeList = childrenNodeList;
	}
	public List<TreeNode> getChildrenNodeList() {
		return this.childrenNodeList;
	}
	
	public void setRespTimeValue(Integer respTimeValue) {
		this.respTimeValue = respTimeValue;
	}
	public Integer getRespTimeValue() {
		return this.respTimeValue;
	}
	
	public void setDelayMu(Integer delayMu) {
		this.delayMu = delayMu;
	}
	public Integer getDelayMu() {
		return this.delayMu;
	}
	
	public void setDelaySigma(Integer delaySigma) {
		this.delaySigma = delaySigma;
	}
	public Integer getDelaySigma() {
		return this.delaySigma;
	}
	
	public void setAllocationResult(HashMap<Integer, Integer> allocationResult) {
		this.allocationResult = allocationResult;
	}
	public HashMap<Integer, Integer> getAllocationResult() {
		return this.allocationResult;
	}
	
	public void setAlloMap(HashMap<Integer, List<Integer>> alloMap) {
		this.alloMap = alloMap;
	}
	public HashMap<Integer, List<Integer>> getAlloMap() {
		return this.alloMap;
	}
	
	public void setProbabilityList(List<Double> probabilityList) {
		this.probabilityList = probabilityList;
	}
	public List<Double> getProbabilityList() {
		return this.probabilityList;
	}
	
	public void setLoopMax(Integer loopMax) {
		this.loopMax = loopMax;
	}
	public Integer getLoopMax() {
		return this.loopMax;
	}
	
	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}
	public Integer getCapacity() {
		return this.capacity;
	}
}
