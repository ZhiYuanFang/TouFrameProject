package xyz.ttyz.mylibrary.socket.websocket;

public class ByteResponse implements Response<byte[]>{
    byte[] responseBytes;

    public ByteResponse(byte[] responseBytes) {
        this.responseBytes = responseBytes;
    }

    @Override
    public byte[] getResponseBytes() {
        return responseBytes;
    }

    @Override
    public String getResponseText() {
        return null;
    }

    @Override
    public void setResponseText(String responseText) {

    }

    @Override
    public byte[] getResponseEntity() {
        return new byte[0];
    }

    @Override
    public void setResponseEntity(byte[] responseEntity) {

    }
}
