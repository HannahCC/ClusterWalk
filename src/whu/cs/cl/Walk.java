package whu.cs.cl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Walk implements Runnable {

	int length;
	int round;
	Node[] nodes = null;
	CountDownLatch countDownLatch = null;

	public Walk(int round, int length, Node[] nodes,
			CountDownLatch countDownLatch) {
		this.round = round;
		this.length = length;
		this.nodes = nodes;
		this.countDownLatch = countDownLatch;
	}

	@Override
	public void run() {
		Node lastNode = null;
		Node nextNode = null;
		for (Node node : nodes) {
			if (node.fobidden)continue;
			for (int label = 0, size = node.mergedClusters.length; label < size; label++) {
				if (node.mergedClusters[label] == null)
					continue;
				List<Integer> walk = new ArrayList<>();
				lastNode = node;
				walk.add(node.id);
				while (walk.size() < length) {
					nextNode = lastNode.getNextAdj(nodes, label);
					walk.add(nextNode.id);
					lastNode = nextNode;
				}
				try {
					FileUtils.writeWalk(round, walk);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		this.countDownLatch.countDown();
	}
}
