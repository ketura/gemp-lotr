package com.gempukku.lotro.draft;

import com.gempukku.lotro.AbstractServer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DraftServer extends AbstractServer {
    private Map<Draft, DraftCallback> _draftCallbackMap = new HashMap<Draft, DraftCallback>();

    private ReadWriteLock _lock = new ReentrantReadWriteLock();

    public void addDraft(Draft draft, DraftCallback draftCallback) {
        _lock.writeLock().lock();
        try {
            _draftCallbackMap.put(draft, draftCallback);
        } finally {
            _lock.writeLock().unlock();
        }
    }

    public boolean isPlayerDrafting(String playerId) {
        _lock.readLock().lock();
        try {
            for (Draft draft : _draftCallbackMap.keySet()) {
                if (draft.isPlayerInDraft(playerId))
                    return true;
            }
            return false;
        } finally {
            _lock.readLock().unlock();
        }
    }

    @Override
    protected void cleanup() {
        _lock.writeLock().lock();
        try {
            Map<Draft, DraftCallback> draftCallbackMap = new HashMap<Draft, DraftCallback>(_draftCallbackMap);
            for (Map.Entry<Draft, DraftCallback> draftDraftCallbackEntry : draftCallbackMap.entrySet()) {
                Draft draft = draftDraftCallbackEntry.getKey();
                draft.advanceDraft(draftDraftCallbackEntry.getValue());
                if (draft.isFinished())
                    _draftCallbackMap.remove(draft);
            }
        } finally {
            _lock.writeLock().unlock();
        }
    }
}
