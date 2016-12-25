package net.sadovnikov.marvinbot.core.domain.message;


import java.io.File;
import java.io.IOException;

public class ImageAttachment extends Attachment {

    public ImageAttachment(File file) throws IOException {
        super(file);
    }

    public ImageAttachment(byte[] bytes, String mime, String imageName) {
        super(bytes, mime, imageName);
    }

    @Override
    public boolean isImage() {
        return true;
    }
}
