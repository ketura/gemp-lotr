package com.gempukku.lotro.cards.set13.shire;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.effects.ReplaceInSkirmishEffect;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.OverwhelmedByMultiplierModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 2
 * Type: Companion • Hobbit
 * Strength: 3
 * Vitality: 4
 * Resistance: 8
 * Game Text: Ring-bound. While you can spot more minions than companions, Frodo cannot be overwhelmed unless his
 * strength is tripled. Skirmish: If Frodo is not assigned to a skirmish, exert Frodo to have him replace a Ring-bound
 * companion skirmishing a minion.
 */
public class Card13_149 extends AbstractCompanion {
    public Card13_149() {
        super(2, 3, 4, 8, Culture.SHIRE, Race.HOBBIT, null, "Frodo", "Frenzied Fighter", true);
        addKeyword(Keyword.RING_BOUND);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(new OverwhelmedByMultiplierModifier(self, self,
                new Condition() {
                    @Override
                    public boolean isFullfilled(LotroGame game) {
                        return Filters.countActive(game, CardType.MINION)
                                > Filters.countActive(game, CardType.COMPANION);
                    }
                }, 3));
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canSpot(game, self, Filters.notAssignedToSkirmish)
                && PlayConditions.canSelfExert(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new ReplaceInSkirmishEffect(self, CardType.COMPANION, Keyword.RING_BOUND));
            return Collections.singletonList(action);
        }
        return null;
    }
}
