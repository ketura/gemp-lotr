package com.gempukku.lotro.cards.set2.dwarven;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.DiscardTopCardFromDeckEffect;
import com.gempukku.lotro.cards.effects.PutCardFromDiscardIntoHandEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseCardsFromDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: Mines of Moria
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Tale. Plays to your support area. Each time you play a Dwarf, you may discard the top 2 cards from your
 * draw deck to take a [DWARVEN] event into hand from your discard pile.
 */
public class Card2_009 extends AbstractPermanent {
    public Card2_009() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.DWARVEN, Zone.SUPPORT, "Great Works Begun There");
        addKeyword(Keyword.TALE);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, Race.DWARF)) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new DiscardTopCardFromDeckEffect(self, playerId, false));
            action.appendCost(
                    new DiscardTopCardFromDeckEffect(self, playerId, false));
            action.appendEffect(
                    new ChooseCardsFromDiscardEffect(playerId, 1, 1, Culture.DWARVEN, CardType.EVENT) {
                        @Override
                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> cards) {
                            for (PhysicalCard card : cards) {
                                action.appendEffect(new PutCardFromDiscardIntoHandEffect(card));
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
