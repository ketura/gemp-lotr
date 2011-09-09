package com.gempukku.lotro.cards.set1.gandalf;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChooseOpponentEffect;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.modifiers.CompositeModifier;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 2
 * Type: Possession • Hand Weapon
 * Strength: +2
 * Game Text: Bearer must be Gandalf. He is damage +1. Fellowship or Regroup: Exert Gandalf to reveal an opponent's
 * hand. Remove (1) for each Orc revealed.
 */
public class Card1_075 extends AbstractAttachableFPPossession {
    public Card1_075() {
        super(2, Culture.GANDALF, Keyword.HAND_WEAPON, "Glamdring", true);
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Filters.name("Gandalf"), Filters.not(Filters.hasAttached(Filters.keyword(Keyword.HAND_WEAPON))));
    }

    @Override
    public Modifier getAlwaysOnEffect(PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(new StrengthModifier(null, null, 2));
        modifiers.add(new KeywordModifier(null, null, Keyword.DAMAGE));
        return new CompositeModifier(self, Filters.attachedTo(self), modifiers);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, final LotroGame game, PhysicalCard self) {
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
                    new ChooseOpponentEffect(playerId) {
                        @Override
                        protected void opponentChosen(String opponentId) {
                            List<PhysicalCard> orcs = Filters.filter(game.getGameState().getHand(opponentId), game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.ORC));
                            action.addEffect(new RemoveTwilightEffect(orcs.size()));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
