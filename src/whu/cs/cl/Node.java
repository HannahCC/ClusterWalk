package whu.cs.cl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Node {
	static Random random = new Random(123456);
	int idx = -1;
	int degree = 0;
	List<Edge> adjacents = null;
	int clusterIdx = -1;
	double shift = 0;
	long shiftSum = 0;

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

	public void sortEdge(Node[] nodes) {
		/*
		 * if (this.idx == 4092 || this.idx == 6562){
		 * System.out.println("debug"); }
		 */
		for (Edge adjacent : adjacents) {
			adjacent.weight = (int) (nodes[adjacent.nodeIdx].shift*1000);
			shiftSum += adjacent.weight;
		}
		Collections.sort(adjacents);

	}

	public int getRandomNextAdj() {
		int num = random.nextInt((int)(shiftSum));
		int temp = 0;
		for (Edge adjacent : this.adjacents) {
			temp += adjacent.weight;
			if (num <= temp) {
				return adjacent.nodeIdx;
			}
		}
		return -1;
	}
}
