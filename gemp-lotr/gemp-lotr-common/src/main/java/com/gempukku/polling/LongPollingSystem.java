package com.gempukku.polling;

import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LongPollingSystem {
    private static Logger _log = Logger.getLogger(LongPollingSystem.class);

    private ConcurrentLinkedQueue<TimeoutResource> _waitingActions = new ConcurrentLinkedQueue<TimeoutResource>();
    private long _pollingInterval = 200;
    private long _pollingLength = 10000;
    private ProcessingRunnable _processingRunnable;

    public LongPollingSystem() {
    }

    public void start() {
        _processingRunnable = new ProcessingRunnable();
        Thread thr = new Thread(_processingRunnable);
        thr.start();
    }

    public void processLongPollingResource(LongPollingResource resource, LongPollableResource pollableResource) {
        if (resource.isChanged())
            resource.processIfNotProcessed();
        else
            pollableResource.registerForChanges(resource);

        _waitingActions.add(new TimeoutResource(resource, pollableResource, System.currentTimeMillis()));
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
                long now = System.currentTimeMillis();
                Iterator<TimeoutResource> iterator = _waitingActions.iterator();
                while (iterator.hasNext()) {
                    TimeoutResource resource = iterator.next();
                    if (resource.getLongPollingResource().wasProcessed())
                        iterator.remove();
                    else {
                        if (resource.getStart() + _pollingLength < now) {
                            resource.getLongPollableResource().deregisterResource(resource.getLongPollingResource());
                            resource.getLongPollingResource().processIfNotProcessed();
                            iterator.remove();
                        }
                    }
                }
                pause();
            }
        }
    }

    private class TimeoutResource {
        private long _start;
        private LongPollableResource _longPollableResource;
        private LongPollingResource _longPollingResource;

        private TimeoutResource(LongPollingResource longPollingResource, LongPollableResource longPollableResource, long start) {
            _longPollingResource = longPollingResource;
            _longPollableResource = longPollableResource;
            _start = start;
        }

        public LongPollableResource getLongPollableResource() {
            return _longPollableResource;
        }

        public LongPollingResource getLongPollingResource() {
            return _longPollingResource;
        }

        public long getStart() {
            return _start;
        }
    }
}
