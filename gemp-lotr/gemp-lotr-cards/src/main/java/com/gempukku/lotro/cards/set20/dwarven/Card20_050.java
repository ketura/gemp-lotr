package com.gempukku.lotro.cards.set20.dwarven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndStackCardsFromHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;

import java.util.Collections;
import java.util.List;

/**
 * 2
 * Farin, Dwarf of Erebor
 * Dwarven	Companion • Dwarf
 * 5	3	6
 * Damage +1.
 * Fellowship: Exert Farin and stack a [Dwarven] event onto a [Dwarven] support area condition in your support area
 * to draw a card
 */
public class Card20_050 extends AbstractCompanion {
    public Card20_050() {
        super(2, 5, 3, 6, Culture.DWARVEN, Race.DWARF, null, "Farin", "Dwarf of Erebor", true);
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(final String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canSelfExert(self, game)
                && PlayConditions.hasCardInHand(game, playerId, 1, Culture.DWARVEN, CardType.EVENT)
                && PlayConditions.canSpot(game, Culture.DWARVEN, Zone.SUPPORT, CardType.CONDITION)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendCost(
                    new ChooseActiveCardEffect(self, playerId, "Choose DWARVEN condition in your support area", Culture.DWARVEN, Zone.SUPPORT, CardType.CONDITION) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.appendCost(
                                    new ChooseAndStackCardsFromHandEffect(action, playerId, 1, 1, card, Culture.DWARVEN, CardType.EVENT));
                        }
                    });
            action.appendEffect(
                    new DrawCardsEffect(action, playerId, 1));
            return Collections.singletonList(action);
        }
        return null;
    }
}
