package whu.cs.cl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClusterWalk {

	public static void main(String args[]) throws IOException {
		String curPath = args[0];
		String dataSet = args[1];
		int dataSize = Integer.parseInt(args[2]);
		String clusterAlg = args[3];
		int clusterSize = Integer.parseInt(args[4]);
		int rounds = Integer.parseInt(args[5]);
		int length = Integer.parseInt(args[6]);

		String graphFile = curPath + "graphs/" + dataSet + ".edgelist";
		String clusterFile = curPath + "clusters/" + dataSet + "_" + clusterAlg
				+ "_c" + clusterSize + ".clusters";
		String walkDir = curPath + "cw_walks/";
		String walkFile = walkDir + dataSet + "_" + clusterAlg + "_c"
				+ clusterSize + "_cw1" + "_r" + rounds + "l" + length
				+ ".walks";

		Node[] nodes = new Node[dataSize];
		List<Set<Node>> clusters = new ArrayList<Set<Node>>(clusterSize);
		int[] clustersFreqSum = new int[clusterSize];
		FileUtils.readGraph(graphFile, nodes);
		FileUtils.readCluster(clusterFile, nodes, clusters);
		initFreqSumMap(clusters, clustersFreqSum); // 从cluster中random一个node时，根据node的频率来random
		FileUtils.initWalkFiles(walkDir, walkFile, rounds);
		ExecutorService threadPool = Executors.newFixedThreadPool(rounds);
		for (int r = 0; r < rounds; r++) {
			threadPool.execute(new Walk(r, length, nodes, clusters, clustersFreqSum));
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

	private static void initFreqSumMap(List<Set<Node>> clusters,
			int[] clustersFreqSum) {
		for (int i = 0, size = clusters.size(); i < size; i++) {
			Set<Node> cluster = clusters.get(i);
			int freq = 0;
			for (Node node : cluster) {
				freq += node.freq;
			}
			clustersFreqSum[i] = freq;
		}
	}

}
