package com.gempukku.lotro.cards.set13.site;

import com.gempukku.lotro.cards.AbstractNewSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.RemoveBurdenEffect;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Bloodlines
 * Twilight Cost: 3
 * Type: Site
 * Game Text: Underground. When the fellowship moves to this site during the regroup phase, the Free Peoples player may
 * remove 2 burdens.
 */
public class Card13_186 extends AbstractNewSite {
    public Card13_186() {
        super("Caves of Aglarond", 3, Direction.RIGHT);
        addKeyword(Keyword.UNDERGROUND);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.movesTo(game, effectResult, self)
                && PlayConditions.isPhase(game, Phase.REGROUP)
                && playerId.equals(game.getGameState().getCurrentPlayerId())) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new RemoveBurdenEffect(playerId, self, 2));
            return Collections.singletonList(action);
        }
        return null;
    }
}
