package com.gempukku.lotro.cards.set4.gondor;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.CancelSkirmishEffect;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.PreventableEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.List;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Possession • Gauntlets
 * Game Text: Bearer must be a [GONDOR] Man. Skirmish: Exert bearer to cancel a skirmish involving bearer. Any Shadow
 * player may remove (1) to prevent this.
 */
public class Card4_112 extends AbstractAttachableFPPossession {
    public Card4_112() {
        super(1, 0, 0, Culture.GONDOR, Keyword.GAUNTLETS, "Boromir's Gauntlets", true);
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Filters.culture(Culture.GONDOR), Filters.race(Race.MAN));
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.SKIRMISH, self)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), self.getAttachedTo())) {
            ActivateCardAction action = new ActivateCardAction(self, Keyword.SKIRMISH);
            action.appendCost(
                    new ExertCharactersEffect(self, self.getAttachedTo()));
            action.appendEffect(
                    new PreventableEffect(action,
                            new CancelSkirmishEffect(Filters.hasAttached(self)),
                            GameUtils.getOpponents(game, playerId),
                            new RemoveTwilightEffect(1)));
        }
        return null;
    }
}
