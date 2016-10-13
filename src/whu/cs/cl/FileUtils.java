package whu.cs.cl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

	static List<BufferedWriter> bws = null;

	public static void readGraph(String graphFile, Node[] nodes)
			throws IOException {
		File f = new File(graphFile);
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line = null;
		while (null != (line = br.readLine())) {
			String[] items = line.split("\\s+");
			int idx1 = Integer.parseInt(items[0]);
			int idx2 = Integer.parseInt(items[1]);
			nodes[idx1-1].addAdjacent(idx2-1);
		}
		br.close();
	}

	public static void readCluster(String clusterFile,
			Node[] nodes, Cluster[] clusters )
			throws IOException {
		File f = new File(clusterFile);
		BufferedReader br = new BufferedReader(new FileReader(f));
		int i = 0;
		String line = null;
		while (null != (line = br.readLine())) {
			String[] items = line.split("\\s+");
			clusters[i] = new Cluster();
			for (String item : items) {
				int idx = Integer.parseInt(item)-1;
				clusters[i].addNode(idx);
				nodes[idx].setCluster(i);
			}
			i++;
		}
		br.close();
	}

	public static void initWalkFiles(String walkDirName, String walkFileName,
			int rounds) throws IOException {
		bws = new ArrayList<BufferedWriter>(rounds);
		File walkDir = new File(walkDirName);
		if (!walkDir.exists()) {
			walkDir.mkdirs();
		}
		for (int r = 0; r < rounds; r++) {
			File walkFile = new File(walkFileName + "." + r);
			BufferedWriter bw = new BufferedWriter(new FileWriter(walkFile));
			bws.add(bw);
		}
	}

	public static void mergeWalkFiles(String walkFileName, int rounds)
			throws IOException {
		for (int r = 0; r < rounds; r++) {
			bws.get(r).flush();
			bws.get(r).close();
		}
		File walkFile = new File(walkFileName);
		BufferedWriter bw = new BufferedWriter(new FileWriter(walkFile));
		for (int r = 0; r < rounds; r++) {
			File walkFile_r = new File(walkFileName + "." + r);
			BufferedReader br = new BufferedReader(new FileReader(walkFile_r));
			String line = null;
			while (null != (line = br.readLine())) {
				bw.write(line + "\r\n");
			}
			br.close();
			walkFile_r.delete();
		}
		bw.flush();
		bw.close();
	}

	public static void writeWalk(int round, List<Integer> walk)
			throws IOException {
		BufferedWriter bw = bws.get(round);
		for (Integer node : walk) {
			bw.write(node + " ");
		}
		bw.write("\r\n");
	}

}
