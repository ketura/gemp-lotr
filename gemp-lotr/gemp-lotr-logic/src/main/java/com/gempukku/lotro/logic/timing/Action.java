package com.gempukku.lotro.logic.timing;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

public interface Action {
    public enum Type {
        PLAY_CARD, SPECIAL_ABILITY, TRIGGER, OTHER
    }

    public Type getType();

    public ActionSource getActionSource();

    public void setActionTimeword(Phase phase);

    public PhysicalCard getActionAttachedToCard();

    public void setVirtualCardAction(boolean virtualCardAction);

    public boolean isVirtualCardAction();

    public void setPerformingPlayer(String playerId);

    public String getPerformingPlayer();

    public Phase getActionTimeword();

    public String getText(LotroGame game);

    public Effect nextEffect(LotroGame game);

    public final class ActionSource {
        private PhysicalCard _physicalCard;
        private String _ruleName;

        public ActionSource(PhysicalCard physicalCard) {
            _physicalCard = physicalCard;
        }

        public ActionSource(String ruleName) {
            _ruleName = ruleName;
        }

        public PhysicalCard getPhysicalCard() {
            return _physicalCard;
        }

        public String getRuleName() {
            return _ruleName;
        }
    }
}
