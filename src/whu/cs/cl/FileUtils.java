package whu.cs.cl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FileUtils {
	public static void readGraph(String graphFile, Map<Integer, Node> nodes)
			throws IOException {
		File f = new File(graphFile);
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line = null;
		while (null != (line = br.readLine())) {
			String[] items = line.split("\\s+");
			int idx1 = Integer.parseInt(items[0]);
			int idx2 = Integer.parseInt(items[1]);
			if (nodes.containsKey(idx1)) {
				nodes.get(idx1).addFrequnce();
			} else {
				nodes.put(idx1, new Node(idx1, 1));
			}
			if (nodes.containsKey(idx2)) {
				nodes.get(idx2).addFrequnce();
			} else {
				nodes.put(idx2, new Node(idx2, 1));
			}
		}
		br.close();
	}

	public static void readCluster(String clusterFile,
			Map<Integer, Node> nodes, Map<Integer, Set<Node>> clusters)
			throws IOException {
		File f = new File(clusterFile);
		BufferedReader br = new BufferedReader(new FileReader(f));
		int i = 1;
		String line = null;
		while (null != (line = br.readLine())) {
			String[] items = line.split("\\s+");
			clusters.put(i, new HashSet<Node>());
			for (String item : items) {
				int idx = Integer.parseInt(item);
				if (nodes.containsKey(idx)) {
					Node node = nodes.get(idx);
					node.addCluster(i);
					clusters.get(i).add(node);
				} else {
					br.close();
					throw new RuntimeException(
							"Node in clusters didn't exsit in graph!");
				}
			}
			i++;
		}
		br.close();
	}
}
