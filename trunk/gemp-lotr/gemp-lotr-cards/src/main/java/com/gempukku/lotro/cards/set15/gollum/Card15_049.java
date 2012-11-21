package com.gempukku.lotro.cards.set15.gollum;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.modifiers.OverwhelmedByMultiplierModifier;
import com.gempukku.lotro.cards.modifiers.ResistanceModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Free
 * Culture: Gollum
 * Twilight Cost: 0
 * Type: Companion
 * Strength: 3
 * Vitality: 4
 * Resistance: 5
 * Game Text: Ring-bound. To play, add a burden. Each Ring-bound companion is resistance +1.
 * Skirmish: Exert 2 Ring-bound Hobbits to make Smeagol strength +1 and prevent him from being overwhelmed unless his
 * strength is tripled.
 */
public class Card15_049 extends AbstractCompanion {
    public Card15_049() {
        super(0, 3, 4, 5, Culture.GOLLUM, null, null, "Smeagol", "Wretched and Hungry", true);
        addKeyword(Keyword.RING_BOUND);
    }

    @Override
    public PlayPermanentAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayPermanentAction playCardAction = super.getPlayCardAction(playerId, game, self, twilightModifier, ignoreRoamingPenalty);
        playCardAction.appendCost(
                new AddBurdenEffect(self.getOwner(), self, 1));
        return playCardAction;
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new ResistanceModifier(self, Filters.and(CardType.COMPANION, Keyword.RING_BOUND), 1);
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canExert(self, game, 1, 2, Race.HOBBIT, Keyword.RING_BOUND)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 2, 2, 1, Race.HOBBIT, Keyword.RING_BOUND));
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new StrengthModifier(self, self, 1), Phase.SKIRMISH));
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new OverwhelmedByMultiplierModifier(self, self, 3), Phase.SKIRMISH));
            return Collections.singletonList(action);
        }
        return null;
    }
}
