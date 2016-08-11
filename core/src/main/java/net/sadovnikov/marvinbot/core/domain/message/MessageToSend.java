package net.sadovnikov.marvinbot.core.domain.message;


import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class MessageToSend extends Message {

    private String recepientId;
    private Set<Attachment> attachments = new HashSet<>();

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

    public Set<Attachment> attachments() {
        return this.attachments;
    }

    public void addFile(File file) {
        attachments.add(new Attachment(file));
    }

    public void addFile(byte[] file, String name) {
        attachments.add(new Attachment(file, name));
    }

    public void addImage(File file) {
        attachments.add(new ImageAttachment(file));
    }

    public void addImage(byte[] file, String imageName) {
        attachments.add(new ImageAttachment(file, imageName));
    }

    public boolean equals(MessageToSend message) {
        return message.text().equals(text) && message.recepientId().equals(recepientId);
    }

}
