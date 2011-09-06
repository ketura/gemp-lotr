package com.gempukku.lotro.cards.set1.shire;

import com.gempukku.lotro.cards.AbstractAlly;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.HealCharacterEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Ally • Home 1 • Hobbit
 * Strength: 2
 * Vitality: 2
 * Site: 1
 * Game Text: At the start of each of your turns, heal this ally. Fellowship: Exert this ally to heal another Hobbit
 * ally whose home is site 1.
 */
public class Card1_297 extends AbstractAlly {
    public Card1_297() {
        super(1, 1, 2, 2, Keyword.HOBBIT, Culture.SHIRE, "Hobbit Party Guest");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                && PlayConditions.canExert(game.getGameState(), game.getModifiersQuerying(), self)) {
            final DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, Keyword.FELLOWSHIP, "Exert this ally to heal another Hobbit ally whose home is site 1.");
            action.addCost(
                    new ExertCharacterEffect(self));
            action.addEffect(
                    new ChooseActiveCardEffect(playerId, "Choose another Hobbit ally", Filters.type(CardType.ALLY), Filters.keyword(Keyword.HOBBIT), Filters.not(Filters.sameCard(self)), Filters.siteNumber(1)) {
                        @Override
                        protected void cardSelected(PhysicalCard anotherHobbitAlly) {
                            action.addEffect(new HealCharacterEffect(anotherHobbitAlly));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends Action> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.START_OF_TURN) {
            DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, null, "Heal this ally");
            action.addEffect(new HealCharacterEffect(self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
