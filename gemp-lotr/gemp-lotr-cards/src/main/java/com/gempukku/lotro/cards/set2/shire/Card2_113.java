package com.gempukku.lotro.cards.set2.shire;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.DrawCardEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mines of Moria
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 2
 * Type: Possession
 * Game Text: Tale. Plays to your support area. Each time you play a tale, you may spot Bilbo to draw a card.
 */
public class Card2_113 extends AbstractPermanent {
    public Card2_113() {
        super(Side.FREE_PEOPLE, 2, CardType.POSSESSION, Culture.SHIRE, Zone.FREE_SUPPORT, "Red Book of Westmarch", true);
        addKeyword(Keyword.TALE);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.played(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.keyword(Keyword.TALE))
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.name("Bilbo"))) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new DrawCardEffect(playerId, 1));
            return Collections.singletonList(action);
        }
        return null;
    }
}
