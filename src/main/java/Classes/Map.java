package Classes;

import Collections.Array.UnorderedArrayList;
import Collections.Graph.Graph;
import Collections.Linked.LinkedUnorderedList;
import Exceptions.ElementNotFound;

public class Map<T> extends Graph<T> {

    /**
     * Constructs an empty Map.
     */
    public Map() {
        super();
    }

    /**
     * Returns the list of vertexes in the map.
     *
     * @return the list of vertexes
     */
    public UnorderedArrayList<T> getVertexes() {

        UnorderedArrayList<T> list = new UnorderedArrayList<>(numVertices);
        for (int i = 0; i < numVertices; i++) {
            list.addToRear(vertices[i]);
        }
        return list;
    }

    /**
     * Returns the adjacency matrix of the map.
     *
     * @return the adjacency matrix
     */
    public double[][] getMatrix() {
        return initializeMatrix(numVertices);
    }

    /**
     * Initializes the adjacency matrix with the specified size.
     *
     * @param size the size of the matrix
     * @return the initialized matrix
     */
    private double[][] initializeMatrix(int size) {

        double[][] matrix = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = adjMatrix[i][j] ? 1.0 : 0.0;
            }
        }
        return matrix;
    }

    /**
     * Returns the list of edges connected to the specified vertex.
     *
     * @param v the vertex
     * @return the list of edges
     * @throws ElementNotFound if the vertex is not found
     */
    public LinkedUnorderedList<T> getEdges(T v) throws ElementNotFound {
        int vertex = getIndex(v);
        if (!indexIsValid(vertex)) {
            throw new ElementNotFound("Vertex not valid");
        }

        LinkedUnorderedList<T> edges = new LinkedUnorderedList<>();
        for (int i = 0; i < numVertices; i++) {
            if (adjMatrix[vertex][i]) {
                edges.addToRear(vertices[i]);
            }
        }
        return edges;
    }

    /**
     * Returns the vertex at the specified index.
     *
     * @param i the index
     * @return the vertex, or null if the index is invalid
     */
    public T getVertex(int i) {
        if (!indexIsValid(i)) {
            return null;
        }
        return vertices[i];
    }

    /**
     * Returns the number of vertices in the map.
     *
     * @return the number of vertices
     */
    public int getNumVertices() {
        return numVertices;
    }

    /**
     * Returns a string representation of the map.
     *
     * @return a string representation of the map
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Vértices do Grafo:\n");
        for (int i = 0; i < numVertices; i++) {
            sb.append(i).append(": ").append(vertices[i].toString()).append("\n");
        }

        sb.append("\nMatriz de Adjacência:\n");
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                sb.append(adjMatrix[i][j] ? "1 " : "0 ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}