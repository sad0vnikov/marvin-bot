package net.sadovnikov.marvinbot.core.message;


public class SentMessage extends Message {

    private String recepientId;

    public void setRecepientId(String id) {
        this.recepientId = id;
    }

    public String getRecepientId() {
        return this.recepientId;
    }

    public void setText(String text) {
        this.text = text;
    }
}
