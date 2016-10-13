package whu.cs.cl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Walk implements Runnable {

	static final int maxWalkLength = 200;
	int round = -1;
	Node[] nodes = null;
	Cluster[] clusters = null;
	Random random = new Random();

	public Walk(int round, Node[] nodes, Cluster[] clusters) {
		this.round = round;
		this.nodes = nodes;
		this.clusters = clusters;
	}

	@Override
	public void run() {
		for (Node node : nodes) {
			if (node.idx == 4092 || node.idx == 6562) {
				System.out.println("bebug");
			}
			List<Integer> walk = new ArrayList<>();
			int order = node.getClusterOrder(clusters);
			// int preNodeIdx = -1;
			Node lastNode = node;
			Node nextNode = null;
			int nextNodeIdx = 0;
			if (order >= 1) { // 往左边走
				List<Integer> left_walk = new ArrayList<>();
				do {
					nextNodeIdx = lastNode.getRandomLeftAdj();
					if (nextNodeIdx == -1)
						break;
					nextNode = nodes[nextNodeIdx];
					left_walk.add(nextNode.idx);
					// preNodeIdx = lastNode.idx;
					lastNode = nextNode;
				} while (lastNode.getClusterOrder(clusters) > 1
						&& left_walk.size() < maxWalkLength);
				int leftSize = left_walk.size();
				for (int i = leftSize - 1; i >= 0; i--) {
					walk.add(left_walk.get(i));
				}
			}
			walk.add(node.idx);
			// preNodeIdx = -1;
			lastNode = node;
			if (order <= clusters.length) {// 往右边走
				do {
					nextNodeIdx = lastNode.getRandomRightAdj();
					if (nextNodeIdx == -1)
						break;
					nextNode = nodes[nextNodeIdx];
					walk.add(nextNode.idx);
					// preNodeIdx = lastNode.idx;
					lastNode = nextNode;
				} while (lastNode.getClusterOrder(clusters) < clusters.length
						&& walk.size() < maxWalkLength);
			}

			/*for (Integer w : walk) {
				System.out.print(w + "/"
						+ nodes[w - 1].getClusterOrder(clusters) + "\t");
			}
			System.out.println();*/
			try {
				FileUtils.writeWalk(round, walk);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
