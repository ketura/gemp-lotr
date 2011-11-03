package com.gempukku.lotro.cards.set5.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.cards.modifiers.CantBeAssignedToSkirmishModifier;
import com.gempukku.lotro.cards.modifiers.CantTakeWoundsModifier;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifierFlag;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Battle of Helm's Deep
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 4
 * Type: Minion • Wizard
 * Strength: 8
 * Vitality: 4
 * Site: 4
 * Game Text: Saruman may not take wounds during the archery phase and may not be assigned to a skirmish. Each time
 * the fellowship moves, you may heal each [ISENGARD] Orc twice. Shadow: Exert Saruman to play an [ISENGARD] possession
 * from your discard pile.
 */
public class Card5_056 extends AbstractMinion {
    public Card5_056() {
        super(4, 8, 4, 4, Race.WIZARD, Culture.ISENGARD, "Saruman", true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new CantTakeWoundsModifier(self,
                        new Condition() {
                            @Override
                            public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
                                return !modifiersQuerying.hasFlagActive(gameState, ModifierFlag.SARUMAN_FIRST_SENTENCE_INACTIVE);
                            }
                        },
                        Filters.and(
                                Filters.sameCard(self),
                                new Filter() {
                                    @Override
                                    public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                                        return gameState.getCurrentPhase() == Phase.ARCHERY;
                                    }
                                })));
        modifiers.add(
                new CantBeAssignedToSkirmishModifier(self,
                        new Condition() {
                            @Override
                            public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
                                return !modifiersQuerying.hasFlagActive(gameState, ModifierFlag.SARUMAN_FIRST_SENTENCE_INACTIVE);
                            }
                        },
                        Filters.sameCard(self)));
        return modifiers;
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.WHEN_FELLOWSHIP_MOVES) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, playerId, 1, 1, 2, Culture.ISENGARD, Race.ORC));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.SHADOW, self, 0)
                && PlayConditions.canSelfExert(self, game)
                && PlayConditions.canPlayFromDiscard(playerId, game, Culture.ISENGARD, CardType.POSSESSION)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ExertCharactersEffect(self, self));
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game.getGameState().getDiscard(playerId), Culture.ISENGARD, CardType.POSSESSION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
