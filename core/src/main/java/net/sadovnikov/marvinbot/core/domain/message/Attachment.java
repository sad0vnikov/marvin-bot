package net.sadovnikov.marvinbot.core.domain.message;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;

public class Attachment {

    private File file;
    private String fileName;
    private byte[] bytes;


    public Attachment(File file) {
        this.file = file;
        this.fileName = file.getName();
    }

    public Attachment(byte[] bytes, String fileName) {
        this.bytes = bytes;
        this.fileName = fileName;
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
