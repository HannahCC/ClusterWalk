package whu.cs.cl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

public class Walk implements Runnable {

	int round = -1;
	Map<Integer, Node> nodes = null;
	Map<Integer, Set<Node>> clusters = null;
	Map<Integer, Integer> clustersFreqSum = null;
	Map<Integer, List<Integer>> walks = new HashMap<Integer, List<Integer>>();
	Random random = new Random();

	public Walk(int round, Map<Integer, Node> nodes, Map<Integer, Set<Node>> clusters, Map<Integer, Integer> clustersFreqSum) {
		this.round = round;
		this.nodes = nodes;
		this.clusters = clusters;
		this.clustersFreqSum = clustersFreqSum;
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
					walk.add(getRandomNodeBiasFreq(entry.getValue(), clustersFreqSum.get(cid)));
				}
			}
			try {
				FileUtils.writeWalk(round,walk);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
