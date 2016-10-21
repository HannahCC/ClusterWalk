package whu.cs.cl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Utils {

	static Random random = new Random(123456);

	public static void sortNodeEdge(Node[] nodes) {
		for (Node node : nodes) {
			node.sortEdge(nodes);
		}
	}

	public static void setNodeShift(Node[] nodes, Cluster[] clusters) {
		for (Cluster cluster : clusters) {
			double[] dist = dijkstra(cluster.centroid, nodes);
			double newShift = 0;
			for (int i = 0, size = nodes.length; i < size; i++) {
				if (i == cluster.centroid)
					continue;
				if (dist[i] == 0) {
					System.out.println("error@@@!!!");
				}
				newShift = 1 / dist[i];
				nodes[i].shift = newShift > nodes[i].shift ? newShift
						: nodes[i].shift;
			}
		}
		for (int i = 0, size = nodes.length; i < size; i++) {
			System.out.println(nodes[i].shift);
		}
	}

	/*
	 * public static void setClusterNeighbour(Node[] nodes, Cluster[] clusters)
	 * { double[][] distance = getCentroidDistance(nodes, clusters); for (int i
	 * = 0, size = clusters.length; i < size; i++) { int neighbourIdx = -1;
	 * double dist = Integer.MAX_VALUE; for (int j = i + 1; j < size; j++) { if
	 * (distance[i][j] < dist) { neighbourIdx = j; dist = distance[i][j]; } }
	 * clusters[i].nextClusterIdx = neighbourIdx; } }
	 */

	public static double[][] getCentroidDistance(Node[] nodes,
			Cluster[] clusters) {
		int clusterSize = clusters.length;
		double[][] distance = new double[clusterSize][clusterSize];
		for (int i = 0; i < clusterSize; i++) {
			Set<Integer> dests = new HashSet<>();
			for (Cluster cluster : clusters) {
				dests.add(cluster.centroid);
			}
			double[] dist = dijkstra(clusters[i].centroid, dests, nodes);
			for (int j = 0; j < clusterSize; j++) {
				distance[i][j] = dist[clusters[j].centroid];
			}
		}
		return distance;
	}

	public static double[] dijkstra(int start, Node[] nodes) {
		int size = nodes.length;
		double[] dist = new double[size];
		/*
		 * int[][] path = new int[size][size]; for(int
		 * i=0;i<size;i++){Arrays.fill(path[i], -1);}
		 */
		Arrays.fill(dist, Integer.MAX_VALUE);
		Set<Integer> s = new HashSet<>();
		List<HeapNode> heap = new ArrayList<HeapNode>();
		insertHeap(heap, new HeapNode(start, 0));
		dist[start] = 0;
		// path[start][0] = start;
		HeapNode top = null;
		while (null != (top = deleteMinHeap(heap))) {
			int nodeIdx = top.nodeIdx;
			s.add(nodeIdx);
			for (Edge adjacent : nodes[nodeIdx].adjacents) {
				int adjNodeIdx = adjacent.nodeIdx;
				if (s.contains(adjNodeIdx)) {
					continue;
				}
				double new_dist = dist[nodeIdx] + adjacent.weight;
				if (new_dist < dist[adjNodeIdx]) {
					dist[adjNodeIdx] = new_dist;
					/*
					 * for (int k = 0; k < size; k++) { path[adjNodeIdx][k] =
					 * path[nodeIdx][k]; if (path[nodeIdx][k] == nodeIdx) {
					 * path[adjNodeIdx][k + 1] = adjNodeIdx; break; } }
					 */
					int index = -1;
					if (-1 != (index = containsHeap(heap, adjNodeIdx, new_dist))) {
						siftUpHeap(heap, index);
					} else {
						insertHeap(heap, new HeapNode(adjNodeIdx, new_dist));
					}
				}
			}
		}
		return dist;
	}

	public static double[] dijkstra(int start, Set<Integer> dests, Node[] nodes) {
		int size = nodes.length;
		double[] dist = new double[size];
		/*
		 * int[][] path = new int[size][size]; for(int
		 * i=0;i<size;i++){Arrays.fill(path[i], -1);}
		 */
		Arrays.fill(dist, Integer.MAX_VALUE);
		Set<Integer> s = new HashSet<>();
		List<HeapNode> heap = new ArrayList<HeapNode>();
		insertHeap(heap, new HeapNode(start, 0));
		dist[start] = 0;
		// path[start][0] = start;
		while (true) {
			HeapNode top = deleteMinHeap(heap);
			int nodeIdx = top.nodeIdx;
			s.add(nodeIdx);
			if (dests.contains(nodeIdx)) {
				dests.remove(nodeIdx);
			}
			if (dests.size() == 0)
				break;
			for (Edge adjacent : nodes[nodeIdx].adjacents) {
				int adjNodeIdx = adjacent.nodeIdx;
				if (s.contains(adjNodeIdx)) {
					continue;
				}
				double new_dist = dist[nodeIdx] + adjacent.weight;
				if (new_dist < dist[adjNodeIdx]) {
					dist[adjNodeIdx] = new_dist;
					/*
					 * for (int k = 0; k < size; k++) { path[adjNodeIdx][k] =
					 * path[nodeIdx][k]; if (path[nodeIdx][k] == nodeIdx) {
					 * path[adjNodeIdx][k + 1] = adjNodeIdx; break; } }
					 */
					int index = -1;
					if (-1 != (index = containsHeap(heap, adjNodeIdx, new_dist))) {
						siftUpHeap(heap, index);
					} else {
						insertHeap(heap, new HeapNode(adjNodeIdx, new_dist));
					}
				}
			}
		}
		return dist;
	}

	public static void siftUpHeap(List<HeapNode> heap, int index) {
		HeapNode parent = null, self = null;
		int parentIdx = 1, tmpIdx = -1;
		double tmpDist = -1;
		while (index > 0) {
			parentIdx = ((index + 1) >> 1) - 1;
			parent = heap.get(parentIdx);
			self = heap.get(index);
			if (parent.distance <= self.distance) {
				break;
			} else {
				tmpIdx = parent.nodeIdx;
				tmpDist = parent.distance;
				parent.nodeIdx = self.nodeIdx;
				parent.distance = self.distance;
				self.nodeIdx = tmpIdx;
				self.distance = tmpDist;
			}
			index = parentIdx;
		}
	}

	public static int containsHeap(List<HeapNode> heap, int nodeIdx,
			double new_dist) {
		int index = -1;
		for (int i = 0, size = heap.size(); i < size; i++) {
			HeapNode heapNode = heap.get(i);
			if (heapNode.nodeIdx == nodeIdx) {
				heapNode.distance = new_dist;
				index = i;
				break;
			}
		}
		return index;
	}

	public static HeapNode deleteMinHeap(List<HeapNode> heap) {
		if (heap.size() == 0)
			return null;
		HeapNode top = heap.get(0);
		if (heap.size() > 1) {
			HeapNode new_top = heap.remove(heap.size() - 1);
			heap.set(0, new_top);
			siftDownHeap(heap, 0);
		} else {
			heap.remove(0);
		}
		return top;
	}

	public static void siftDownHeap(List<HeapNode> heap, int index) {
		HeapNode son = null, self = null;
		int min = index;
		int left = 0;
		int right = 0;
		int tmpIdx = -1;
		double tmpDist = -1;
		int size = heap.size();
		while (index < size) {
			left = (index << 1) + 1;
			right = (index << 1) + 2;
			if (left < size && heap.get(left).distance < heap.get(min).distance) {
				min = left;
			}
			if (right < size
					&& heap.get(right).distance < heap.get(min).distance) {
				min = right;
			}
			if (min == index)
				break;
			son = heap.get(min);
			self = heap.get(index);
			tmpIdx = son.nodeIdx;
			tmpDist = son.distance;
			son.nodeIdx = self.nodeIdx;
			son.distance = self.distance;
			self.nodeIdx = tmpIdx;
			self.distance = tmpDist;
			index = min;
		}
	}

	public static void insertHeap(List<HeapNode> heap, HeapNode heapNode) {
		heap.add(heapNode);
		siftUpHeap(heap, heap.size() - 1);
	}

	public static void setClusterCentroid(Node[] nodes, Cluster[] clusters) {
		for (Cluster cluster : clusters) {
			cluster.setCentroid(nodes);
		}
	}
}
