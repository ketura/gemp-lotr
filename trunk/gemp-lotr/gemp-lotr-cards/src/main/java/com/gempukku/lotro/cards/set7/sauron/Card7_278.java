package com.gempukku.lotro.cards.set7.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.PreventCardEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 3
 * Type: Minion • Orc
 * Strength: 9
 * Vitality: 2
 * Site: 5
 * Game Text: Besieger. Response: If a besieger is about to take a wound, discard 2 cards from hand to prevent it.
 */
public class Card7_278 extends AbstractMinion {
    public Card7_278() {
        super(3, 9, 2, 5, Race.ORC, Culture.SAURON, "Gorgoroth Soldier");
        addKeyword(Keyword.BESIEGER);
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, final Effect effect, final PhysicalCard self) {
        if (PlayConditions.isGettingWounded(effect, game, Keyword.BESIEGER)
                && PlayConditions.canDiscardFromHand(game, playerId, 2, Filters.any)) {
            final WoundCharactersEffect woundEffect = (WoundCharactersEffect) effect;
            final Collection<PhysicalCard> cardsToBeWounded = woundEffect.getAffectedCardsMinusPrevented(game);
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 2));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose besieger", Keyword.BESIEGER, Filters.in(cardsToBeWounded)) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard besieger) {
                            action.appendEffect(
                                    new PreventCardEffect(woundEffect, besieger));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
