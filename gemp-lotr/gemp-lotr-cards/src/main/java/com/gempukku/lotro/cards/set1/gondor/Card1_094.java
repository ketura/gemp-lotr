package com.gempukku.lotro.cards.set1.gondor;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Possession
 * Game Text: Bearer must be a [GONDOR] Man. Fellowship: Discard this possession to heal a companion or to remove a
 * Shadow condition from a companion.
 */
public class Card1_094 extends AbstractAttachableFPPossession {
    public Card1_094() {
        super(1, 0, 0, Culture.GONDOR, null, "Athelas");
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Filters.culture(Culture.GONDOR), Filters.race(Race.MAN));
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)) {
            final ActivateCardAction action = new ActivateCardAction(self, Keyword.FELLOWSHIP);
            action.appendCost(
                    new DiscardCardsFromPlayEffect(self, self));

            List<Effect> possibleEffects = new LinkedList<Effect>();
            possibleEffects.add(
                    new ChooseAndHealCharactersEffect(action, playerId, Filters.type(CardType.COMPANION)) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Heal a companion";
                        }
                    });

            possibleEffects.add(
                    new ChooseActiveCardEffect(playerId, "Choose Shadow condition attached to companion", Filters.side(Side.SHADOW), Filters.type(CardType.CONDITION), Filters.attachedTo(Filters.type(CardType.COMPANION))) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Discard Shadow condition attached to a companion";
                        }

                        @Override
                        protected void cardSelected(PhysicalCard shadowCondition) {
                            action.appendEffect(new DiscardCardsFromPlayEffect(self, shadowCondition));
                        }
                    });

            action.appendEffect(
                    new ChoiceEffect(action, playerId, possibleEffects));
            return Collections.singletonList(action);
        }
        return null;
    }
}
