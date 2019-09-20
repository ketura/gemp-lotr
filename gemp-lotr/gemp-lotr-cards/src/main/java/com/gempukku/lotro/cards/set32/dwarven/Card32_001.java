package com.gempukku.lotro.cards.set32.dwarven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.decisions.YesNoDecision;
import com.gempukku.lotro.logic.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.effects.RemoveBurdenEffect;
import com.gempukku.lotro.logic.modifiers.AllyParticipatesInArcheryFireAndSkirmishesModifier;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Clouds Burst
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Artifact • Support Area
 * Game Text: Each time you play Roac, you may heal Thorin or Dain Ironfoot. Then, you may remove a doubt.
 * Assignment: If you cannot spot Thorin, Dwarven allies may be assigned to skirmishes.
 */
public class Card32_001 extends AbstractPermanent {
    public Card32_001() {
        super(Side.FREE_PEOPLE, 2, CardType.ARTIFACT, Culture.DWARVEN, "Crown of Erebor", null, true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(final String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, Filters.owner(playerId), Filters.name("Roac"))) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, playerId, 1, 1,
                            Filters.or(Filters.name("Thorin"), Filters.name("Dain Ironfoot"))));
            action.appendEffect(
                    new PlayoutDecisionEffect(playerId,
                            new YesNoDecision("Do you want to remove a doubt?") {
                                @Override
                                public void yes() {
                                    action.appendEffect(
                                            new RemoveBurdenEffect(playerId, self, 1));
                                }
                            }));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.ASSIGNMENT, self)
                && !PlayConditions.canSpot(game, Filters.name("Thorin"))) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(new AddUntilStartOfPhaseModifierEffect(
                    new AllyParticipatesInArcheryFireAndSkirmishesModifier(self, Filters.and(CardType.ALLY, Race.DWARF)), Phase.SKIRMISH));
            return Collections.singletonList(action);
        }
        return null;
    }
}
