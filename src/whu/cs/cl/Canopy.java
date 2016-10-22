package whu.cs.cl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Canopy {
	private Node[] nodes;
	private List<Node> subNodes;
	private List<Cluster> clusters;
	private double T1 = -1; // 阈值
	private double T2 = -1; // 阈值

	public Canopy(List<Integer> adjacents, List<Cluster> clusters, Node[] nodes) {
		subNodes = new ArrayList<>();
		for (Integer nodeIdx : adjacents) {// 进行深拷贝
			this.subNodes.add(nodes[nodeIdx]);
		}
		Collections.sort(subNodes);
		this.clusters = clusters;
		this.nodes = nodes;
		this.T1 = getAverageDistance();
		this.T2 = Math.sqrt(0.25 * nodes[0].vector.length);
		if (T1 < T2) {
			double temp = T1;
			T1 = T2;
			T2 = temp;
		}

	}

	/**
	 * 进行聚类，按照Canopy算法进行计算
	 */
	public void cluster() {
		while (subNodes.size() != 0) {
			Node baseNode = subNodes.get(0);// 基准点
			subNodes.remove(0);
			Cluster cluster = new Cluster();
			cluster.nodes.add(baseNode);
			for (Integer nodeIdx : baseNode.adjacents) {
				Node anotherNode = nodes[nodeIdx];
				if (subNodes.contains(anotherNode)) {
					double distance = baseNode.cosSimilarity(anotherNode);
					if (distance <= T2) {
						cluster.nodes.add(anotherNode);
						subNodes.remove(anotherNode);
					} else if (distance <= T1) {
						cluster.nodes.add(anotherNode);
					}
				}
			}
			clusters.add(cluster);
		}
	}

	/**
	 * 得到平均距离
	 * 
	 * @return 返回中心点
	 */
	private double getAverageDistance() {
		double sum = 0;
		int nodeSize = subNodes.size();
		for (int i = 0; i < nodeSize; i++) {
			for (int j = 0; j < nodeSize; j++) {
				if (i == j)
					continue;
				Node nodeA = subNodes.get(i);
				Node nodeB = subNodes.get(j);
				sum += nodeA.cosSimilarity(nodeB);
			}
		}
		int distanceNumber = nodeSize * (nodeSize + 1) / 2;
		double avgDist = sum / distanceNumber / 2; // 平均距离的一半
		return avgDist;
	}
}