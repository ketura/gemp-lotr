package com.gempukku.lotro.cards.set18.gandalf;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddTokenEffect;
import com.gempukku.lotro.cards.effects.RemoveTokenEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Treachery & Deceit
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 2
 * Type: Condition • Support Area
 * Game Text: Fellowship: Exert a [GANDALF] Man to add 2 [GANDALF] tokens here. Skirmish: Remove 3 [GANDALF] tokens
 * from here to make a companion strength +3.
 */
public class Card18_021 extends AbstractPermanent {
    public Card18_021() {
        super(Side.FREE_PEOPLE, 2, CardType.CONDITION, Culture.GANDALF, Zone.SUPPORT, "Last Stand");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canExert(self, game, Culture.GANDALF, Race.MAN)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Culture.GANDALF, Race.MAN));
            action.appendEffect(
                    new AddTokenEffect(self, self, Token.GANDALF, 2));
            return Collections.singletonList(action);
        }
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canRemoveTokens(game, self, Token.GANDALF, 3)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveTokenEffect(self, self, Token.GANDALF, 3));
            action.appendEffect(
                    new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 3, CardType.COMPANION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
