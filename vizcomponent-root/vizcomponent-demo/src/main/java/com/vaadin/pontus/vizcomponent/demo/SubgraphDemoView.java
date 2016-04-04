package com.vaadin.pontus.vizcomponent.demo;

import com.vaadin.pontus.vizcomponent.VizComponent;
import com.vaadin.pontus.vizcomponent.VizComponent.EdgeClickEvent;
import com.vaadin.pontus.vizcomponent.VizComponent.NodeClickEvent;
import com.vaadin.pontus.vizcomponent.VizComponent.NodeClickListener;
import com.vaadin.pontus.vizcomponent.model.Graph;
import com.vaadin.pontus.vizcomponent.model.Subgraph;
import com.vaadin.pontus.vizcomponent.model.Subgraph.GraphNode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class SubgraphDemoView extends VerticalLayout {

    public SubgraphDemoView() {

        final VizComponent component = new VizComponent();

        Graph graph = new Graph("G", Graph.DIGRAPH);

        // Create one subgraph with two nodes
        Graph.Node node11 = new Graph.Node("n1");
        Graph.Node node12 = new Graph.Node("n2");
        Subgraph sub = new Subgraph();
        sub.setParam("style", "filled");
        sub.setParam("color", "lightgrey");
        sub.setParam("label", "\"sub #1\"");
        sub.addEdge(node11, node12);
        sub.addEdge(node12, node11);

        // Note that the name "cluster_0" is significant here
        GraphNode subNode = new GraphNode("cluster_0", sub);
        graph.addNode(subNode);

        // Create another subgraph with
        Graph.Node node21 = new Graph.Node("m1");
        Graph.Node node22 = new Graph.Node("m2");
        sub = new Subgraph();
        sub.setParam("color", "red");
        sub.setParam("label", "\"sub #2\"");
        sub.addEdge(node21, node22);
        sub.addEdge(node22, node21);

        subNode = new GraphNode("cluster_1", sub);
        graph.addNode(subNode);

        Graph.Node node1 = new Graph.Node("k1");
        Graph.Node node2 = new Graph.Node("k2");

        graph.addEdge(node1, node11);
        graph.addEdge(node12, node2);

        graph.addEdge(node1, node21);
        graph.addEdge(node22, node2);

        component.setWidth("400px");
        component.setHeight("300px");
        component.drawGraph(graph);

        Label label = new Label(
                "Example that demonstrates subgraphs. "
                        + "A node can either be a regular node or a node that contains another graph. "
                        + "The nodes in the graph are still clickable, but the node clusters (subgraphs)"
                        + " are not. Note also that having egdes to and from subgraphs directly, "
                        + "might not give the desired result. ");

        setSizeFull();
        addComponent(label);
        addComponent(component);
        setExpandRatio(component, 1);
        setComponentAlignment(component, Alignment.MIDDLE_CENTER);

        component.addClickListener(new NodeClickListener() {

            @Override
            public void nodeClicked(NodeClickEvent e) {
                Graph.Node node = e.getNode();
                component.addCss(node, "stroke", "blue");
                component.addTextCss(node, "fill", "blue");
            }

        });

        component.addClickListener(new VizComponent.EdgeClickListener() {

            @Override
            public void edgeClicked(EdgeClickEvent e) {
                component.addCss(e.getEdge(), "stroke", "blue");
                component.addTextCss(e.getEdge(), "fill", "blue");

            }

        });
    }
}
