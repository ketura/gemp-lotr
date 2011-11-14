package com.gempukku.lotro.cards.set8.gondor;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.CheckLimitEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Siege of Gondor
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 2
 * Type: Condition • Support Area
 * Game Text: Fortification. Regroup: Exert a [GONDOR] Man to play a [GONDOR] fortification from your discard pile
 * (limit 1).
 */
public class Card8_044 extends AbstractPermanent {
    public Card8_044() {
        super(Side.FREE_PEOPLE, 2, CardType.CONDITION, Culture.GONDOR, Zone.SUPPORT, "Sixth Level");
        addKeyword(Keyword.FORTIFICATION);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.REGROUP, self)
                && PlayConditions.canExert(self, game, Culture.GONDOR, Race.MAN)
                && PlayConditions.canPlayFromDiscard(playerId, game, Culture.GONDOR, Keyword.FORTIFICATION)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Culture.GONDOR, Race.MAN));
            action.appendEffect(
                    new CheckLimitEffect(action, self, 1, Phase.REGROUP,
                            new ChooseAndPlayCardFromDiscardEffect(playerId, game.getGameState().getDiscard(playerId), Culture.GONDOR, Keyword.FORTIFICATION)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
