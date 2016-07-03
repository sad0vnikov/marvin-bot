package net.sadovnikov.marvinbot.core.domain.message;


public class MessageToSend extends Message {

    private String recepientId;


    public MessageToSend(String text, String chatId) {
        this.text = text;
        this.recepientId = chatId;
    }

    public String recepientId() {
        return this.recepientId;
    }

    public String text() {
        return this.text;
    }

    public boolean equals(MessageToSend message) {
        return message.text().equals(text) && message.recepientId().equals(recepientId);
    }

}
