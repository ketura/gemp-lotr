package com.gempukku.lotro.cards.set17.site;

import com.gempukku.lotro.cards.AbstractNewSite;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.RemoveBurdenEffect;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Twilight Cost: 3
 * Type: Site
 * Game Text: River. When the fellowship moves to this site, if it is a Sanctuary, remove 2 burdens.
 */
public class Card17_147 extends AbstractNewSite {
    public Card17_147() {
        super("Imladris", 3, Direction.LEFT);
        addKeyword(Keyword.RIVER);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.movesTo(game, effectResult, self, Keyword.SANCTUARY)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new RemoveBurdenEffect(game.getGameState().getCurrentPlayerId(), self, 2));
            return Collections.singletonList(action);
        }
        return null;
    }
}
