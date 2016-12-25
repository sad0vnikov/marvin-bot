package net.sadovnikov.marvinbot.plugins.xkcd_plugin.image;

import java.io.IOException;
import java.util.Random;

public class RandomXkcdImage implements XkcdImage{

    LastXkcdImage image;

    public RandomXkcdImage() throws IOException {
        LastXkcdImage lastImage = new LastXkcdImage();
        int lastNum = lastImage.num();
        Random r = new Random();
        int randomNum = r.nextInt(lastNum);

        this.image = new NumeredXkcdImage(randomNum);
    }

    @Override
    public byte[] bytes() throws IOException {
        return image.bytes();
    }

    @Override
    public String name() {
        return image.name();
    }

    @Override
    public int num() {
        return image.num();
    }

    @Override
    public String description() {
        return image.description();
    }

    @Override
    public String mimeType() {
        return image.mimeType();
    }
}
