package com.simon.our.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DPcombination {

	/**
	 *  allocate balls to binList
	 * return all allocation combinations
	 * 
	 * @param balls	monitor的个数
	 * @param binList	相当于桶的个数，并且，每个桶都有一个容量限制
	 * @return
	 */
	public  List<List<Integer>> dpAllocation(int balls,
			List<Integer> binList) {
		if (binList.size() == 0) {
			if (balls == 0) {
				return new ArrayList<>();
			} else {
				return null;
			}
		}

		int smaller = binList.get(binList.size() - 1) > balls ? balls : binList
				.get(binList.size() - 1);
		List<List<Integer>> result = new ArrayList<>();
		for (int i = 0; i <= smaller; i++) {
			List<List<Integer>> plan;
			if (cache.containsKey(binList.size() - 1)) {
				if (cache.get(binList.size() - 1).containsKey(balls - i)) {
					plan = cache.get(binList.size() - 1).get(balls - i);
				} else {
					plan = dpAllocation(balls - i,
							binList.subList(0, binList.size() - 1));
					cache.get(binList.size() - 1).put(balls - i, plan);
				}
			} else {
				plan = dpAllocation(balls - i,
						binList.subList(0, binList.size() - 1));
				HashMap<Integer, List<List<Integer>>> map = new HashMap<>();
				map.put(balls - i, plan);
				cache.put(binList.size() - 1, map);
			}

			if (plan == null) {
				continue;
			}
			if (plan.size() == 0 && binList.size() == 1) {
				List<Integer> temp = new ArrayList<>();
				temp.add(i);
				result.add(temp);
			}
			for (List<Integer> list : plan) {
				List<Integer> newList = new ArrayList<>(list);
				newList.add(i);
				result.add(newList);
			}

		}

		return result;

	}

	/*
	 * two dimensional cache the first key means bins the second key means balls
	 * the final value means the allocation combinations
	 */
	HashMap<Integer, HashMap<Integer, List<List<Integer>>>> cache = new HashMap<>();

	public static void main(String[] args) {
		List<Integer> binSizeList = new ArrayList<>();
		binSizeList.add(4);
		binSizeList.add(11);
		// binSizeList.add(3);
		int balls = 5;
		DPcombination dPcombination = new DPcombination();
		List<List<Integer>> result = dPcombination.dpAllocation(balls, binSizeList);
		for (List<Integer> list : result) {
			for (Integer integer : list) {
				System.out.print(integer + ",");
			}
			System.out.println("");
		}
	}

}
