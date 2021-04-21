package com.gempukku.lotro.async;

import org.w3c.dom.Document;

import java.io.File;
import java.util.Map;

public interface ResponseWriter {
    public void writeError(int status);
    public void writeError(int status, Map<String, String> headers);

    public void writeFile(File file, Map<String, String> headers);

    public void writeHtmlResponse(String html);
    public void writeJsonResponse(String html);

    public void writeByteResponse(byte[] bytes, Map<? extends CharSequence, String> headers);

    public void writeXmlResponse(Document document);

    public void writeXmlResponse(Document document, Map<? extends CharSequence, String> addHeaders);
}
