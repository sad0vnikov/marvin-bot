package net.sadovnikov.marvinbot.plugins.xkcd_plugin.image;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import static java.lang.Math.toIntExact;

/**
 * Loads images from xkcd.com using JSON api
 */
public class LastXkcdImage implements XkcdImage {

    protected final String XKCD_URL = "http://xkcd.com/";
    protected final String JSON_DOC = "info.0.json";

    protected byte[] image;
    protected String fileName;
    protected Long num;
    protected URL imageUrl;
    protected String description;

    /**
     * The newest bytes will be loaded
     */
    public LastXkcdImage() throws IOException {
        URL dataUrl = getComicsDataUrl();

        try {
            loadComicsData(dataUrl);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


    public byte[] bytes() throws IOException {

        if (this.image == null) {
            this.image = this.loadFile();
        }

        return this.image;

    }

    public String name() {
        return fileName;
    }

    public int num() {
        return toIntExact(this.num);
    }

    public String description() {
        return description;
    }

    protected URL getComicsDataUrl() throws MalformedURLException {
        return new URL(XKCD_URL + JSON_DOC);
    }

    protected void loadComicsData(URL comicsDataUrl) throws IOException, ParseException {
        URLConnection conn = comicsDataUrl.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String json = "";
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            json += inputLine;
        }

        JSONParser jsonParser = new JSONParser();
        JSONObject obj = (JSONObject) jsonParser.parse(json);

        String imageUrl = (String) obj.get("img");
        this.imageUrl = new URL(imageUrl);
        Long numStr = (Long) obj.get("num");

        this.num = numStr;
        this.description = (String) obj.get("alt");

        loadFile();
    }

    protected byte[] loadFile() throws IOException {
        String[] urlParts = this.imageUrl.getPath().split("/");
        String fileName = urlParts[urlParts.length - 1];
        this.fileName = fileName;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        URL url = this.imageUrl;


        try (InputStream in = url.openStream()) {
            byte[] buffer = new byte[1024];
            int n = 0;
            while ((n = in.read(buffer)) != -1) {
                baos.write(buffer, 0, n);
            }
        }

        return baos.toByteArray();
    }

}
