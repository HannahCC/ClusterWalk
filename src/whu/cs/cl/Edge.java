package whu.cs.cl;

public class Edge implements Comparable<Edge> {

	int nodeIdx = -1;
	double weight = 1;

	public Edge(int nodeIdx) {
		this.nodeIdx = nodeIdx;
	}

	public Edge(int nodeIdx, double weight) {
		this.nodeIdx = nodeIdx;
		this.weight = weight;
	}

	@Override
	public int compareTo(Edge o) {
		if (this.weight > o.weight) {
			return 1;
		} else if (this.weight < o.weight) {
			return -1;
		}
		return 0;
	}
}
