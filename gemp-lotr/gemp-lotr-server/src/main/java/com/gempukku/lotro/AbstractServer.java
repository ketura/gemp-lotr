package com.gempukku.lotro;

public abstract class AbstractServer {
    private boolean _started;
    private CleaningTask _cleaningTask;

    public final synchronized void startServer() {
        if (!_started) {
            _cleaningTask = new CleaningTask();
            new Thread(_cleaningTask).start();
            _started = true;
        }
    }

    public final synchronized void stopServer() {
        if (_started) {
            _cleaningTask.stop();
            _started = false;
        }
    }

    protected abstract void cleanup();

    private class CleaningTask implements Runnable {
        private boolean _running = true;

        public void run() {
            while (_running) {
                cleanup();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // Ignore
                }
            }
        }

        public void stop() {
            _running = false;
        }
    }
}
