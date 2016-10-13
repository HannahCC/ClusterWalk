package whu.cs.cl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Walk implements Runnable {

	int length = 80;
	int round = -1;
	Node[] nodes = null;
	Cluster[] clusters = null;
	Random random = new Random();

	public Walk(int round, int length, Node[] nodes, Cluster[] clusters) {
		this.round = round;
		this.length = length;
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
			Node lastNode = node;
			Node nextNode = null;
			int nextNodeIdx = 0;
			walk.add(node.idx);
			lastNode = node;
			do {
				nextNodeIdx = lastNode.getRandomNextAdj();
				if (nextNodeIdx == -1)
					break;
				nextNode = nodes[nextNodeIdx];
				walk.add(nextNode.idx);
				lastNode = nextNode;
			} while (walk.size() < length);

			for (Integer w : walk) {
				System.out.print(w + "/" + nodes[w - 1].clusterIdx + "\t");
			}
			System.out.println();

			try {
				FileUtils.writeWalk(round, walk);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
