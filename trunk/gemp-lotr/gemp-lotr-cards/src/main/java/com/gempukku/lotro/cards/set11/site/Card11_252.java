package com.gempukku.lotro.cards.set11.site;

import com.gempukku.lotro.cards.AbstractNewSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.RemoveBurdenEffect;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Shadows
 * Twilight Cost: 2
 * Type: Site
 * Game Text: Battleground. When the fellowship moves from this site, the Free Peoples player may spot 3 burdens
 * to remove a burden.
 */
public class Card11_252 extends AbstractNewSite {
    public Card11_252() {
        super("Osgiliath Reclaimed", 2, Direction.LEFT);
        addKeyword(Keyword.BATTLEGROUND);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.movesFrom(game, effectResult, self)
                && playerId.equals(game.getGameState().getCurrentPlayerId())
                && PlayConditions.canSpotBurdens(game, 3)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new RemoveBurdenEffect(playerId, self, 1));
            return Collections.singletonList(action);
        }
        return null;
    }
}
