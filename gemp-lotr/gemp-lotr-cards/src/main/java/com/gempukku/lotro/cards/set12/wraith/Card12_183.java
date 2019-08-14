package com.gempukku.lotro.cards.set12.wraith;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.effects.RevealCardsFromYourHandEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 8
 * Type: Minion • Nazgul
 * Strength: 14
 * Vitality: 4
 * Site: 3
 * Game Text: Fierce. The Witch-King is twilight cost -1 for each wound on the Ring-bearer. Each time a companion is
 * played, you may reveal this card from hand to exert the Ring-bearer.
 */
public class Card12_183 extends AbstractMinion {
    public Card12_183() {
        super(8, 14, 4, 3, Race.NAZGUL, Culture.WRAITH, Names.witchKing, "Black Lord", true);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public int getTwilightCostModifier(LotroGame game, PhysicalCard self) {
        return -game.getGameState().getWounds(game.getGameState().getRingBearer(game.getGameState().getCurrentPlayerId()));
    }

    @Override
    public List<OptionalTriggerAction> getOptionalInHandAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, CardType.COMPANION)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new RevealCardsFromYourHandEffect(self, playerId, self));
            action.appendEffect(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.ringBearer));
            return Collections.singletonList(action);
        }
        return null;
    }
}
