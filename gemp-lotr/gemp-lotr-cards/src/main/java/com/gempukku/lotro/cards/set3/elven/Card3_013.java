package com.gempukku.lotro.cards.set3.elven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractAlly;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.decisions.IntegerAwaitingDecision;
import com.gempukku.lotro.logic.effects.*;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Realms of Elf-lords
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 4
 * Type: Ally • Home 3 • Elf
 * Strength: 8
 * Vitality: 4
 * Site: 3
 * Game Text: At the start of each of your turns, you may spot an ally whose home is site 3 to heal that ally twice.
 * Regroup: Exert Elrond twice to heal a companion.
 */
public class Card3_013 extends AbstractAlly {
    public Card3_013() {
        super(4, SitesBlock.FELLOWSHIP, 3, 8, 4, Race.ELF, Culture.ELVEN, "Elrond", "Herald to Gil-galad", true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(final String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.startOfTurn(game, effectResult)) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new ChooseActiveCardEffect(self, playerId, "Choose an ally", CardType.ALLY, Filters.isAllyHome(3, SitesBlock.FELLOWSHIP), Filters.canHeal) {
                        @Override
                        protected void cardSelected(LotroGame game, final PhysicalCard card) {
                            action.appendEffect(
                                    new PlayoutDecisionEffect(playerId,
                                            new IntegerAwaitingDecision(1, "How many times do you wish to heal it?", 0, 2, 2) {
                                                @Override
                                                public void decisionMade(String result) throws DecisionResultInvalidException {
                                                    final int heals = getValidatedResult(result);
                                                    for (int i=0; i<heals; i++)
                                                        action.appendEffect(
                                                                new HealCharactersEffect(self, self.getOwner(), card));
                                                }
                                            }));
                        }
                    });
            
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.REGROUP, self)
                && PlayConditions.canExert(self, game, 2, self)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, playerId, CardType.COMPANION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
