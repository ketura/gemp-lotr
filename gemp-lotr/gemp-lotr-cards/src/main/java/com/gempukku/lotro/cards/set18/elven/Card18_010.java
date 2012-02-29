package com.gempukku.lotro.cards.set18.elven;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddTokenEffect;
import com.gempukku.lotro.cards.effects.RemoveTokenEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndRemoveCultureTokensFromCardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Treachery & Deceit
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Condition • Support Area
 * Game Text: Fellowship: Remove a culture token from a Free Peoples card to add an [ELVEN] token here.
 * Skirmish: Remove 2 [ELVEN] tokens from here to make a minion skirmishing an [ELVEN] companion strength -1.
 */
public class Card18_010 extends AbstractPermanent {
    public Card18_010() {
        super(Side.FREE_PEOPLE, 2, CardType.CONDITION, Culture.ELVEN, Zone.SUPPORT, "Elven Supplies");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canRemoveAnyCultureTokens(game, 1, Side.FREE_PEOPLE)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndRemoveCultureTokensFromCardEffect(self, playerId, null, 1, Side.FREE_PEOPLE));
            action.appendEffect(
                    new AddTokenEffect(self, self, Token.ELVEN));
            return Collections.singletonList(action);
        }
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canRemoveTokens(game, self, Token.ELVEN, 2)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveTokenEffect(self, self, Token.ELVEN, 2));
            action.appendEffect(
                    new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, -1, CardType.MINION, Filters.inSkirmishAgainst(Culture.ELVEN, CardType.COMPANION)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
