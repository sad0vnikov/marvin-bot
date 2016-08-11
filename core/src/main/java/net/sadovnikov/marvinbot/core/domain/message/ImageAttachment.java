package net.sadovnikov.marvinbot.core.domain.message;


import java.io.File;

public class ImageAttachment extends Attachment {

    public ImageAttachment(File file) {
        super(file);
    }

    public ImageAttachment(byte[] bytes, String imageName) {
        super(bytes, imageName);
    }

    @Override
    public boolean isImage() {
        return true;
    }
}
