package whu.cs.cl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Node {
	static Random random = new Random(123456);
	int idx = -1;
	int degree = 0;
	List<Edge> adjacents = null;
	int clusterIdx = -1;
	double shift = 0;
	int leftBoundary = -1;
	int rightBoundary = -1;

	public Node(int idx) {
		this.idx = idx;
		adjacents = new ArrayList<>();
	}

	public void addAdjacent(int idx) {
		adjacents.add(new Edge(idx));
		degree++;
	}

	public void setCluster(int clusterIdx) {
		if (this.clusterIdx != -1) {
			throw new RuntimeException(
					"a node can not belong to more than one cluster.");
		}
		this.clusterIdx = clusterIdx;
	}

	public int getClusterOrder(Cluster[] clusters) {
		return clusters[clusterIdx].order;
	}

	public void sortEdge(Node[] nodes) {
		/*if (this.idx == 4092 || this.idx == 6562){
			System.out.println("debug");
		}*/
		for (Edge adjacent : adjacents) {
			adjacent.weight = nodes[adjacent.nodeIdx].shift;
		}
		Collections.sort(adjacents);
		int size = adjacents.size();
		this.leftBoundary = -1;
		this.rightBoundary = -1;
		double boundary = this.shift;
		for (int i = 0; i < size; i++) {
			if (adjacents.get(i).weight < boundary) {
				leftBoundary++;
				rightBoundary++;
			} else if (adjacents.get(i).weight == boundary) {
				rightBoundary++;
			} else if (adjacents.get(i).weight > boundary) {
				rightBoundary++;
				break;
			}
		}
		int idx = 0;
		Iterator<Edge> it = adjacents.iterator();
		while (it.hasNext()) {
			Edge adjacent = it.next();
			int d = (int) adjacent.weight - (int) this.shift;
			if (d < -1) { // 尽量向相邻的簇移动，除非没有邻居到相邻簇
				if (idx < leftBoundary) {
					it.remove();
				}
			} else if (d > 1) {
				if (idx > rightBoundary) {
					it.remove();
				}
			}
			idx++;
		}
		size = adjacents.size();
		this.leftBoundary = -1;
		for (int i = 0; i < size; i++) {
			if (adjacents.get(i).weight < boundary) { // <=会走的慢一些，但是有些节点只有相等的簇内邻居，另外这样会产生回路问题
				this.leftBoundary++;
			} else {
				break;
			}
		}

		this.rightBoundary = size;
		for (int i = size - 1; i >= 0; i--) {
			if (adjacents.get(i).weight > boundary) {
				this.rightBoundary--;
			} else {
				break;
			}
		}
	}

	public int getRandomLeftAdj() {
		int nextNodeIdx = -1;

		if (this.leftBoundary == -1) {
		} else if (this.leftBoundary == 0) {
			nextNodeIdx = this.adjacents.get(0).nodeIdx;
		} else {
			nextNodeIdx = this.adjacents.get(random.nextInt(this.leftBoundary)).nodeIdx;
		}
		return nextNodeIdx;
	}

	public int getRandomRightAdj() {
		int nextNodeIdx = -1;
		if (this.adjacents.size() == this.rightBoundary) {
		} else if (this.adjacents.size() - 1 == this.rightBoundary) {
			nextNodeIdx = this.adjacents.get(this.rightBoundary).nodeIdx;
		} else {
			nextNodeIdx = this.adjacents
					.get(this.rightBoundary
							+ random.nextInt(this.adjacents.size()
									- this.rightBoundary)).nodeIdx;
		}
		return nextNodeIdx;
	}
}
