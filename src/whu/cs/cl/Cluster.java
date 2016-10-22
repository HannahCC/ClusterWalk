package whu.cs.cl;

import java.util.ArrayList;
import java.util.List;

public class Cluster {

	List<Node> nodes;// 该簇有哪些节点
	int centroid; // 簇的质心（Node）的索引
	List<Integer> labels;// 簇的属性

	public Cluster() {
		this.nodes = new ArrayList<>();
		this.centroid = -1;
		this.labels = new ArrayList<>();
	}

	public void addNode(Node node) {
		nodes.add(node);
	}

	public void setCentroid(int nodeIdx) {
		centroid = nodeIdx;
	}

	public void setLabel(int labelSize) {
		float[] clusterVector = new float[labelSize];
		for (Node node : nodes) {
			Utils.arrayAdd(clusterVector, node.vector, node.labeled ? 8 : 1);
		}

		float max = clusterVector[0];
		labels.add(0);
		for (int i = 1; i < labelSize; i++) {
			if (clusterVector[i] == max) {
				labels.add(i);
			} else if (clusterVector[i] > max) {
				max = clusterVector[i];
				labels.clear();
				labels.add(i);
			}
		}
		//System.out.println(labels);
	}

	public void mergeCluster(Cluster cluster) {
		this.nodes.addAll(cluster.nodes);
	}

	public void updateNodeVector() {
		for (Node node : nodes) {
			node.updateVector(labels, 0.1f);
		}
	}
}
