package whu.cs.cl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClusterWalk {

	public static void main(String args[]) throws IOException {
		String cur_path = args[0];
		String graphFile = cur_path + args[1];
		String clusterFile = cur_path + args[2];
		int iter = Integer.parseInt(args[3]);

		Map<Integer, Node> nodes = new HashMap<>();
		Map<Integer, Set<Node>> clusters = new HashMap<>();
		Map<Integer, Integer> clusters_freq_sum = new HashMap<>();
		FileUtils.readGraph(graphFile, nodes);
		FileUtils.readCluster(clusterFile, nodes, clusters);
		initFreqSumMap(clusters, clusters_freq_sum);

		ExecutorService threadPool = Executors.newFixedThreadPool(iter);
		for (int i = 0; i < iter; i++) {
			threadPool.execute(new Walk(i, nodes, clusters, clusters_freq_sum));
		}
		threadPool.shutdown();
		while (!threadPool.isTerminated()) {
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				// log.info("error during sleep");
			}
		}

	}

	private static void initFreqSumMap(Map<Integer, Set<Node>> clusters, Map<Integer, Integer> clusters_freq_sum) {
		for (Entry<Integer, Set<Node>> entry : clusters.entrySet()) {
			int cid = entry.getKey();
			Set<Node> cluster = entry.getValue();
			int freq = 0;
			for (Node node : cluster) {
				freq += node.freq;
			}
			clusters_freq_sum.put(cid, freq);
		}
	}

}
