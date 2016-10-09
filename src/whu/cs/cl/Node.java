package whu.cs.cl;

import java.util.Random;
import java.util.Set;

public class Node {
	static Random random = new Random();
	Integer idx = -1;
	Integer freq = 0;
	Set<Integer> cluster_ids = null;

	public Node(Integer idx, Integer freq) {
		this.idx = idx;
		this.freq = freq;
	}

	public void addCluster(int cluster_id){
		this.cluster_ids.add(cluster_id);
	}
	
	public Integer getRandomCluster(){
		int num = random.nextInt(cluster_ids.size());
		int i = 0, x = -1;
		for(int id : this.cluster_ids){
			if(i++==num){x = id;break;}
		}
		return x;
	}
	
	public void addFrequnce(){
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
