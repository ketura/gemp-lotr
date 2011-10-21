package com.gempukku.lotro.logic.timing;

import com.gempukku.lotro.game.state.LotroGame;

import java.util.Collection;

public interface Effect {
    public enum Type {
        BEFORE_WOUND, BEFORE_EXERT, BEFORE_ADD_BURDENS, BEFORE_DISCARD_FROM_PLAY,
        BEFORE_ADD_TWILIGHT, BEFORE_KILLED, BEFORE_HEALED,
        BEFORE_TAKE_CONTROL_OF_A_SITE
    }

    /**
     * Returns the text tha represents this effect. This text might be displayed
     * to the user.
     *
     * @param game
     * @return
     */
    public String getText(LotroGame game);

    /**
     * Returns the type of the effect. This should list the type of effect it represents
     * if the effect is a recognizable by the game.
     *
     * @return
     */
    public Effect.Type getType();

    /**
     * Checks wheather this effect can be played in full. This is required to check
     * for example for cards that give a choice of effects to carry out and one
     * that can be played in full has to be chosen.
     *
     * @param game
     * @return
     */
    public boolean isPlayableInFull(LotroGame game);

    /**
     * Plays the effect and returns it's result.
     *
     * @param game
     * @return
     */
    public Collection<? extends EffectResult> playEffect(LotroGame game);

    /**
     * Returns if the effect playing called earlier was successful or not. This is
     * required for checking if cost was paid and effects can be carried out.
     *
     * @return
     */
    public boolean wasSuccessful();

    /**
     * Returns if the effect was carried out (not prevented) in full. This is required
     * for checking if effect that player can prevent by paying some cost should be
     * played anyway. If it was prevented, the original event has to be played.
     *
     * @return
     */
    public boolean wasCarriedOut();

    /**
     * Resets the history and any state this effect might have, to be able to be
     * played again (for example multiple players trying to prevent the same effect.
     */
    public void reset();
}
