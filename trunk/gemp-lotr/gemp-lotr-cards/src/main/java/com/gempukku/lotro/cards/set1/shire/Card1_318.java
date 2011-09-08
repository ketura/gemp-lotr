package com.gempukku.lotro.cards.set1.shire;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.cards.effects.PlaySiteEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardsEffect;
import com.gempukku.lotro.logic.effects.DiscardCardFromPlayEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Possession
 * Game Text: Plays to your support area. Fellowship or Regroup: Exert 2 Hobbits and discard Thror's Map to play the
 * fellowship's next site (replacing opponent's site if necessary).
 */
public class Card1_318 extends AbstractLotroCardBlueprint {
    public Card1_318() {
        super(Side.FREE_PEOPLE, CardType.POSSESSION, Culture.SHIRE, "Thror's Map", true);
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return PlayConditions.checkUniqueness(game.getGameState(), game.getModifiersQuerying(), self);
    }

    @Override
    public Action getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return new PlayPermanentAction(self, Zone.FREE_SUPPORT);
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canPlayCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && checkPlayRequirements(playerId, game, self, 0)) {
            return Collections.singletonList(getPlayCardAction(playerId, game, self, 0));
        }

        if ((PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                || PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.REGROUP, self))
                && Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.HOBBIT), Filters.canExert()) >= 2) {
            Keyword phaseKeyword = (game.getGameState().getCurrentPhase() == Phase.FELLOWSHIP) ? Keyword.FELLOWSHIP : Keyword.REGROUP;
            final DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, phaseKeyword, "Exert 2 Hobbits and discard Thror's Map to play the fellowship's next site (replacing opponent's site if necessary)");
            action.addCost(
                    new ChooseActiveCardsEffect(playerId, "Choose Hobbits", 2, 2, Filters.keyword(Keyword.HOBBIT), Filters.canExert()) {
                        @Override
                        protected void cardsSelected(List<PhysicalCard> cards) {
                            action.addCost(new ExertCharacterEffect(cards.get(0)));
                            action.addCost(new ExertCharacterEffect(cards.get(1)));
                        }
                    });
            action.addCost(new DiscardCardFromPlayEffect(self, self));
            action.addEffect(new PlaySiteEffect(playerId, game.getGameState().getCurrentSiteNumber() + 1));
            return Collections.singletonList(action);
        }
        return null;
    }
}
