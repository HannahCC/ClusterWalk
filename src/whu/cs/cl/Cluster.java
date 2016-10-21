package whu.cs.cl;

import java.util.HashSet;
import java.util.Set;


public class Cluster {
	Set<Integer> nodeIdxs = new HashSet<>();// 该簇有哪些节点
	int centroid = -1; // 簇的质心（Node）的索引

	public void addNode(Integer idx) {
		nodeIdxs.add(idx);
	}

	public void setCentroid(Node[] nodes) {
		int maxDegree = 0;
		for (Integer nodeIdx : nodeIdxs) {
			int degree = nodes[nodeIdx].degree;
			if (degree > maxDegree) {
				maxDegree = degree;
				this.centroid = nodeIdx;
			}
		}
		//System.out.println(maxDegree);
	}


}
