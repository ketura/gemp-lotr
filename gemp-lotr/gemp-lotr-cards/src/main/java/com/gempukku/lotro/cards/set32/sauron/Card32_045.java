package com.gempukku.lotro.cards.set32.sauron;

import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.AddBurdenEffect;
import com.gempukku.lotro.logic.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.ExertCharactersEffect;
import com.gempukku.lotro.logic.effects.PreventableEffect;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.effects.AssignmentEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Clouds Burst
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 5
 * Type: Minion • Maia
 * Strength: 13
 * Vitality: 4
 * Site: 5
 * Game Text: Each [GUNDABAD] Orc is strength +1 and fierce. Assignment: Exert Sauron to assign a [GUNDABAD]
 * or [WRAITH] minion to a companion (except Bilbo). The Free Peoples player may add 2 doubts or exert that
 * companion to prevent this.
 */

public class Card32_045 extends AbstractMinion {
    public Card32_045() {
        super(5, 13, 4, 5, Race.MAIA, Culture.SAURON, "Sauron", "The Necromancer", true);
    }
    
    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(new StrengthModifier(self, Filters.and(Culture.GUNDABAD, Race.ORC), 1));
        modifiers.add(new KeywordModifier(self, Filters.and(Culture.GUNDABAD, Race.ORC), Keyword.FIERCE));
        return modifiers;
    }
  
    @Override
    public List<? extends Action> getPhaseActionsInPlay(final String playerId, final LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.ASSIGNMENT, self, 0)
                && PlayConditions.canExert(self, game, self)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a GUNDABAD or WRAITH minion", Filters.and(Filters.or(Filters.and(Race.ORC, Culture.GUNDABAD), Culture.WRAITH), CardType.MINION), Filters.assignableToSkirmishAgainst(Side.SHADOW, Filters.and(CardType.COMPANION, Filters.not(Filters.name("Bilbo"))))) {
                @Override
                public void cardSelected(final LotroGame game, final PhysicalCard minion) {
                    action.appendEffect(
                            new ChooseActiveCardEffect(self, playerId, "Choose a companion (Except Bilbo)", CardType.COMPANION, Filters.not(Filters.name("Bilbo")), Filters.assignableToSkirmishAgainst(Side.SHADOW, minion)) {
                        @Override
                        public void cardSelected(final LotroGame game, final PhysicalCard companion) {
                            action.appendEffect(
                                    new PreventableEffect(action, new AssignmentEffect(playerId, companion, minion),
                                            Collections.singletonList(game.getGameState().getCurrentPlayerId()),
                                            new PreventableEffect.PreventionCost() {
                                @Override
                                public Effect createPreventionCostForPlayer(SubAction subAction, String playerId) {
                                    List<Effect> possibleCosts = new LinkedList<Effect>();
                                    possibleCosts.add(
                                            new ExertCharactersEffect(action, self, companion));
                                    possibleCosts.add(
                                            new AddBurdenEffect(game.getGameState().getCurrentPlayerId(), self, 2));
                                    return new ChoiceEffect(subAction, playerId, possibleCosts) {
                                        @Override
                                        public String getText(LotroGame game) {
                                            return "Exert that companion or add two doubts";
                                        }
                                    };
                                }
                            }));
                        }
                    });
                }
            });
            return Collections.singletonList(action);
        }
        return null;
    }
}
