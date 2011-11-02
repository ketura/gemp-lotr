package com.gempukku.lotro.cards.set7.gondor;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPutCardFromDiscardOnTopOfDeckEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 3
 * Type: Condition • Support Area
 * Game Text: To play, spot 3 [GONDOR] companions. While no opponent controls a site, wound a minion at the start
 * of each regroup phase. Regroup: Discard this condition to place a [GONDOR] fortification from your discard pile
 * on top of your draw deck.
 */
public class Card7_096 extends AbstractPermanent {
    public Card7_096() {
        super(Side.FREE_PEOPLE, 3, CardType.CONDITION, Culture.GONDOR, Zone.SUPPORT, "Gondor Still Stands", true);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && PlayConditions.canSpot(game, 3, Culture.GONDOR, CardType.COMPANION);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.startOfPhase(game, effectResult, Phase.REGROUP)
                && Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Filters.siteControlledByShadowPlayer(self.getOwner())) == 0) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new ChooseAndWoundCharactersEffect(action, self.getOwner(), 1, 1, CardType.MINION));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.REGROUP, self)
                && PlayConditions.canSelfDiscard(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new DiscardCardsFromPlayEffect(self, self));
            action.appendEffect(
                    new ChooseAndPutCardFromDiscardOnTopOfDeckEffect(action, playerId, 1, 1, Culture.GONDOR, Keyword.FORTIFICATION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
