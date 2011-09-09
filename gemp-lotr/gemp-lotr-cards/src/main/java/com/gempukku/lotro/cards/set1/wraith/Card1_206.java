package com.gempukku.lotro.cards.set1.wraith;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardFromPlayEffect;
import com.gempukku.lotro.logic.effects.DrawCardEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.EndOfPhaseResult;
import com.gempukku.lotro.logic.timing.results.StartOfPhaseResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Search. Plays to your support area. At the beginning of each of your Shadow phases, draw 1 card. At the
 * end of each of your Shadow phases, exert a Nazgul or discard this condition.
 */
public class Card1_206 extends AbstractLotroCardBlueprint {
    public Card1_206() {
        super(Side.SHADOW, CardType.CONDITION, Culture.WRAITH, "Bent on Discovery");
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return PlayConditions.canPayForShadowCard(game, self, twilightModifier);
    }

    @Override
    public Action getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return new PlayPermanentAction(self, Zone.SHADOW_SUPPORT, twilightModifier);
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canPlayCardDuringPhase(game, Phase.SHADOW, self)
                && checkPlayRequirements(playerId, game, self, 0))
            return Collections.singletonList(getPlayCardAction(playerId, game, self, 0));
        return null;
    }

    @Override
    public List<? extends Action> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.START_OF_PHASE
                && ((StartOfPhaseResult) effectResult).getPhase() == Phase.SHADOW) {
            DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, null, "Draw 1 card");
            action.addEffect(
                    new DrawCardEffect(self.getOwner()));
            return Collections.singletonList(action);
        }
        if (effectResult.getType() == EffectResult.Type.END_OF_PHASE
                && ((EndOfPhaseResult) effectResult).getPhase() == Phase.SHADOW) {
            final DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, null, "Exert a Nazgul or discard this condition");
            List<Effect> possibleEffects = new LinkedList<Effect>();
            possibleEffects.add(
                    new ChooseActiveCardEffect(self.getOwner(), "Choose a Nazgul", Filters.keyword(Keyword.NAZGUL), Filters.canExert()) {
                        @Override
                        public String getText() {
                            return "Exert a Nazgul";
                        }

                        @Override
                        protected void cardSelected(PhysicalCard nazgul) {
                            action.addEffect(
                                    new ExertCharacterEffect(nazgul));
                        }
                    });
            possibleEffects.add(
                    new DiscardCardFromPlayEffect(self, self));

            action.addEffect(
                    new ChoiceEffect(action, self.getOwner(), possibleEffects, false));
            return Collections.singletonList(action);
        }
        return null;
    }
}
