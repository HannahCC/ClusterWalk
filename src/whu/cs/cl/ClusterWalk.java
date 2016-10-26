package whu.cs.cl;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
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

	public static void main(String args[]) throws IOException,
			InterruptedException {
		for(String arg:args){
			System.out.print(arg+"\t");
		}
		System.out.println();
		String curPath = args[0];
		String dataSet = args[1];
		int dataSize = Integer.parseInt(args[2]);
		int labelSize = Integer.parseInt(args[3]);
		int trainPercent = Integer.parseInt(args[4]);
		int foldSize = Integer.parseInt(args[5]);
		int rounds = Integer.parseInt(args[6]);
		int length = Integer.parseInt(args[7]);
		int iter = 10; // canopy 迭代次数

		String graphFile = curPath + "graphs/" + dataSet + ".edgelist";
		String labelFileDir = curPath + "classifiers/(train_" + trainPercent
				+ "percent)10fold_all/";
		String walkDir = curPath + "cw_walks/";
		String walkFile = walkDir + dataSet + "_cw4" + "_r" + rounds + "l"
				+ length + ".walks.t";

		Node[] nodes = new Node[dataSize];
		for (int i = 0; i < dataSize; i++) {
			nodes[i] = new Node(i + 1, labelSize);
		}

		FileUtils.readGraph(graphFile, nodes);
		ExecutorService threadPool = Executors.newFixedThreadPool(rounds);

		for (int f = 0; f < foldSize; f++) {
			initNode(nodes);
			FileUtils.readLabel(labelFileDir, labelSize, f, nodes);
			int r = 0;
			while (r++ < iter) {
				resetCluster(nodes);// 每次聚类都重置簇
				for (Node node : nodes) {// 对每个node的邻居进行聚类，直到指定次数，且每个聚类都确定了属性
					Canopy canopy = new Canopy(node.adjacents, node.clusters,
							nodes);
					canopy.cluster();
				}
				for (Node node : nodes) {
					node.setClusterLabel(nodes, labelSize);
					node.updateClusterNodeVector(nodes);
				}
			}
			mergeCluster(nodes, labelSize);// 将label相同的簇合并
			FileUtils.initWalkFiles(walkDir, walkFile + trainPercent + "f" + f,
					rounds);
			CountDownLatch countDownLatch = new CountDownLatch(rounds);
			for (r = 0; r < rounds; r++) {
				threadPool.execute(new Walk(r, length, nodes, countDownLatch));
			}
			countDownLatch.await();
			FileUtils.mergeWalkFiles(walkFile + trainPercent + "f" + f, rounds);
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

	private static void mergeCluster(Node[] nodes, int labelSize) {
		for (Node node : nodes) {
			node.mergeCluster(labelSize);
		}
	}

	private static void resetCluster(Node[] nodes) {
		for (Node node : nodes) {
			node.resetCluster();
		}
	}

	private static void initNode(Node[] nodes) {
		for (Node node : nodes) {
			node.initVector();
		}
	}
}
