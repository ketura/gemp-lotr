package com.gempukku.lotro.async;

import org.apache.log4j.Logger;

import java.util.concurrent.ConcurrentLinkedQueue;

public class LongPollingSystem {
    private static Logger _log = Logger.getLogger(LongPollingSystem.class);

    private ConcurrentLinkedQueue<LongPollingResource> _waitingActions = new ConcurrentLinkedQueue<LongPollingResource>();
    private long _pollingInterval = 200;
    private long _pollingLength = 10000;
    private ProcessingRunnable _processingRunnable;

    public LongPollingSystem() {
        _waitingActions.add(new PauseResource());
    }

    public void start() {
        _processingRunnable = new ProcessingRunnable();
        Thread thr = new Thread(_processingRunnable);
        thr.start();
    }

    public void appendLongPollingResource(LongPollingResource resource) {
        _waitingActions.add(new TimeoutLongPollingResource(resource));
    }

    private void pause() {
        try {
            Thread.sleep(_pollingInterval);
        } catch (InterruptedException exp) {
            // Ignore
        }
    }

    private class ProcessingRunnable implements Runnable {
        @Override
        public void run() {
            while (true) {
                LongPollingResource resource = _waitingActions.poll();
                try {
                    if (resource.isChanged())
                        resource.processIfNotProcessed();
                    else
                        _waitingActions.add(resource);
                } catch (Exception exp) {
                    _log.error("Error while processing a long-polled resource", exp);
                }
            }
        }
    }

    private class TimeoutLongPollingResource implements LongPollingResource {
        private long _start;
        private LongPollingResource _longPollingResource;

        private TimeoutLongPollingResource(LongPollingResource longPollingResource) {
            _longPollingResource = longPollingResource;
            _start = System.currentTimeMillis();
        }

        @Override
        public boolean isChanged() {
            boolean timeout = _start + _pollingLength < System.currentTimeMillis();
            return timeout || _longPollingResource.isChanged();
        }

        @Override
        public void processIfNotProcessed() {
            _longPollingResource.processIfNotProcessed();
        }
    }

    private class PauseResource implements LongPollingResource {
        @Override
        public boolean isChanged() {
            return true;
        }

        @Override
        public void processIfNotProcessed() {
            pause();
            _waitingActions.add(this);
        }
    }
}
