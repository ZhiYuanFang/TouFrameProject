package xyz.ttyz.toubasemvvm.adapter.utils;

import jsbridge.BridgeHandler;

public class BridgeHandlerCommand {
    String handlerName;
    BridgeHandler bridgeHandler;

    public BridgeHandlerCommand(String handlerName, BridgeHandler bridgeHandler) {
        this.handlerName = handlerName;
        this.bridgeHandler = bridgeHandler;
    }

    public String getHandlerName() {
        return handlerName;
    }

    public void setHandlerName(String handlerName) {
        this.handlerName = handlerName;
    }

    public BridgeHandler getBridgeHandler() {
        return bridgeHandler;
    }

    public void setBridgeHandler(BridgeHandler bridgeHandler) {
        this.bridgeHandler = bridgeHandler;
    }
}
