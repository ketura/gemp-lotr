package com.gempukku.lotro;

public abstract class AbstractServer {
    private static ServerCleaner _cleaningTask = new ServerCleaner();

    private boolean _started;

    public void startServer() {
        if (!_started) {
            _cleaningTask.addServer(this);
            _started = true;
        }
    }

    public void stopServer() {
        if (_started) {
            _cleaningTask.removeServer(this);
            _started = false;
        }
    }

    protected abstract void cleanup();
}
