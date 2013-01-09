package com.gempukku.lotro.async;

import org.w3c.dom.Document;

import java.io.File;
import java.util.Map;

public interface ResponseWriter {
    public void writeError(int status);
    public void writeError(int status, Map<String, String> headers);

    public void writeFile(File file);

    public void writeHtmlResponse(String html);

    public void writeByteResponse(String contentType, byte[] bytes);

    public void writeResponse(Document document);

    public void writeResponse(Document document, Map<String, String> addCookies);
}
