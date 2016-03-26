package message;

public class ReceivedMessage extends Message{

    private String senderId;

    public ReceivedMessage(String senderId, String text) {
        this.text = text;
        this.senderId = senderId;
    }

    public String getSenderId() {
        return this.senderId;
    }
}
