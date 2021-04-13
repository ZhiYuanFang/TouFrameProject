package xyz.ttyz.tourfrxohc.event;

public class VoiceEvent {
    byte[] voices;

    public VoiceEvent(byte[] voices) {
        this.voices = voices;
    }

    public byte[] getVoices() {
        return voices;
    }
}
