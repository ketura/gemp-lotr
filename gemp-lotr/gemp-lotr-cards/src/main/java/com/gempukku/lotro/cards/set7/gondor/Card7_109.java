package com.gempukku.lotro.cards.set7.gondor;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.effects.CommonEffects;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.AbstractExtraPlayCostModifier;
import com.gempukku.lotro.logic.modifiers.cost.AddThreatExtraPlayCostModifier;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 2
 * Type: Condition • Support Area
 * Game Text: To play, add 2 threats. Skirmish: Exert a [GONDOR] Man to wound a roaming minion that Man is skirmishing.
 * Discard this condition at the start of the regroup phase.
 */
public class Card7_109 extends AbstractPermanent {
    public Card7_109() {
        super(Side.FREE_PEOPLE, 2, CardType.CONDITION, Culture.GONDOR, "Long Prepared");
    }

    @Override
    public List<? extends AbstractExtraPlayCostModifier> getExtraCostToPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new AddThreatExtraPlayCostModifier(self, 2, null, self));
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(final String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canExert(self, game, Culture.GONDOR, Race.MAN)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Culture.GONDOR, Race.MAN) {
                        @Override
                        protected void forEachCardExertedCallback(PhysicalCard character) {
                            action.appendEffect(
                                    new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, CardType.MINION, Keyword.ROAMING, Filters.inSkirmishAgainst(character)));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        return CommonEffects.getSelfDiscardAtStartOfRegroup(game, effectResult, self);
    }
}
