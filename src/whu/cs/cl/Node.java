package whu.cs.cl;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Node {
	static Random random = new Random();
	Integer idx = -1;
	Integer freq = 0;
	Set<Integer> clusterIds = null;

	public Node(Integer idx, Integer freq) {
		this.idx = idx;
		this.freq = freq;
		this.clusterIds = new HashSet<>();
	}

	public void addCluster(int cluster_id) {
		this.clusterIds.add(cluster_id);
	}

	public Integer getRandomCluster() {
		int num = random.nextInt(clusterIds.size());
		int i = 0, x = -1;
		for (int id : this.clusterIds) {
			if (i++ == num) {
				x = id;
				break;
			}
		}
		return x;
	}

	public void addFrequnce() {
		this.freq++;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Node) {
			Node obj = (Node) o;
			if (obj.idx == this.idx) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return idx.hashCode();
	}
}
