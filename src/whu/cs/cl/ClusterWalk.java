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
		String curPath = args[0];
		String dataSet = args[1];
		String clusterAlg = args[2];
		int rounds = Integer.parseInt(args[3]);
		int length = Integer.parseInt(args[4]);

		String graphFile = curPath + "graphs/" + dataSet + ".edgelist";
		String clusterFile = curPath + "clusters/" + dataSet + "_" + clusterAlg
				+ "_l" + length + ".clusters";
		String walkDir = curPath + "cw_walks/";
		String walkFile = walkDir + dataSet + "_cw_" + clusterAlg + "_r"
				+ rounds + "l" + length + ".walks";

		Map<Integer, Node> nodes = new HashMap<>();
		Map<Integer, Set<Node>> clusters = new HashMap<>();
		Map<Integer, Integer> clustersFreqSum = new HashMap<>();
		FileUtils.readGraph(graphFile, nodes);
		FileUtils.readCluster(clusterFile, nodes, clusters);
		initFreqSumMap(clusters, clustersFreqSum); // 从cluster中random一个node时，根据node的频率来random
		FileUtils.initWalkFiles(walkDir, walkFile, rounds);
		ExecutorService threadPool = Executors.newFixedThreadPool(rounds);
		for (int r = 0; r < rounds; r++) {
			threadPool.execute(new Walk(r, nodes, clusters, clustersFreqSum));
		}
		threadPool.shutdown();
		while (!threadPool.isTerminated()) {
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				// log.info("error during sleep");
			}
		}
		FileUtils.mergeWalkFiles(walkFile, rounds);
	}

	private static void initFreqSumMap(Map<Integer, Set<Node>> clusters,
			Map<Integer, Integer> clusters_freq_sum) {
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
