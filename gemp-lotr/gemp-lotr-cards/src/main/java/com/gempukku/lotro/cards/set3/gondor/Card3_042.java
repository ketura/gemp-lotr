package com.gempukku.lotro.cards.set3.gondor;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.logic.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.AllyParticipatesInArcheryFireAndSkirmishesModifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Realms of Elf-lords
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 0
 * Type: Possession
 * Game Text: Bearer must be Boromir. Maneuver: Exert Boromir and spot an ally. Until the regroup phase, that ally is
 * strength +3 and participates in archery fire and skirmishes.
 */
public class Card3_042 extends AbstractAttachableFPPossession {
    public Card3_042() {
        super(0, 0, 0, Culture.GONDOR, null, "Horn of Boromir", null, true);
    }

    @Override
    public Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.boromir;
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.MANEUVER, self)
                && PlayConditions.canExert(self, game, Filters.boromir)
                && Filters.canSpot(game, CardType.ALLY)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.boromir));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose an Ally", CardType.ALLY) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.insertEffect(
                                    new AddUntilStartOfPhaseModifierEffect(
                                            new StrengthModifier(self, Filters.sameCard(card), 3), Phase.REGROUP));
                            action.insertEffect(
                                    new AddUntilStartOfPhaseModifierEffect(
                                            new AllyParticipatesInArcheryFireAndSkirmishesModifier(self, Filters.sameCard(card)), Phase.REGROUP));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
