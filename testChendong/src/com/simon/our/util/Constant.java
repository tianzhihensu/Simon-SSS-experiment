package com.simon.our.util;

public class Constant {

	// 树结构中的nodeType
	public static final Integer LEAF = 1;	// 叶节点
	public static final Integer SEQUENCE = 2;	// 顺序的非叶节点
	public static final Integer CONCURRENCY = 3;	// 并发的非叶节点
	public static final Integer SELECTION = 4;	// 选择的非叶节点
	public static final Integer LOOP = 5;	// 循环的非叶节点，这个感觉用不着，最终还是要转换为选择非叶节点
	
	// 树结构中的nodeName
	public static final String leafName = "leaf";
	public static final	String concurrenyName = "concurrency";
	public static final String sequenceName = "sequence";
	public static final String selectionName = "selection";
	public static final	String loopName = "loop";
}
