package net.sadovnikov.marvinbot.core.domain.message;


import net.sadovnikov.marvinbot.core.domain.Channel;
import net.sadovnikov.marvinbot.core.service.chat.AbstractChat;
import net.sadovnikov.marvinbot.core.service.chat.Chat;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class MessageToSend extends Message {

    private AbstractChat chat;
    private Set<Attachment> attachments = new HashSet<>();

    public MessageToSend(String text, AbstractChat chat) {
        this.text = text;
        this.chat = chat;
    }

    public AbstractChat chat() {
        return this.chat;
    }

    public String text() {
        return this.text;
    }

    public Set<Attachment> attachments() {
        return this.attachments;
    }

    public void addFile(File file) throws IOException {
        attachments.add(new Attachment(file));
    }

    public void addFile(byte[] file, String mime, String name) {
        attachments.add(new Attachment(file, mime, name));
    }

    public void addImage(File file) throws IOException {
        attachments.add(new ImageAttachment(file));
    }

    public void addImage(byte[] file, String mime, String imageName) {
        attachments.add(new ImageAttachment(file, mime, imageName));
    }

    public boolean equals(MessageToSend message) {
        return message.text().equals(text) && message.chat().equals(chat);
    }

}
