package com.gempukku.lotro.chat;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer {
    private Map<String, ChatRoomMediator> _chatRooms = new ConcurrentHashMap<String, ChatRoomMediator>();

    private boolean _started;
    private CleaningTask _cleaningTask;

    public void createChatRoom(String name) {
        _chatRooms.put(name, new ChatRoomMediator());
    }

    public ChatRoomMediator getChatRoom(String name) {
        return _chatRooms.get(name);
    }

    public void destroyChatRoom(String name) {
        _chatRooms.remove(name);
    }

    public synchronized void startServer() {
        if (!_started) {
            _cleaningTask = new CleaningTask();
            new Thread(_cleaningTask).start();
            _started = true;
        }
    }

    public synchronized void stopServer() {
        if (_started) {
            _cleaningTask.stop();
            _started = false;
        }
    }

    private void cleanup() {
        // TODO cleanup server
    }

    private class CleaningTask implements Runnable {
        private boolean _running = true;

        public void run() {
            while (_running) {
                cleanup();
                try {
                    Thread.sleep(100);
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
