package net.sadovnikov.marvinbot.plugins.xkcd_plugin.image;

import java.io.IOException;

public interface XkcdImage {

    public byte[] bytes() throws IOException;

    public String name();

    public int num();

    public String description();

    public String mimeType();
}
