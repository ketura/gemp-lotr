package com.gempukku.lotro.cards.set8.rohan;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddTokenEffect;
import com.gempukku.lotro.cards.effects.RemoveTokenEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Siege of Gondor
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: Fellowship: Exert a [ROHAN] companion and either a [GONDOR] Man or a [SHIRE] companion to add a [ROHAN]
 * token here. Skirmish: Remove a [ROHAN] token here to exert a minion skirmishing a companion.
 */
public class Card8_086 extends AbstractPermanent {
    public Card8_086() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.ROHAN, Zone.SUPPORT, "Doom Drove Them");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canExert(self, game, Culture.ROHAN, CardType.COMPANION)
                && PlayConditions.canExert(self, game, Filters.or(Filters.and(Culture.GONDOR, Race.MAN), Filters.and(Culture.SHIRE, CardType.COMPANION)))) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Culture.ROHAN, CardType.COMPANION));
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.or(Filters.and(Culture.GONDOR, Race.MAN), Filters.and(Culture.SHIRE, CardType.COMPANION))));
            action.appendEffect(
                    new AddTokenEffect(self, self, Token.ROHAN));
            return Collections.singletonList(action);
        }
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canRemoveTokens(game, Token.ROHAN, 1, self)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveTokenEffect(self, self, Token.ROHAN));
            action.appendEffect(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, CardType.MINION, Filters.inSkirmishAgainst(CardType.COMPANION)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
