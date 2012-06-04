package com.gempukku.lotro.cards.set18.orc;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.SubCostToEffectAction;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.OptionalEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAssignCharacterToMinionEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.StackActionEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

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
    protected List<? extends Action> getExtraPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.ASSIGNMENT, self, 0)
                && PlayConditions.canSpot(game, Filters.not(self), Culture.ORC, Race.ORC)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseAndAssignCharacterToMinionEffect(action, playerId, self, Filters.ringBearer, Filters.not(Culture.SHIRE)));

            String fpPlayer = game.getGameState().getCurrentPlayerId();
            List<Effect> possibleCosts = new LinkedList<Effect>();
            possibleCosts.add(
                    new AddBurdenEffect(self, 1));
            possibleCosts.add(
                    new ChooseAndExertCharactersEffect(action, fpPlayer, 1, 1, 2, Filters.ringBearer));
            SubCostToEffectAction subAction = new SubCostToEffectAction(action);
            action.appendCost(
                    new ChoiceEffect(subAction, fpPlayer, possibleCosts));
            action.appendEffect(
                    new DiscardCardsFromPlayEffect(self, self));
            action.appendEffect(
                    new OptionalEffect(action, game.getGameState().getCurrentPlayerId(),
                            new StackActionEffect(subAction) {
                                @Override
                                public String getText(LotroGame game) {
                                    return "Add burden or exert the Ring-bearer twice to discard " + GameUtils.getCardLink(self);
                                }
                            }));
            return Collections.singletonList(action);
        }
        return null;
    }
}
