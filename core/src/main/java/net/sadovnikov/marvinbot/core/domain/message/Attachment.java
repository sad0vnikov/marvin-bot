package net.sadovnikov.marvinbot.core.domain.message;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.file.Files;

public class Attachment {

    private File file;
    private String fileName;
    private byte[] bytes;
    private String mime;


    public Attachment(File file) throws IOException {
        this.file = file;
        this.fileName = file.getName();
        this.mime = Files.probeContentType(file.toPath());
    }

    public Attachment(byte[] bytes, String mimeType, String fileName) {
        this.bytes = bytes;
        this.fileName = fileName;
        this.mime = mimeType;
    }

    public File file() throws FileNotFoundException {
        if (file == null && bytes != null) {
            try {
                file = loadBytesIntoFile();
            } catch (IOException e) {
                throw new FileNotFoundException();
            }
        }
        return file;
    }

    public byte[] bytes() throws IOException {
        if (bytes == null && file != null) {
            try {
                bytes = getBytesFromFile();
            } catch (FileNotFoundException e) {
                throw new IOException();
            }
        }

        return bytes;
    }

    public String mimeType() {
        return this.mime;
    }

    protected byte[] getBytesFromFile() throws FileNotFoundException, IOException {
        InputStream in = new FileInputStream(file);
        return IOUtils.toByteArray(in);
    }

    protected File loadBytesIntoFile() throws IOException {
        File file = new File(System.getProperty("java.io.tmpdir"), this.fileName);
        FileUtils.writeByteArrayToFile(file, this.bytes);
        return file;
    }


    public boolean isImage() {
        return false;
    }

}
