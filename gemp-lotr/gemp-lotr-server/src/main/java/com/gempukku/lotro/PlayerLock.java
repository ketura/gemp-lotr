package com.gempukku.lotro;

import com.gempukku.lotro.game.User;

public class PlayerLock {
    public static Object getLock(User player) {
        return player.getName().intern();
    }

    public static Object getLock(String playerName) {
        return playerName.intern();
    }
}
