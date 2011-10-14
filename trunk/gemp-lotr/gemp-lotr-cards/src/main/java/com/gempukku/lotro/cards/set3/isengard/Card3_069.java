package com.gempukku.lotro.cards.set3.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.PreventableEffect;
import com.gempukku.lotro.cards.modifiers.CantBeAssignedToSkirmishModifier;
import com.gempukku.lotro.cards.modifiers.CantTakeWoundsModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.AssignmentEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifierFlag;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Realms of Elf-lords
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 4
 * Type: Minion • Wizard
 * Strength: 8
 * Vitality: 4
 * Site: 4
 * Game Text: Saruman may not take wounds during the archery phase and may not be assigned to a skirmish. Assignment:
 * Exert Saruman to assign an [ISENGARD] minion to a companion (except the Ring-bearer). That companion may exert
 * to prevent this.
 */
public class Card3_069 extends AbstractMinion {
    public Card3_069() {
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
                                return !modifiersQuerying.hasFlagActive(ModifierFlag.SARUMAN_FIRST_SENTENCE_INACTIVE);
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
                                return !modifiersQuerying.hasFlagActive(ModifierFlag.SARUMAN_FIRST_SENTENCE_INACTIVE);
                            }
                        },
                        Filters.sameCard(self)));
        return modifiers;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, final LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.ASSIGNMENT, self, 0)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), self)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ExertCharactersEffect(self, self));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose ISENGARD minion", Filters.culture(Culture.ISENGARD), Filters.type(CardType.MINION), Filters.canBeAssignedToSkirmishByEffect(Side.SHADOW)) {
                        @Override
                        protected void cardSelected(LotroGame game, final PhysicalCard minion) {
                            action.appendEffect(
                                    new ChooseActiveCardEffect(self, playerId, "Choose non Ring-bearer companion", Filters.type(CardType.COMPANION), Filters.not(Filters.keyword(Keyword.RING_BEARER)), Filters.canBeAssignedToSkirmishByEffect(Side.SHADOW)) {
                                        @Override
                                        protected void cardSelected(LotroGame game, PhysicalCard companion) {
                                            action.appendEffect(
                                                    new PreventableEffect(
                                                            action,
                                                            new AssignmentEffect(playerId, companion, minion),
                                                            Collections.singletonList(game.getGameState().getCurrentPlayerId()),
                                                            new ExertCharactersEffect(self, companion)));
                                        }
                                    });
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
