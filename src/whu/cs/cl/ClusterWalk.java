package whu.cs.cl;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class HeapNode {
	int nodeIdx;
	double distance;

	public HeapNode(int nodeIdx, double distance) {
		this.nodeIdx = nodeIdx;
		this.distance = distance;
	}
}

public class ClusterWalk {

	public static void main(String args[]) throws IOException {
		for (int i = 0, ls = args.length; i < ls; i++) {
			System.out.print(args[i] + "\t");
		}
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
				+ clusterSize + "_cw3" + "_r" + rounds + "l" + length
				+ ".walks";

		Node[] nodes = new Node[dataSize];
		for (int i = 0; i < dataSize; i++) {

			nodes[i] = new Node(i + 1);
		}
		Cluster[] clusters = new Cluster[clusterSize];

		FileUtils.readGraph(graphFile, nodes);
		FileUtils.readCluster(clusterFile, nodes, clusters);
		Utils.setClusterCentroid(nodes, clusters);
		// Utils.setClusterNeighbour(nodes, clusters);
		Utils.setNodeShift(nodes, clusters);
		Utils.sortNodeEdge(nodes);
		FileUtils.initWalkFiles(walkDir, walkFile, rounds);
		ExecutorService threadPool = Executors.newFixedThreadPool(rounds);
		for (int r = 0; r < rounds; r++) {
			threadPool.execute(new Walk(r, length, nodes, clusters));
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
}
