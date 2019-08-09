package com.gempukku.lotro.cards.set13.site;

import com.gempukku.lotro.logic.cardtype.AbstractNewSite;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.AddBurdenEffect;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Bloodlines
 * Twilight Cost: 0
 * Type: Site
 * Game Text: Dwelling. When the fellowship moves to this site, the first Shadow player may discard 2 cards from hand
 * to add a burden.
 */
public class Card13_188 extends AbstractNewSite {
    public Card13_188() {
        super("Courtyard Parapet", 0, Direction.LEFT);
        addKeyword(Keyword.DWELLING);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        String firstShadow = GameUtils.getFirstShadowPlayer(game);
        if (TriggerConditions.movesTo(game, effectResult, self)
                && playerId.equals(firstShadow)
                && PlayConditions.canDiscardFromHand(game, playerId, 2, Filters.any)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 2));
            action.appendEffect(
                    new AddBurdenEffect(playerId, self, 1));
            return Collections.singletonList(action);
        }
        return null;
    }
}
