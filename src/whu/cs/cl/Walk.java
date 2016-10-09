package whu.cs.cl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;


public class Walk implements Runnable{

	Map<Integer,Node> nodes = null;
	Map<Integer,Set<Node>> clusters = null;
	Map<Integer, List<Integer>> walks = new HashMap<Integer, List<Integer>>();
	public Walk(Map<Integer,Node> nodes, Map<Integer,Set<Node>> clusters){
		this.nodes = nodes;
		this.clusters = clusters;
	}
	@Override
	public void run() {
		for(Node node : nodes.values()){
			List<Integer> walk = new ArrayList<>();
			int cluster_id = node.getRandomCluster();
			for(Entry<Integer, Set<Node>>  entry: clusters.entrySet()){
				if(entry.getKey() == cluster_id){walk.add(node.idx);}
				else{
					walk.add(getRandomNodeBiasFreq(entry.getValue()));
				}
			}
			walks.put(node.idx, walk);
		}
	}
	private Integer getRandomNodeBiasFreq(Set<Node> value) {
		List<E>
		for(Node node : nodes){
			
		}
		return null;
	}


	public static void main(String[] args) {
	    System.out.println(	getRandomNum(new int[]{0,1,2,3}, new int[]{50,20,20,10}));
	}
	
	//probability 与 arr 一一对应的表示 arr 中各个数的概率，且满足 probability 各元素和不能超过 100；
	public static int getRandomNum(int[] arr, int[] probability){
		if(arr.length != probability.length) return Integer.MIN_VALUE;
		Random ran = new Random();
		int ran_num = ran.nextInt(100);
		int temp = 0;
		for (int i = 0; i < arr.length; i++) {
			temp += probability[i];
			if(ran_num < temp)
				return arr[i];
		}
		return Integer.MIN_VALUE;
	}
}



