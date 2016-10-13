package whu.cs.cl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Walk implements Runnable {

	int round;
	int length;
	Node[] nodes = null;
	List<Set<Node>> clusters = null;
	int[] clustersFreqSum = null;
	Random random = new Random();

	public Walk(int round, int length, Node[] nodes, List<Set<Node>> clusters,
			int[] clustersFreqSum) {
		this.round = round;
		this.length = length;
		this.nodes = nodes;
		this.clusters = clusters;
		this.clustersFreqSum = clustersFreqSum;
	}

	@Override
	public void run() {
		for (Node node : nodes) {
			List<Integer> walk = new ArrayList<>();
			int cluster_id = node.getRandomCluster();
			for (int i = 0, size = clusters.size(); i < size; i++) {
				if (i == cluster_id) {
					walk.add(node.idx);
				} else {
					walk.add(getRandomNodeBiasFreq(clusters.get(i),
							clustersFreqSum[i]));
				}
			}
			try {
				FileUtils.writeWalk(round, walk);
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
