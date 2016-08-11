package net.sadovnikov.marvinbot.plugins.xkcd_plugin.image;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


public class NumeredXkcdImage extends LastXkcdImage {

    public NumeredXkcdImage(int num) throws IOException {
        URL dataUrl = getComicsDataUrl(num);

        try {
            loadComicsData(dataUrl);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    protected URL getComicsDataUrl(int comicsNum) throws MalformedURLException {
        return new URL(XKCD_URL + String.valueOf(comicsNum) + "/" + JSON_DOC);
    }


}
