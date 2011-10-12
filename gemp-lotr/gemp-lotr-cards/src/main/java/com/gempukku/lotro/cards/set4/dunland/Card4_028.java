package com.gempukku.lotro.cards.set4.dunland;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddTokenEffect;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.RemoveTokenEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.cards.modifiers.CancelStrengthBonusModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.Action;

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
        super(Side.SHADOW, 0, CardType.CONDITION, Culture.DUNLAND, Zone.SUPPORT, "No Defense");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.SHADOW, self, 3)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveTwilightEffect(3));
            action.appendEffect(
                    new AddTokenEffect(self, self, Token.DUNLAND));
            return Collections.singletonList(action);
        }
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.SKIRMISH, self, 0)
                && PlayConditions.canRemoveToken(game.getGameState(), self, Token.DUNLAND)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.DUNLAND), Filters.race(Race.MAN))) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveTokenEffect(self, self, Token.DUNLAND));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose DUNLAND Man", Filters.culture(Culture.DUNLAND), Filters.race(Race.MAN)) {
                        @Override
                        protected void cardSelected(PhysicalCard dunlandMan) {
                            action.appendEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new CancelStrengthBonusModifier(self, Filters.and(Filters.type(CardType.POSSESSION), Filters.attachedTo(Filters.inSkirmishAgainst(Filters.sameCard(dunlandMan))))), Phase.SKIRMISH));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
