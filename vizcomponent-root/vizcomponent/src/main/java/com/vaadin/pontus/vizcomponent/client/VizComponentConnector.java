package com.vaadin.pontus.vizcomponent.client;

import java.util.logging.Logger;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.vaadin.client.MouseEventDetailsBuilder;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.pontus.vizcomponent.VizComponent;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.shared.ui.Connect;

// Connector binds client-side widget class to server-side component class
// Connector lives in the client and the @Connect annotation specifies the
// corresponding server-side component
@SuppressWarnings("serial")
@Connect(VizComponent.class)
public class VizComponentConnector extends AbstractComponentConnector {

    // ServerRpc is used to send events to server. Communication implementation
    // is automatically created here
    VizComponentServerRpc rpc = RpcProxy.create(VizComponentServerRpc.class,
            this);

    public VizComponentConnector() {

        // To receive RPC events from server, we register ClientRpc
        // implementation
        registerRpc(VizComponentClientRpc.class, new VizComponentClientRpc() {

            @Override
            public void addNodeCss(String nodeId, String property, String value) {
                getWidget().addNodeCss(nodeId, property, value);

            }

            @Override
            public void addEdgeCss(String edgeId, String property, String value) {
                getWidget().addEdgeCss(edgeId, property, value);

            }

            @Override
            public void addNodeTextCss(String nodeId, String property,
                    String value) {
                getWidget().addNodeTextCss(nodeId, property, value);

            }

            @Override
            public void addEdgeTextCss(String edgeId, String property,
                    String value) {
                getWidget().addEdgeTextCss(edgeId, property, value);

            }
        });

    }

    class NodeClickHandler implements ClickHandler {
        @Override
        public void onClick(ClickEvent event) {
            Logger.getLogger("VizComponentConnector").info("Clicked");
            Element e = Element.as(event.getNativeEvent().getEventTarget());
            String nodeId = getWidget().getNodeId(e.getParentElement());

            Logger.getLogger("VizComponentConnector").info("Clicked 2");
            MouseEventDetails details = MouseEventDetailsBuilder
                    .buildMouseEventDetails(event.getNativeEvent(), getWidget()
                            .getElement());
            rpc.nodeClicked(nodeId, details);
            Logger.getLogger("VizComponentConnector").info("Clicked 3");
            event.stopPropagation();
            event.preventDefault();
            Logger.getLogger("VizComponentConnector").info("Clicked 4");
        }
    }

    class EdgeClickHandler implements ClickHandler {
        @Override
        public void onClick(ClickEvent event) {
            Element e = Element.as(event.getNativeEvent().getEventTarget());
            String edgeId = getWidget().getEdgeId(e.getParentElement());
            MouseEventDetails details = MouseEventDetailsBuilder
                    .buildMouseEventDetails(event.getNativeEvent(), getWidget()
                            .getElement());
            rpc.edgeClicked(edgeId, details);
            event.stopPropagation();
            event.preventDefault();
        }
    }

    // We must implement getWidget() to cast to correct type
    // (this will automatically create the correct widget type)
    @Override
    public VizComponentWidget getWidget() {
        return (VizComponentWidget) super.getWidget();
    }

    // We must implement getState() to cast to correct type
    @Override
    public VizComponentState getState() {
        return (VizComponentState) super.getState();
    }

    // Whenever the state changes in the server-side, this method is called
    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        if (stateChangeEvent.hasPropertyChanged("graph")
                || stateChangeEvent.hasPropertyChanged("graphType")
                || stateChangeEvent.hasPropertyChanged("name")
                || stateChangeEvent.hasPropertyChanged("params")
                || stateChangeEvent.hasPropertyChanged("nodeParams")
                || stateChangeEvent.hasPropertyChanged("edgeParams")) {
            updateGraph();
        }
        if (stateChangeEvent.hasPropertyChanged("height")
                || stateChangeEvent.hasPropertyChanged("width")) {
            updateSvgSize();
        }
    }

    private void updateGraph() {
        getWidget().renderGraph(getState());
        getWidget().addNodeClickHandler(new NodeClickHandler());
        getWidget().addEdgeClickHandler(new EdgeClickHandler());
    }

    private void updateSvgSize() {
        getWidget().updateSvgSize();
    }

}