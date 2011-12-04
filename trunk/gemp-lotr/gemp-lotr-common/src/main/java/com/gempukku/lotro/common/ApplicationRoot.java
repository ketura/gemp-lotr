package com.gempukku.lotro.common;

import java.io.File;

public class ApplicationRoot {
    private static File _root;

    public static void setRoot(File file) {
        _root = file;
    }

    public static File getRoot() {
        return _root;
    }
}
