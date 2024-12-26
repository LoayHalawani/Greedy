import java.util.*;

public class Graph {

	private class Edge {
		public int u;
		public int v;
		public int w;

		public Edge(int u, int v, int w) {
			this.u = u;
			this.v = v;
			this.w = w;
		}

		@Override
		public String toString() {
			char n1 = (char) (u + 'a');
			char n2 = (char) (v + 'a');
			return n1 + " -> " + n2 + " (" + w + ")";
		}
	}

	private List<List<Edge>> adj;
	private List<Edge> mst;
	private PriorityQueue<Edge> queue;
	private List<Integer> visited;
	private int[] parent;
	private int[] distances;

	public Graph() {
		adj = new ArrayList<>();
	}

	// Weighted undirected graph
	private void addWeightedEdge(char n1, char n2, int w) {
		int u = n1 - 'a';
		int v = n2 - 'a';

		while (adj.size() <= Math.max(u, v)) {
        	adj.add(new ArrayList<>());
    	}
		
		adj.get(u).add(new Edge(u, v, w));
		adj.get(v).add(new Edge(v, u, w));
	}

	// Prim's
	private void prim() {
		queue = new PriorityQueue<>(Comparator.comparingInt(e -> e.w));

		visited = new ArrayList<>();
		mst = new ArrayList<>();

		visited.add(0);

		for(Edge e : adj.get(0)) {
			queue.add(e);
		}

		while(!queue.isEmpty()) {
			Edge e1 = queue.poll();
			if(!visited.contains(e1.v)) {
				mst.add(e1);
				visited.add(e1.v);
				for(Edge e2 : adj.get(e1.v)) {
					if(!visited.contains(e2.v)) {
						queue.add(e2);
					}
				}
			}
		}

		printMST();
	}

	// Kruskal's
	private void kruskal() {
		List<Edge> edges = new ArrayList<>();
		mst = new ArrayList<>();

		for(List<Edge> e : adj) {
			edges.addAll(e);
		}

		edges.sort(Comparator.comparingInt(e -> e.w));

		makeSet();

		for(Edge e : edges) {
			int rootX = findSet(e.u);
			int rootY = findSet(e.v);

			if(rootX != rootY) {
				mst.add(e);
				union(e.u, e.v);
			}
		}

		printMST();
	}

	// MAKE-SET()
	private void makeSet() {
		parent = new int[adj.size()];
		for(int x = 0; x < parent.length; x ++) {
			parent[x] = x;
		}
	}

	// FIND-SET(x)
	private int findSet(int x) {
		if(x == parent[x]) {
			return x;
		}
		return findSet(parent[x]);
	}

	// UNION(x, y)
	private void union(int x, int y) {
		int rootX = findSet(x);
		int rootY = findSet(y);
		if(rootX != rootY) {
			parent[rootX] = rootY;
		}
	}

	// Display minimum spanning tree
	private void printMST() {
		for(Edge e : mst) {
			System.out.println(e);
		}
	}

	// Djikstra's
	private void djikstra(char n) {
		int u = n - 'a';
		initialize(u);
		visited = new ArrayList<>();
		queue = new PriorityQueue<>(Comparator.comparingInt(e -> distances[e.v]));

	    queue.add(new Edge(u, u, 0));

	    while (!queue.isEmpty()) {
	        Edge e1 = queue.poll();
	        if (!visited.contains(e1.v)) {
		        visited.add(e1.v);
		        for (Edge e2 : adj.get(e1.v)) {
		            if (!visited.contains(e2.v) && distances[e1.v] != Integer.MAX_VALUE) {
		            	relax(e1.v, e2.v, e2.w);
		                queue.add(new Edge(e1.v, e2.v, distances[e2.v]));
		            }
		        }
		    }
	    }

		printDistances(n);
	}

	// INITIALIZE(s)
	private void initialize(int u) {
		distances = new int[adj.size()];
		Arrays.fill(distances, Integer.MAX_VALUE);
		distances[u] = 0;
	}

	// RELAX(u, v, w)
	private void relax(int u, int v, int w) {
		if(distances[v] > distances[u] + w) {
			distances[v] = distances[u] + w;
		}
	}

	// Display shortest paths
	private void printDistances(char n) {
		for(int i = 0; i < distances.length; i ++) {
			char n2 = (char) (i + 'a');
            System.out.println(n + " -> " + n2 + " = " + distances[i]);
        }
	}	
	
	public static void main(String[] args) {
		Graph graph = new Graph();

		graph.addWeightedEdge('a', 'b', 15);
		graph.addWeightedEdge('a', 'f', 10);
		graph.addWeightedEdge('b', 'c', 14);
		graph.addWeightedEdge('b', 'g', 6);
		graph.addWeightedEdge('c', 'g', 3);
		graph.addWeightedEdge('c', 'd', 9);
		graph.addWeightedEdge('d', 'e', 1);
		graph.addWeightedEdge('e', 'g', 8);
		graph.addWeightedEdge('e', 'f', 19);
		graph.addWeightedEdge('f', 'g', 11);

		System.out.println("Prim's:");
		graph.prim();

		System.out.println("\nKruskal's:");
		graph.kruskal();

		System.out.println("\nDjikstra's:");
		graph.djikstra('a');
	}
}