package com.gempukku.lotro.cards.set1.shire;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChooseOpponentEffect;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.cards.effects.RevealAndChooseCardsFromOpponentHandEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Possession • Hand Weapon
 * Strength: +2
 * Game Text: Bearer must be Frodo. Fellowship or Regroup: Exert Frodo to reveal an opponent's hand. Remove (1) for
 * each Orc revealed (limit (4)).
 */
public class Card1_313 extends AbstractAttachableFPPossession {
    public Card1_313() {
        super(1, 2, 0, Culture.SHIRE, Keyword.HAND_WEAPON, "Sting", true);
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.name("Frodo");
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(final String playerId, final LotroGame game, final PhysicalCard self) {
        if ((PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                || PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.REGROUP, self))
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), self.getAttachedTo())) {
            Phase phase = game.getGameState().getCurrentPhase();
            Keyword keyword;
            if (phase == Phase.FELLOWSHIP)
                keyword = Keyword.FELLOWSHIP;
            else
                keyword = Keyword.REGROUP;

            final ActivateCardAction action = new ActivateCardAction(self, keyword);
            action.appendCost(new ExertCharactersEffect(self, self.getAttachedTo()));
            action.appendEffect(
                    new ChooseOpponentEffect(playerId) {
                        @Override
                        protected void opponentChosen(final String opponentId) {
                            action.appendEffect(
                                    new RevealAndChooseCardsFromOpponentHandEffect(playerId, opponentId, "Opponent's hand", Filters.none(), 0, 0) {
                                        @Override
                                        protected void cardsSelected(List<PhysicalCard> selectedCards) {
                                            Collection<PhysicalCard> orcs = Filters.filter(game.getGameState().getHand(opponentId), game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.ORC));
                                            Integer limit = (Integer) self.getData();
                                            int usedUp = 0;
                                            if (limit != null)
                                                usedUp = limit;
                                            int toRemove = Math.min(4 - usedUp, orcs.size());
                                            if (toRemove > 0) {
                                                self.storeData(usedUp + toRemove);
                                                action.appendEffect(new RemoveTwilightEffect(toRemove));
                                            }
                                        }
                                    });
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredBeforeTriggers(LotroGame game, Effect effect, PhysicalCard self) {
        if (effect.getType() == EffectResult.Type.END_OF_PHASE)
            self.removeData();
        return null;
    }
}
