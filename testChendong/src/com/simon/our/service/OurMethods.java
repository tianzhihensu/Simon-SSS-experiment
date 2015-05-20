package com.simon.our.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
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
		
		if (isAllLeafTreeNode(treeNode)) {	// 如果孩子节点全都是叶子节点
			// 若一个节点的所有孩子节点都是叶节点，那么该节点一定是顺序节点，在顺序的节点下，
			// 如何最优分配，就按照孩子节点的delay_resTime降序排列,分配前k个
			HashMap<Integer, Integer> allocationResult = treeNode.getAllocationResult();
			if (allocationResult.get(k) == null) {	// 说明之前还没有算过这个值
				List<Object> resList = calNewResTimeByK(treeNode, k);
				optimalResTime = (Integer) resList.get(0);
				allocationResult.put(k, optimalResTime);
				
				ArrayList<Integer> indexCollection = (ArrayList<Integer>) resList.get(1);
				HashMap<Integer, ArrayList<Integer>> alloMap = treeNode.getAlloMap();
				alloMap.put(k, indexCollection);
			}
			
			optimalResTime = allocationResult.get(k);
			
			return optimalResTime;
		}
		// 如果该节点的孩子节点不全是叶节点，那就说明该节点的孩子节点都是非叶节点
		List<TreeNode> childrenList = new ArrayList<TreeNode>();
		childrenList = treeNode.getChildrenNodeList();
		
		
		
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
		
		HashMap<String, Integer> indexAndDelayValue = new HashMap<String, Integer>();	// index这里设置为String类型
		for (int i = 0; i < childrenList.size(); i++) {
			indexAndDelayValue.put(String.valueOf(i), childrenList.get(i).getDelayMu());
		}
		
		ByValueComparator bvc = new ByValueComparator(indexAndDelayValue);
		TreeMap<String, Integer> sortedMap = new TreeMap<String, Integer>(bvc);
		
		Integer count = 0;	// 计数器
		Integer sumOfDelayMu = 0;
		for (String index : sortedMap.keySet()) {
			if (count == k) {
				break;
			}
			sumOfDelayMu += indexAndDelayValue.get(index);
			count ++;
		}
		
		Integer optimalResTime = totalResTime - sumOfDelayMu;
		resList.add(optimalResTime);
		
		//除了最佳响应时间，还要保存到底哪些下标被monitor了
		ArrayList<Integer> indexCollection = new ArrayList<Integer>();
		for (String index : sortedMap.keySet()) {
			indexCollection.add(Integer.valueOf(index));
		}
		resList.add(indexCollection);
		
		return resList;
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
