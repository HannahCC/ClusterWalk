package whu.cs.cl;


public class Utils {

	public static void arrayAdd(float[] clusterVector, float[] vector,
			int weight) {
		for (int i = 0, size = clusterVector.length; i < size; i++) {
			clusterVector[i] += vector[i] * weight;
		}
	}
}
