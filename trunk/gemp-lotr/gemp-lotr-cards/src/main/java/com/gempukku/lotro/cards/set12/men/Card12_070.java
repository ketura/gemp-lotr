package com.gempukku.lotro.cards.set12.men;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndTransferAttachableEffect;
import com.gempukku.lotro.cards.modifiers.CancelStrengthBonusTargetModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: While you can spot 6 companions, each companion skirmishing a [MEN] minion loses all strength bonuses
 * from possessions. Maneuver: Spot your [MEN] minion and discard this condition to transfer a Free Peoples possession
 * borne by a companion to another eligible bearer.
 */
public class Card12_070 extends AbstractPermanent {
    public Card12_070() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.MEN, Zone.SUPPORT, "Hemmed In");
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new CancelStrengthBonusTargetModifier(self, new SpotCondition(6, CardType.COMPANION),
                Filters.and(CardType.COMPANION, Filters.inSkirmishAgainst(Culture.MEN, CardType.MINION)),
                CardType.POSSESSION);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.MANEUVER, self, 0)
                && PlayConditions.canSpot(game, Filters.owner(playerId), Culture.MEN, CardType.MINION)
                && PlayConditions.canSelfDiscard(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new ChooseAndTransferAttachableEffect(action, playerId, Filters.and(CardType.POSSESSION, Side.FREE_PEOPLE), CardType.COMPANION, Filters.any));
            return Collections.singletonList(action);
        }
        return null;
    }
}
