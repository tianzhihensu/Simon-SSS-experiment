package com.simon.our.service;
/**
 * 这个是排序用到的，可无视
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {

	public static void main(String[] args) {
		Map<String, Integer> map_Data = new HashMap<String, Integer>();
		map_Data.put("A", 50);
		map_Data.put("B", 100);
		map_Data.put("C", 200);
		map_Data.put("D", 100);
		map_Data.put("E", 50);
		System.out.println(map_Data);
		List<Map.Entry<String, Integer>> list_Data = new ArrayList<Map.Entry<String, Integer>>(
				map_Data.entrySet());
		System.out.println(list_Data);
		Collections.sort(list_Data,
				new Comparator<Map.Entry<String, Integer>>() {
					public int compare(Map.Entry<String, Integer> o1,
							Map.Entry<String, Integer> o2) {
						if ((o2.getValue() - o1.getValue()) > 0)
							return 1;
						else if ((o2.getValue() - o1.getValue()) == 0)
							return 0;
						else
							return -1;
					}
				});
		for (int i = 0; i < list_Data.size(); i++)
			System.out.println(list_Data.get(i).getKey());
	}
}