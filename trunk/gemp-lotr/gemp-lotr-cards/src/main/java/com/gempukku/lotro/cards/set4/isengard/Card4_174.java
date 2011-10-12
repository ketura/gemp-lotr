package com.gempukku.lotro.cards.set4.isengard;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.modifiers.SpecialFlagModifier;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifierFlag;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 2
 * Type: Artifact
 * Strength: +2
 * Game Text: Plays on Saruman. He is fierce and damage +1. Maneuver: Make the first sentence of Saruman's game text
 * not apply until the regroup phase.
 */
public class Card4_174 extends AbstractAttachable {
    public Card4_174() {
        super(Side.SHADOW, CardType.ARTIFACT, 2, Culture.ISENGARD, Keyword.STAFF, "Saruman's Staff", true);
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.name("Saruman");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, Filters.hasAttached(self), 2));
        modifiers.add(
                new KeywordModifier(self, Filters.hasAttached(self), Keyword.FIERCE));
        modifiers.add(
                new KeywordModifier(self, Filters.hasAttached(self), Keyword.DAMAGE));
        return modifiers;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.MANEUVER, self, 0)
                && !game.getModifiersQuerying().hasFlagActive(ModifierFlag.SARUMAN_FIRST_SENTENCE_INACTIVE)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new AddUntilStartOfPhaseModifierEffect(
                            new SpecialFlagModifier(self, ModifierFlag.SARUMAN_FIRST_SENTENCE_INACTIVE), Phase.REGROUP));
            return Collections.singletonList(action);
        }
        return null;
    }
}
