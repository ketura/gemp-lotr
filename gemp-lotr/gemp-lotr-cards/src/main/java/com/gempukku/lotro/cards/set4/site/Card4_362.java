package com.gempukku.lotro.cards.set4.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Twilight Cost: 9
 * Type: Site
 * Site: 9T
 * Game Text: When the fellowship moves to Orthanc Library, each Shadow player may draw a card for each companion over 4.
 */
public class Card4_362 extends AbstractSite {
    public Card4_362() {
        super("Orthanc Library", Block.TWO_TOWERS, 9, 9, Direction.LEFT);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.WHEN_MOVE_TO
                && game.getGameState().getCurrentSite() == self) {
            int companionCount = Filters.countSpottable(game.getGameState(), game.getModifiersQuerying(), CardType.COMPANION);
            if (!playerId.equals(game.getGameState().getCurrentPlayerId())
                    && companionCount > 4) {
                OptionalTriggerAction action = new OptionalTriggerAction(self);
                action.appendEffect(
                        new DrawCardsEffect(playerId, companionCount - 4));
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
