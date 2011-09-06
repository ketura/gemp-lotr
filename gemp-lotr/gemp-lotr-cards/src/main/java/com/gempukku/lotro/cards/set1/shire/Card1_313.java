package com.gempukku.lotro.cards.set1.shire;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.GameUtils;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

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
        super(1, Culture.SHIRE, "Sting", true);
        addKeyword(Keyword.HAND_WEAPON);
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Filters.name("Frodo"), Filters.not(Filters.hasAttached(Filters.keyword(Keyword.HAND_WEAPON))));
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, final LotroGame game, final PhysicalCard self) {
        if ((PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                || PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.REGROUP, self))
                && PlayConditions.canExert(game.getGameState(), game.getModifiersQuerying(), self.getAttachedTo())) {
            Phase phase = game.getGameState().getCurrentPhase();
            Keyword keyword;
            if (phase == Phase.FELLOWSHIP)
                keyword = Keyword.FELLOWSHIP;
            else
                keyword = Keyword.REGROUP;

            final DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, keyword, "Exert Frodo to reveal an opponent's hand. Remove (1) for each Orc revealed (limit (4)).");
            action.addCost(new ExertCharacterEffect(self.getAttachedTo()));
            action.addEffect(
                    new PlayoutDecisionEffect(game.getUserFeedback(), playerId,
                            new MultipleChoiceAwaitingDecision(1, "Choose an opponent", GameUtils.getOpponents(game, playerId)) {
                                @Override
                                protected void validDecisionMade(int index, String opponent) {
                                    List<PhysicalCard> orcs = Filters.filter(game.getGameState().getHand(opponent), game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.ORC));
                                    Integer limit = (Integer) self.getData();
                                    int usedUp = 0;
                                    if (limit != null)
                                        usedUp = limit;
                                    int toRemove = Math.min(4 - usedUp, orcs.size());
                                    if (toRemove > 0) {
                                        self.storeData(usedUp + toRemove);
                                        action.addEffect(new RemoveTwilightEffect(toRemove));
                                    }
                                }
                            }));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends Action> getRequiredBeforeTriggers(LotroGame game, Effect effect, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.END_OF_PHASE)
            self.removeData();
        return null;
    }
}
