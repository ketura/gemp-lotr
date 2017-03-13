package com.gempukku.lotro.cards.set21.esgaroth;

import com.gempukku.lotro.cards.AbstractAlly;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Main Deck
 * Side: Free
 * Culture: Esgaroth
 * Twilight Cost: 2
 * Type: Ally • Home 6 • Man
 * Strength: 6
 * Vitality: 3
 * Site: 6
 * Game Text: Archer. Regroup: Exert Bard to wound a minion. If that minion is Smaug, wound it again.
 */
public class Card21_22 extends AbstractAlly {
    public Card21_22() {
        super(2, Block.HOBBIT, 6, 6, 3, Race.MAN, Culture.ESGAROTH, "Bard", "The Bowman", true);
        addKeyword(Keyword.ARCHER);
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(final String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.REGROUP, self)
                && PlayConditions.canExert(self, game, self)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, CardType.MINION));
						@Override
						protected void woundedCardsCallback(Collection<PhysicalCard> cards) {
                            for (PhysicalCard card : cards)
								if (card.getBlueprint().getName().equals("Smaug"))
									action.appendEffect(new WoundCharactersEffect(self, card));
							}
            return Collections.singletonList(action);
        }
        return null;
    }
}
