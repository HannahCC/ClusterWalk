package whu.cs.cl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

public class Walk implements Runnable {

	int iter = -1;
	Map<Integer, Node> nodes = null;
	Map<Integer, Set<Node>> clusters = null;
	Map<Integer, Integer> clusters_freq_sum = null;
	Map<Integer, List<Integer>> walks = new HashMap<Integer, List<Integer>>();
	Random random = new Random();

	public Walk(int iter, Map<Integer, Node> nodes, Map<Integer, Set<Node>> clusters, Map<Integer, Integer> clusters_freq_sum) {
		this.iter = iter;
		this.nodes = nodes;
		this.clusters = clusters;
		this.clusters_freq_sum = clusters_freq_sum;
	}

	@Override
	public void run() {
		for (Node node : nodes.values()) {
			List<Integer> walk = new ArrayList<>();
			int cluster_id = node.getRandomCluster();
			for (Entry<Integer, Set<Node>> entry : clusters.entrySet()) {
				int cid = entry.getKey();
				if (cid == cluster_id) {
					walk.add(node.idx);
				} else {
					walk.add(getRandomNodeBiasFreq(entry.getValue(), clusters_freq_sum.get(cid)));
				}
			}
			FileUtils.writeWalk(iter,walk);
		}
		
	}

	private Integer getRandomNodeBiasFreq(Set<Node> cluster, int freq_sum) {
		int num = random.nextInt(freq_sum);
		int temp = 0;
		for (Node node : cluster) {
			temp += node.freq;
			if (num <= temp) {
				return node.idx;
			}
		}
		return Integer.MIN_VALUE;
	}
}
