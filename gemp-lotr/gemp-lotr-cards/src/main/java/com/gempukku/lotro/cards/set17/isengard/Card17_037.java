package com.gempukku.lotro.cards.set17.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPreventCardEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.modifiers.*;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 3
 * Type: Minion • Wizard
 * Strength: 8
 * Vitality: 4
 * Site: 4
 * Game Text: Saruman cannot be assigned to a skirmish. While you can spot 6 companions, each minion gains hunter 2.
 * Response: If a hunter minion is about to take a wound, exert Saruman to prevent that.
 */
public class Card17_037 extends AbstractMinion {
    public Card17_037() {
        super(3, 8, 4, 4, Race.WIZARD, Culture.ISENGARD, "Saruman", "Instigator of Insurrection", true);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new CantBeAssignedToSkirmishModifier(self,
                new Condition() {
                    @Override
                    public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
                        return !modifiersQuerying.hasFlagActive(gameState, ModifierFlag.SARUMAN_FIRST_SENTENCE_INACTIVE);
                    }
                }, self);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new CantBeAssignedToSkirmishModifier(self,
                        new Condition() {
                            @Override
                            public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
                                return !modifiersQuerying.hasFlagActive(gameState, ModifierFlag.SARUMAN_FIRST_SENTENCE_INACTIVE);
                            }
                        }, self));
        modifiers.add(
                new KeywordModifier(self, CardType.MINION, new SpotCondition(6, CardType.COMPANION), Keyword.HUNTER, 2));
        return modifiers;
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isGettingWounded(effect, game, CardType.MINION, Keyword.HUNTER)
                && PlayConditions.canSelfExert(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseAndPreventCardEffect(self, (WoundCharactersEffect) effect, playerId, "Choose a hunter minion", CardType.MINION, Keyword.HUNTER));
            return Collections.singletonList(action);
        }
        return null;
    }
}
