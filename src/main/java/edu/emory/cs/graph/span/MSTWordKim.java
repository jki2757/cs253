package edu.emory.cs.graph.span;

import edu.emory.cs.graph.Edge;
import edu.emory.cs.graph.Graph;
import edu.emory.cs.set.DisjointSet;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.*;

public class MSTWordKim extends MSTWord {
    public MSTWordKim(InputStream in) {
        super(in);
    }

    static public void main(String[] args) throws Exception {
        final String INPUT_FILE = "src/main/resources/word_vectors.txt";
        final String OUTPUT_FILE = "src/main/resources/word_vectors.dot";

        MSTWord mst = new MSTWordKim(new FileInputStream(INPUT_FILE));
        SpanningTree tree = mst.getMinimumSpanningTree();
        mst.printSpanningTree(new FileOutputStream(OUTPUT_FILE), tree);
        System.out.println(tree.getTotalWeight());
        List<String> result = mst.getShortestPath(2, 100);
        System.out.print(result);
    }

    @Override
    protected Graph createGraph() {
        // TODO: to be filled
        Graph graph = new Graph(super.vertices.size());

        for (int i = 0; i < graph.size() - 1; i++) {
            double[] a = super.vertices.get(i).getVector();

            for (int j = i + 1; j < graph.size(); j++) {
                //if(super.vertices.get(i).getWord() != null) {
                double[] b = super.vertices.get(j).getVector();
                double product = 0;
                double aSum = 0;
                double bSum = 0;

                for (int k = 0; k < 50; k++) { // alternative: stream API
                    product += a[k] * b[k];
                    aSum += Math.pow(a[k], 2);
                    bSum += Math.pow(b[k], 2);
                }
                product = 1 - product / (Math.sqrt(aSum) * Math.sqrt(bSum));
                graph.setUndirectedEdge(i, j, product);
            }
        }
        return graph;
    }

    @Override
    public SpanningTree getMinimumSpanningTree() {
        // TODO: to be filled
        PriorityQueue<Edge> queue = new PriorityQueue<>(super.graph.getAllEdges());
        DisjointSet forest = new DisjointSet(super.graph.size());
        SpanningTree tree = new SpanningTree();
        Edge edge;

        while (!queue.isEmpty()) {
            edge = queue.poll();

            if (!forest.inSameSet(edge.getTarget(), edge.getSource())) {
                tree.addEdge(edge);

                // a spanning tree is found
                if (tree.size() + 1 == super.graph.size()) break;
                // merge forests
                forest.union(edge.getTarget(), edge.getSource());
            }
        }

        return tree;
    }

    @Override
    public List<String> getShortestPath(int source, int target) {

        SpanningTree tree = this.getMinimumSpanningTree();
        Graph graph = new Graph(tree.size() + 1);


        for (int i = 0; i < tree.getEdges().size(); i++) {
            graph.setUndirectedEdge(tree.getEdges().get(i).getSource(), tree.getEdges().get(i).getTarget(), tree.getEdges().get(i).getWeight());
        }

        PriorityQueue<VertexDistancePair> queue = new PriorityQueue<>();
        Integer[] previous = new Integer[tree.size() + 1];
        double[] distances = new double[tree.size() + 1];
        Set<Integer> visited = new HashSet<>();
        List<String> result = new ArrayList<>();

        init(distances, previous, target);
        queue.add(new VertexDistancePair(target, 0));

        while (!queue.isEmpty()) {
            VertexDistancePair u = queue.poll();
            visited.add(u.vertex);

            for (Edge edge : graph.getIncomingEdges(u.vertex)) {
                //Vertex that can be reached through current vertex
                int v = edge.getSource();

                //If the vertex has yet been visited
                if (!visited.contains(v)) {
                    //Calculated distance from target to v
                    double dist = distances[u.vertex] + edge.getWeight();

                    if (dist < distances[v]) {
                        distances[v] = dist + heuristic(v, target);
                        previous[v] = u.vertex;
                        queue.add(new VertexDistancePair(v, dist));
                    }
                }
            }
        }

        int i = source;
        result.add(super.vertices.get(source).getWord());
        while (true) {
            if (i == target) break;
            result.add(super.vertices.get(previous[i]).getWord());
            i = previous[i];
        }

        return result;
    }

    private void init(double[] distances, Integer[] previous, int target) {
        for (int i = 0; i < distances.length; i++) {
            //Set distance from target to target as the heuristic value
            if (i == target)
                distances[i] = heuristic(i, target);
            else {
                //Initialize all distance to infinity
                distances[i] = Double.MAX_VALUE;
                //Initialize all previous vertices to null
                previous[i] = null;
            }
        }
    }

    protected double heuristic(int source, int target) {
        return 0;
    }

    private class VertexDistancePair implements Comparable<VertexDistancePair> {
        public int vertex;
        public double distance;

        public VertexDistancePair(int vertex, double distance) {
            this.vertex = vertex;
            this.distance = distance;
        }

        @Override
        public int compareTo(VertexDistancePair p) {
            double diff = this.distance - p.distance;
            if (diff > 0) return 1;
            if (diff < 0) return -1;
            return 0;
        }
    }
}