package com.gempukku.polling;

import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LongPollingSystem {
    private static Logger _log = Logger.getLogger(LongPollingSystem.class);

    private final Set<ResourceWaitingRequest> _waitingActions = Collections.synchronizedSet(new HashSet<ResourceWaitingRequest>());

    private long _pollingInterval = 1000;
    private long _pollingLength = 10000;

    private ProcessingRunnable _timeoutRunnable;
    private ExecutorService _executorService = Executors.newCachedThreadPool();

    public LongPollingSystem() {
    }

    public void start() {
        _timeoutRunnable = new ProcessingRunnable();
        Thread thr = new Thread(_timeoutRunnable);
        thr.start();
    }

    public void processLongPollingResource(LongPollingResource resource, LongPollableResource pollableResource) {
        ResourceWaitingRequest request = new ResourceWaitingRequest(pollableResource, resource, System.currentTimeMillis());
        if (pollableResource.registerRequest(request)) {
            execute(resource);
        } else {
            _waitingActions.add(request);
        }
    }

    private void pause() {
        try {
            Thread.sleep(_pollingInterval);
        } catch (InterruptedException exp) {
            // Ignore
        }
    }

    private void processWaitingRequest(final ResourceWaitingRequest request) {
        _waitingActions.remove(request);
        execute(request.getLongPollingResource());
    }

    private void execute(final LongPollingResource resource) {
        _executorService.submit(
                new Runnable() {
                    @Override
                    public void run() {
                        resource.processIfNotProcessed();
                    }
                });
    }

    private class ProcessingRunnable implements Runnable {
        @Override
        public void run() {
            while (true) {
                synchronized (_waitingActions) {
                    long now = System.currentTimeMillis();
                    Iterator<ResourceWaitingRequest> iterator = _waitingActions.iterator();
                    while (iterator.hasNext()) {
                        ResourceWaitingRequest waitingRequest = iterator.next();
                        if (waitingRequest.getLongPollingResource().wasProcessed())
                            iterator.remove();
                        else {
                            if (waitingRequest.getStart() + _pollingLength < now) {
                                waitingRequest.getLongPollableResource().deregisterRequest(waitingRequest);
                                iterator.remove();
                                execute(waitingRequest.getLongPollingResource());
                            }
                        }
                    }
                }
                pause();
            }
        }
    }

    private class ResourceWaitingRequest implements WaitingRequest {
        private LongPollingResource _longPollingResource;
        private LongPollableResource _longPollableResource;
        private long _start;

        private ResourceWaitingRequest(LongPollableResource longPollableResource, LongPollingResource longPollingResource, long start) {
            _longPollableResource = longPollableResource;
            _longPollingResource = longPollingResource;
            _start = start;
        }

        @Override
        public void processRequest() {
            processWaitingRequest(this);
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