package com.gempukku.lotro;

import org.apache.log4j.Logger;

public abstract class AbstractServer {
    private static final Logger _logger = Logger.getLogger(AbstractServer.class);
    private static final ServerCleaner _cleaningTask = new ServerCleaner();

    private boolean _started;

    public void startServer() {
        if (!_started) {
            _cleaningTask.addServer(this);
            _started = true;
            _logger.debug("Started: "+getClass().getSimpleName());
            doAfterStartup();
        }
    }

    protected void doAfterStartup() {

    }

    public void stopServer() {
        if (_started) {
            _cleaningTask.removeServer(this);
            _started = false;
            _logger.debug("Stopped: "+getClass().getSimpleName());
        }
    }

    protected abstract void cleanup();
}
