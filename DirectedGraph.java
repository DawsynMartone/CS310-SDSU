package edu.sdsu.cs.datastructures;
/**
 * Program #3
 * Dawsyn Martone
 * cssc0777
 * Jorden Cook
 * cssc0776
 */

import java.util.*;

public class DirectedGraph<V extends Comparable<V>> implements IGraph<V> {
    TreeMap<V, Vertex> MapOfLocations = new TreeMap<>();

    public DirectedGraph() {
    }

    @Override
    public void add(V vertexLabel) {
        if (!contains(vertexLabel)) {
            MapOfLocations.put(vertexLabel, new Vertex(vertexLabel));
        }
    }

    @Override
    public void clear() {
        MapOfLocations.clear();
    }

    //@Override
    public boolean contains(V label) {
        return MapOfLocations.containsKey(label);
    }

    @Override
    public void connect(V start, V destination) {
        try {
            Iterator<Vertex> vertexIterator = MapOfLocations.values().iterator();
            Vertex i = null, j = null;
            while (vertexIterator.hasNext()) {
                Vertex lilUziVert = vertexIterator.next();
                if (start.compareTo(lilUziVert.vertexLabel) == 0)
                    i = lilUziVert;
                if (destination.compareTo(lilUziVert.vertexLabel) == 0)
                    j = lilUziVert;
            }
            if (i == null || j == null)
                vertexIterator.next();
            i.addNewLocations(destination);
        } catch (NoSuchElementException e) {
            throw e;
        }
    }

    @Override
    public boolean isConnected(V start, V destination) {
        return !shortestPath(start, destination).isEmpty();
    }

    @Override
    public void disconnect(V start, V destination) {
        try {
            Iterator<Vertex> vertexIterator = MapOfLocations.values().iterator();
            Vertex i = null, j = null;
            while (vertexIterator.hasNext()) {
                Vertex v = vertexIterator.next();
                if (start.compareTo(v.vertexLabel) == 0)
                    i = v;
                if (destination.compareTo(v.vertexLabel) == 0)
                    j = v;
            }
            if (i == null || j == null)
                vertexIterator.next();
            i.deleteVisited(destination);

        } catch (NoSuchElementException e) {
            throw e;
        }
    }

    @Override
    public Iterable<V> vertices() {
        LinkedList<V> list = new LinkedList<>();
        for (V v : MapOfLocations.keySet())
            list.add(v);
        return list;
    }

    @Override
    public Iterable<V> neighbors(V vertexlabel) {
        try {
            Iterator<Vertex> vertexIterator = MapOfLocations.values().iterator();
            while (vertexIterator.hasNext()) {
                Vertex v = vertexIterator.next();
                if (vertexlabel.compareTo(v.vertexLabel) == 0)
                    return v.listOfVisited;
            }
            vertexIterator.next();
            return null;
        } catch (NoSuchElementException e) {
            throw e;
        }
    }

    @Override
    public void remove(V vertexLabel) {
        try {
            Vertex i = null;
            Iterator<Vertex> vertexIterator = MapOfLocations.values().iterator();
            while (vertexIterator.hasNext()) {
                Vertex v = vertexIterator.next();
                if (vertexLabel.compareTo(v.vertexLabel) == 0)
                    i = v;
            }
            if (i == null)
                vertexIterator.next();
            for (Vertex v : MapOfLocations.values()) {
                if (v.listOfVisited.contains(i.vertexLabel))
                    disconnect(v.vertexLabel, vertexLabel);
            }
            MapOfLocations.remove(i.vertexLabel, i);
        } catch (NoSuchElementException e) {
            throw e;
        }
    }

    @Override
    public IGraph<V> connectedGraph(V origin) {
        DirectedGraph<V> connectedGraph = new DirectedGraph<>();
        try {
            Vertex i = null;
            Iterator<Vertex> vertexIterator = MapOfLocations.values().iterator();
            while (vertexIterator.hasNext()) {
                Vertex v = vertexIterator.next();
                if (origin.compareTo(v.vertexLabel) == 0)
                    i = v;
            }
            if (i == null)
                vertexIterator.next();
            connectedGraph.add(origin);
            V previous = origin;
            for (V v : MapOfLocations.keySet()) {
                if (isConnected(previous, v)) {
                    connectedGraph.add(v);
                    connectedGraph.connect(previous, v);
                    previous = v;
                }
            }
        } catch (NoSuchElementException e) {
            throw e;
        }
        return connectedGraph;
    }

    @Override
    public List<V> shortestPath(V start, V destination) {
        Vertex i = null, k = null;
        try {
            Iterator<Vertex> vertexIterator = MapOfLocations.values().iterator();

            while (vertexIterator.hasNext()) {
                Vertex v = vertexIterator.next();
                if (start.compareTo(v.vertexLabel) == 0)
                    i = v;
                if (destination.compareTo(v.vertexLabel) == 0)
                    k = v;
            }
            if (i == null || k == null) {
                vertexIterator.next();
            }
        } catch (NoSuchElementException e) {
            throw e;
        }
        computeRoute(MapOfLocations.get(start), destination);
        return getShortestPathTo(destination, start);
    }

    ArrayList<V> getShortestPathTo(V target, V origin) {
        ArrayList<V> route = new ArrayList<>();
        for (Vertex vertex = MapOfLocations.get(target); vertex != null; vertex = MapOfLocations.get(vertex.parent)) {
            route.add(vertex.vertexLabel);
            if (vertex.parent == null)
                break;
        }
        if (route.size() == 1 && !MapOfLocations.get(origin).listOfVisited.contains(target))
            return new ArrayList<>();
        if (MapOfLocations.get(origin).listOfVisited.isEmpty())
            return new ArrayList<>();
        Collections.reverse(route);
        return route;
    }

    void computeRoute(Vertex start, V destination) {
        start.values = 0;
        PriorityQueue<V> QofDestinations = new PriorityQueue<>();
        QofDestinations.add(start.vertexLabel);
        while (!QofDestinations.isEmpty()) {

            V current = QofDestinations.poll();

            if (MapOfLocations.get(current).listOfVisited.isEmpty()) {

                return;
            }
            for (V v : neighbors(current)) {

                int distance = MapOfLocations.get(current).values + 1;

                if (distance >= MapOfLocations.get(v).values) {

                    QofDestinations.remove(v);

                    MapOfLocations.get(v).values = distance;

                    MapOfLocations.get(v).parent = current;

                    if (destination.compareTo(MapOfLocations.get(v).vertexLabel) == 0)

                        return;

                    QofDestinations.add(v);

                }
            }
        }
    }

    @Override
    public int size() {
        return MapOfLocations.size();
    }

    private class Vertex implements Comparable<V> {
        V vertexLabel;
        ArrayList<V> listOfVisited = new ArrayList<>();
        int values = -1;
        V parent;

        Vertex(V vertexLabel) {
            this.vertexLabel = vertexLabel;
        }

        void addNewLocations(V dest) {
            if (!listOfVisited.contains(dest))
                listOfVisited.add(dest);
        }

        void deleteVisited(V dest) {
            if (listOfVisited.contains(dest))
                listOfVisited.remove(dest);
        }

        @Override
        public int compareTo(V vertex) {
            return vertexLabel.compareTo(vertex);
        }
    }
}