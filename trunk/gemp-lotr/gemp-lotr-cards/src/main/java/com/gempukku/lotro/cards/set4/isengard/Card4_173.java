package com.gempukku.lotro.cards.set4.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.effects.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.modifiers.CantBeAssignedToSkirmishModifier;
import com.gempukku.lotro.cards.modifiers.CantTakeWoundsModifier;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
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
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 4
 * Type: Minion • Wizard
 * Strength: 8
 * Vitality: 4
 * Site: 4
 * Game Text: Saruman may not take wounds during the archery phase and may not be assigned to a skirmish. When you play
 * Saruman, you may discard a condition. Shadow: Exert Saruman to play Saruman's Staff from your discard pile.
 */
public class Card4_173 extends AbstractMinion {
    public Card4_173() {
        super(4, 8, 4, 4, Race.WIZARD, Culture.ISENGARD, "Saruman", true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new CantTakeWoundsModifier(self,
                        new Condition() {
                            @Override
                            public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
                                return gameState.getCurrentPhase() == Phase.ARCHERY && !modifiersQuerying.hasFlagActive(ModifierFlag.SARUMAN_FIRST_SENTENCE_INACTIVE);
                            }
                        }, Filters.sameCard(self)));
        modifiers.add(
                new CantBeAssignedToSkirmishModifier(self,
                        new Condition() {
                            @Override
                            public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
                                return !modifiersQuerying.hasFlagActive(ModifierFlag.SARUMAN_FIRST_SENTENCE_INACTIVE);
                            }
                        },
                        Filters.sameCard(self)));
        return modifiers;
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.played(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.sameCard(self))) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Filters.type(CardType.CONDITION)));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.SHADOW, self, 0)
                && PlayConditions.canExert(self, game, Filters.sameCard(self))
                && PlayConditions.canPlayFromDiscard(playerId, game, Filters.name("Saruman's Staff"))) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ExertCharactersEffect(self, self));
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game.getGameState().getDiscard(playerId), Filters.name("Saruman's Staff")));
            return Collections.singletonList(action);
        }
        return null;
    }
}
