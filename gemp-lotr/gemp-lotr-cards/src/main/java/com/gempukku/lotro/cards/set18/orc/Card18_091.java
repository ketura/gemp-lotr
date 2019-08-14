package com.gempukku.lotro.cards.set18.orc;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.SubCostToEffectAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.decisions.YesNoDecision;
import com.gempukku.lotro.logic.effects.AddBurdenEffect;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAssignCharacterToMinionEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Treachery & Deceit
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 2
 * Type: Minion • Orc
 * Strength: 7
 * Vitality: 1
 * Site: 4
 * Game Text: Assignment: Spot another [ORC] Orc to assign this minion to skirmish a non-[SHIRE] Ring-bearer.
 * The Free Peoples player may add a burden or exert the Ring-bearer twice to discard this minion.
 */
public class Card18_091 extends AbstractMinion {
    public Card18_091() {
        super(2, 7, 1, 4, Race.ORC, Culture.ORC, "Orkish Sneak", null, true);
    }

    @Override
    public List<? extends Action> getPhaseActionsInPlay(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.ASSIGNMENT, self, 0)
                && PlayConditions.canSpot(game, Filters.not(self), Culture.ORC, Race.ORC)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseAndAssignCharacterToMinionEffect(action, playerId, self, Filters.ringBearer, Filters.not(Culture.SHIRE)));

            final String fpPlayer = game.getGameState().getCurrentPlayerId();

            action.appendEffect(
                    new PlayoutDecisionEffect(fpPlayer,
                    new YesNoDecision("Do you want to add burden or exert the Ring-bearer twice to discard " + GameUtils.getFullName(self) + "?") {
                @Override
                protected void yes() {
                    List<Effect> possibleCosts = new LinkedList<Effect>();
                    possibleCosts.add(
                            new AddBurdenEffect(fpPlayer, self, 1));
                    possibleCosts.add(
                            new ChooseAndExertCharactersEffect(action, fpPlayer, 1, 1, 2, Filters.ringBearer) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Exert the Ring-bearer twice";
                        }
                    });
                    SubCostToEffectAction subAction = new SubCostToEffectAction(action);
                    action.appendCost(
                            new ChoiceEffect(subAction, fpPlayer, possibleCosts));
                    action.appendEffect(
                            new DiscardCardsFromPlayEffect(playerId, self, self));
                }
            }));
            return Collections.singletonList(action);
        };
    return null;
    }
}
