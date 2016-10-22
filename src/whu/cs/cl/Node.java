package whu.cs.cl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Node implements Comparable<Node> {
	static Random random = new Random(123456);
	int id = -1;
	boolean labeled = false;
	List<Integer> adjacents = null;
	int degree = 0;

	float[] vector = null;
	List<Cluster> clusters = null;
	Cluster[] mergedClusters = null;

	public Node(int id, int labelSize) {
		this.id = id;
		this.adjacents = new ArrayList<>();
		this.vector = new float[labelSize];
		this.clusters = new ArrayList<>();
	}

	public void initVector() {
		this.labeled = false;
		for (int i = 0, size = this.vector.length; i < size; i++) {
			this.vector[i] = random.nextInt(100) / 100;
		}
	}

	public void resetCluster() {
		this.clusters.clear();
	}

	public void mergeCluster(int labelSize) {
		mergedClusters = new Cluster[labelSize];
		for (Cluster cluster : clusters) {
			for (int label : cluster.labels) {
				if (mergedClusters[label] == null) {
					mergedClusters[label] = cluster;
				} else {
					mergedClusters[label].mergeCluster(cluster);
				}
			}
		}
		this.clusters.clear();
	}

	public void addAdjacent(int idx) {
		if (!adjacents.contains(idx)) {
			degree++;
			adjacents.add(idx);
		}

	}

	public void setLabeled(boolean labeled) {
		if (labeled && !this.labeled) {
			Arrays.fill(this.vector, 0.0f);
		}
		this.labeled = labeled;
	}

	public void updateVector(int label, float value) {
		this.vector[label] += value;
		if (this.vector[label] > 1)
			this.vector[label] = 1;
	}

	public void updateVector(List<Integer> labels, float value) {
		for (int label : labels) {
			this.vector[label] += value;
			if (this.vector[label] > 1)
				this.vector[label] = 1;
		}
	}

	public Node getNextAdj(Node[] nodes, int label) {
		if (this.mergedClusters[label] == null) {
			int idx = random.nextInt(this.adjacents.size());
			//System.out.println("random adj.label = " + label);
			return nodes[adjacents.get(idx)];
		} else {
			int idx = random.nextInt(this.mergedClusters[label].nodes.size());
			return this.mergedClusters[label].nodes.get(idx);
		}
	}

	public void setClusterLabel(Node[] nodes, int labelSize) {
		for (Cluster cluster : clusters) {
			cluster.setLabel(labelSize);
		}
	}

	public void updateClusterNodeVector(Node[] nodes) {
		for (Cluster cluster : clusters) {
			if (cluster.labels == null)
				continue;
			cluster.updateNodeVector();
		}
	}

	public double cosSimilarity(Node nodeB) {
		double sum = 0;
		for (int i = 0, size = this.vector.length; i < size; i++) {
			float temp = this.vector[i] - nodeB.vector[i];
			sum += temp * temp;
		}
		sum = Math.sqrt(sum);
		return sum;
	}

	@Override
	public int compareTo(Node o) {
		return this.degree - o.degree;
	}

}
