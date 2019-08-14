package com.gempukku.lotro.cards.set4.dunland;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.*;
import com.gempukku.lotro.logic.modifiers.CancelStrengthBonusTargetModifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Dunland
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: Plays to your support area. Shadow: Remove (3) to place a [DUNLAND] token here. Skirmish: Spot
 * a [DUNLAND] Man and remove a [DUNLAND] token from this card. A character skirmishing that [DUNLAND] Man does not gain
 * strength bonuses from possessions.
 */
public class Card4_028 extends AbstractPermanent {
    public Card4_028() {
        super(Side.SHADOW, 0, CardType.CONDITION, Culture.DUNLAND, "No Defense");
    }

    @Override
    public List<? extends Action> getPhaseActionsInPlay(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 3)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveTwilightEffect(3));
            action.appendEffect(
                    new AddTokenEffect(self, self, Token.DUNLAND));
            return Collections.singletonList(action);
        }
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && PlayConditions.canRemoveTokens(game, self, Token.DUNLAND)
                && Filters.canSpot(game, Culture.DUNLAND, Race.MAN)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveTokenEffect(self, self, Token.DUNLAND));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose DUNLAND Man", Culture.DUNLAND, Race.MAN) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard dunlandMan) {
                            action.appendEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new CancelStrengthBonusTargetModifier(self,
                                                    Filters.and(Filters.character, Filters.inSkirmishAgainst(dunlandMan)),
                                                    CardType.POSSESSION)));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
