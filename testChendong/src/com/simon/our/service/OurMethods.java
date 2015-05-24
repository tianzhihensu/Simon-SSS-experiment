package com.simon.our.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.simon.our.pojo.TreeNode;
import com.simon.our.util.Constant;

/**
 * 在生成好树之后，用动态规划的方式，算出分配k个monitors时的最优响应时间，
 * 以及每个monitor被分配的位置。
 * @author SSS
 *
 */
public class OurMethods {

	/**
	 * 得到最优响应时间,同时也会记录每个monitor都被分配到哪个地方了。
	 * @param treeNode	要分配k个monitor的（子）树
	 * @param k		monitor的个数
	 * @return
	 */
	public Integer getOptimalResTime(TreeNode treeNode, Integer k) {
		Integer optimalResTime = 0;
		/*System.out.println("hehe\n");*/
		// 如果已经存入了已知的值，就直接返回已经存入的值，避免重复计算
		if (treeNode.getAllocationResult().get(k) != null) {
			return treeNode.getAllocationResult().get(k);
		}
		
		if (isAllLeafTreeNode(treeNode)) {	// 如果孩子节点全都是叶子节点
			// 若一个节点的所有孩子节点都是叶节点，那么该节点一定是顺序节点，在顺序的节点下，
			// 如何最优分配，就按照孩子节点的delay_resTime降序排列,分配前k个
			HashMap<Integer, Integer> allocationResult = treeNode.getAllocationResult();
			if (allocationResult.get(k) == null) {	// 说明之前还没有算过这个值
				List<Object> resList = calNewResTimeByK(treeNode, k);
				optimalResTime = (Integer) resList.get(0);
				allocationResult.put(k, optimalResTime);
				
				List<Integer> indexCollection = (List<Integer>) resList.get(1);
				HashMap<Integer, List<Integer>> alloMap = treeNode.getAlloMap();
				alloMap.put(k, indexCollection);
			}
			
			optimalResTime = allocationResult.get(k);
			
			return optimalResTime;
		}
		
		// 如果该节点的孩子节点不全是叶节点，那就说明该节点的孩子节点都是非叶节点
		List<TreeNode> childrenList = new ArrayList<TreeNode>();
		childrenList = treeNode.getChildrenNodeList();
		//先得到所有孩子节点的容量之和，在父节点那儿有保存
		Integer totalCapacity = treeNode.getCapacity();
		
		// 在对孩子节点逐一计算k取上下限之间所有值时的对应最优响应时间时，应设定一个数组，保存所有可能的取值情况
		// 一个hashMap对应一个孩子节点所有的k取值下的最优响应时间, 第一个参数表示k的取值，第二个表示最优响应时间
		List<HashMap<Integer, Integer>> intermediateResult = new ArrayList<HashMap<Integer,Integer>>();
		
		for (int i = 0; i < childrenList.size(); i++) {
			TreeNode childTreeNode = childrenList.get(i);
			// 界定每个分支安放monitor个数的上、下限
			int downLimit = 0;
			int upLimit = 0;
			downLimit = k>(totalCapacity - childTreeNode.getCapacity())? k - (totalCapacity - childTreeNode.getCapacity()) : 0;
			upLimit = childTreeNode.getCapacity()>k? k : childTreeNode.getCapacity();
			
			HashMap<Integer, Integer> resTimeMap = new HashMap<Integer, Integer>();
			for (int j = downLimit; j <= upLimit; j++) {
				Integer resTimeByJ = 0;
				resTimeByJ = getOptimalResTime(childTreeNode, j);
				resTimeMap.put(j, resTimeByJ);	// 将每一种可能的结果，都放进哈希列表中
			}
			intermediateResult.add(resTimeMap);
		}
		
		// 根据容量和K值，得到所有的组合数
		List<Integer> binList = new ArrayList<Integer>();
		for (int i = 0; i < childrenList.size(); i++) {
			binList.add(childrenList.get(i).getCapacity());
		}
		List<List<Integer>> combiResult = new ArrayList<List<Integer>>();
		DPcombination dPcombination = new DPcombination();
		combiResult = dPcombination.dpAllocation(k, binList);
		
		// 做测试用
		/*System.out.println("k:" + k);
		for (int i = 0; i < binList.size(); i++) {
			System.out.print(binList.get(i) + "    ");
		}
		System.out.println();
		for (int i = 0; i < combiResult.size(); i++) {
			List<Integer> ss = combiResult.get(i);
			System.out.print(combiResult.get(i) + "    ");
		}
		System.out.println();*/
		
		// 遍历所有的组合数，得到一个最优解
		optimalResTime = 0;
		List<Integer> finalCombiList = new ArrayList<Integer>();
		for (int i = 0; i < combiResult.size(); i++) {
			List<Integer> combiList = combiResult.get(i);
			Integer curResTime = gFunc(combiList, treeNode, intermediateResult);
			if (i == 0) {	// 赋初值
				optimalResTime = curResTime;
				finalCombiList = combiList;
			} else {
				if (optimalResTime > curResTime) {	// 当记录到有更短的响应时间的时候，就替换掉，并且记录下当前的组合方式
					optimalResTime = curResTime;
					finalCombiList = combiList;
				}
			}
		}
		
		// 在得到最优解的时候，要把当前k下的最优解和组合方式记录在treeNode里
		HashMap<Integer, Integer> allocationResult = treeNode.getAllocationResult();
		allocationResult.put(k, optimalResTime);
		treeNode.setAllocationResult(allocationResult);
		
		HashMap<Integer, List<Integer>> alloMap = treeNode.getAlloMap();
		alloMap.put(k, finalCombiList);
		
		return optimalResTime;
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
	
	/**
	 * 计算在分配k个monitor的情况下，最优的响应时间,以及这k个monitor的分布情况
	 * @param treeNode
	 * @param k
	 * @return
	 */
	public List<Object> calNewResTimeByK(TreeNode treeNode, Integer k) {
		List<Object> resList = new ArrayList<Object>();
		
		List<TreeNode> childrenList = treeNode.getChildrenNodeList();
		//首先算总的resTime
		Integer totalResTime = 0;
		for (int i = 0; i < childrenList.size(); i++) {
			TreeNode tempNode = childrenList.get(i);
			totalResTime += tempNode.getDelayMu() + tempNode.getRespTimeValue();
		}
		
		Integer sumOfDelayMu = 0;
		for (int i = 0; i < k; i++) {
			sumOfDelayMu += childrenList.get(i).getDelayMu();
		}
		
		Integer optimalResTime = totalResTime - sumOfDelayMu;
		resList.add(optimalResTime);
		
		//除了最佳响应时间，还要保存到底哪些下标被monitor了, 因为这里已经排过序了，所以直接添加进下标即可
		List<Integer> indexCollection = new ArrayList<Integer>();
		for (int i = 0; i < k; i++) {
			indexCollection.add(i);
		}
		resList.add(indexCollection);
		
		return resList;
	}
	
	/**
	 * 根据一种在孩子节点上分配monitor的组合方式，得到该组合方式下的响应时间
	 * @param combiList	一种排列方式
	 * @param treeNode	一个树的节点，需要对该节点的孩子节点分配k个monitors
	 * @param intermediateResult
	 * @return
	 */
	public Integer gFunc(List<Integer> combiList, TreeNode treeNode, List<HashMap<Integer, Integer>> intermediateResult) {
		Integer ResTime = 0;
		
		// 根据tree类型的不同，选择不同的计算方式
		if (treeNode.getNodeType().equals(Constant.SEQUENCE)) {
			// 顺序的话，就是将各个孩子的响应时间加起来
			for (int i = 0; i < combiList.size(); i++) {
				Integer count = combiList.get(i);
				ResTime += intermediateResult.get(i).get(count);
			}
			
		} else if (treeNode.getNodeType().equals(Constant.CONCURRENCY)) {
			// 并发结构，就算所有的孩子节点中，最长的那一个
			for (int i = 0; i < combiList.size(); i++) {
				Integer count = combiList.get(i);
				Integer tempResTime = intermediateResult.get(i).get(count);
				if (tempResTime > ResTime) {
					ResTime = tempResTime;
				}
			}
			
		} else if (treeNode.getNodeType().equals(Constant.SELECTION)) {
			// 选择结构
			
			
		} else {	// 不是以上三种，只剩下了loop类型
			// 循环结构
			
		}
		
		return ResTime;
	}
	
	/**
	 * 找到树中有哪些节点是被monitor到的
	 * @param treeNode
	 * @param k
	 * @return 返回被monitor的节点的名称列表
	 */
	public List<String> getNodesMonitored(TreeNode treeNode, Integer k) {
		List<String> nodesList = new ArrayList<String>();
		if (isAllLeafTreeNode(treeNode)) {
			List<Integer> alloIndexList = treeNode.getAlloMap().get(k);
			List<TreeNode> childrenList = treeNode.getChildrenNodeList();
			for (int i = 0; i < alloIndexList.size(); i++) {
				TreeNode node = childrenList.get(alloIndexList.get(i));
				nodesList.add(node.getNodeName());
			}
			
			return nodesList;
		}
		
		// 如果其孩子节点不全是叶子节点，则递归寻找
		
		// 首先取出孩子节点
		List<TreeNode> childrenList = treeNode.getChildrenNodeList();
		// 然后得到该节点分配K个monitors的时候其子节点的个数分配情况
		List<Integer> combiList = treeNode.getAlloMap().get(k);
		
		for (int i = 0; i < childrenList.size(); i++) {
			TreeNode childTreeNode = childrenList.get(i);
			Integer count = combiList.get(i);	// 在该孩子节点上分配了几个monitors
			List<String> tempNodesList = getNodesMonitored(childTreeNode, count);
			// 将得到的临时结果放在外部的大结果列表里
			for (int j = 0; j < tempNodesList.size(); j++) {
				nodesList.add(tempNodesList.get(j));
			}
		}
		
		return nodesList;
	}
	
	public static void main(String[] args) {
		HashMap<String, Integer> datas = new HashMap<String, Integer>(){{
            put("Winter Lau", 100);
            put("Yier", 150);
            put("Nothing", 30);
            put("Zolo", 330);
        }};
         
        ByValueComparator bvc = new ByValueComparator(datas);
         
        //第一种方法
        TreeMap<String, Integer> sorted_map = new TreeMap<String, Integer>(bvc);
        sorted_map.putAll(datas);
         
        for(String name : sorted_map.keySet()){
            System.out.printf("%s -> %d\n", name, datas.get(name));
        }
	}
	
	static class ByValueComparator implements Comparator<String> {
        HashMap<String, Integer> base_map;
 
        public ByValueComparator(HashMap<String, Integer> base_map) {
            this.base_map = base_map;
        }
 
        public int compare(String arg0, String arg1) {
            if (!base_map.containsKey(arg0) || !base_map.containsKey(arg1)) {
                return 0;
            }
 
            if (base_map.get(arg0) < base_map.get(arg1)) {
                return 1;
            } else if (base_map.get(arg0) == base_map.get(arg1)) {
                return 0;
            } else {
                return -1;
            }
        }
    }
}
