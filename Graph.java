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

	private List<Edge>[] adj;
	private List<Edge> mst;
	private PriorityQueue<Edge> pq;
	private boolean[] visited;
	private int[] parent;
	private int[] distances;

	@SuppressWarnings("unchecked")
	public Graph(int V) {
		adj = new ArrayList[V];
		for(int i = 0; i < adj.length; i ++) {
			adj[i] = new ArrayList<>();
		}
	}

	private void initializeVisited() {
		visited = new boolean[adj.length];
		for(int i = 0; i < visited.length; i ++) {
			visited[i] = false;
		}
	}

	private void addWeightedEdge(char n1, char n2, int w) {
		int u = n1 - 'a';
		int v = n2 - 'a';
		
		adj[u].add(new Edge(u, v, w));
		adj[v].add(new Edge(v, u, w));
	}

	private void printMST() {
		for(Edge e : mst) {
			System.out.println(e);
		}
	}

	// Prim's
	private void prim() {
		initializeVisited();
		pq = new PriorityQueue<>(Comparator.comparingInt(e -> e.w));
		mst = new ArrayList<>(adj.length - 1);

		visited[0] = true;
		for(Edge e : adj[0]) {
			pq.add(e);
		}

		while(!pq.isEmpty()) {
			Edge curr = pq.poll();

			int u = curr.v;
			if(!visited[u]) {
				mst.add(curr);
				visited[u] = true;

				for(Edge e : adj[u]) {
					int v = e.v;
					if(!visited[v]) {
						pq.add(e);
					}
				}
			}
		}

		printMST();
	}

	// Kruskal's
	private void kruskal() {
		mst = new ArrayList<>(adj.length - 1);

		List<Edge> edges = new ArrayList<>();
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
		parent = new int[adj.length];
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
			parent[rootY] = rootX;
		}
	}

	// Djikstra's
	private void djikstra(char n) {
		int s = n - 'a';

		initializeVisited();
		pq = new PriorityQueue<>(Comparator.comparingInt(e -> distances[e.v]));

		initialize(s);

	    pq.add(new Edge(s, s, 0));

	    while(!pq.isEmpty()) {
	        Edge curr = pq.poll();

	        int u = curr.v;
	        if(!visited[u]) {
		        visited[u] = true;

		        for(Edge e : adj[u]) {
		        	int v = e.v;
		        	int w = e.w;
		            if (!visited[v] && distances[u] != Integer.MAX_VALUE) {
		            	relax(u, v, w);
		                pq.add(new Edge(u, v, distances[v]));
		            }
		        }
		    }
	    }

		printDistances(n);
	}

	// INITIALIZE(s)
	private void initialize(int u) {
		distances = new int[adj.length];
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
		Graph graph = new Graph(7);

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