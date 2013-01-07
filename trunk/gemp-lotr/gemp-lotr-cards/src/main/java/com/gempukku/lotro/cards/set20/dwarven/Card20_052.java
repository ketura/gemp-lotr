package com.gempukku.lotro.cards.set20.dwarven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndStackCardsFromDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;

import java.util.Collections;
import java.util.List;

/**
 * 2
 * Fror, Gimli's Kinsman
 * Dwarven	Companion • Dwarf
 * 5	3	6
 * Damage +1.
 * Regroup: Exert Fror to stack a [Dwarven] event from your discard pile onto a [Dwarven] support area Condition
 */
public class Card20_052 extends AbstractCompanion {
    public Card20_052() {
        super(2, 5, 3, 6, Culture.DWARVEN, Race.DWARF, null, "Fror", "Gimli's Kinsman", true);
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(final String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.REGROUP, self)
                && PlayConditions.canSelfExert(self, game)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a DWARVEN condition in your support area", Culture.DWARVEN, CardType.CONDITION, Keyword.SUPPORT_AREA) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.appendEffect(
                                    new ChooseAndStackCardsFromDiscardEffect(action, playerId, 1, 1, card, Culture.DWARVEN, CardType.EVENT));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
