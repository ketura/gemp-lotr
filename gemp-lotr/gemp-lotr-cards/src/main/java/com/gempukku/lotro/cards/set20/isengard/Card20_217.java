package com.gempukku.lotro.cards.set20.isengard;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collection;

/**
 * 0
 * Build Me an Army
 * Isengard	Event • Shadow
 * Discard X [Isengard] Orcs to play an Uruk-hai from your discard pile; its twilight cost is -X and it is fierce
 * (and damage +1 if you can spot 6 companions) until the regroup phase.
 */
public class Card20_217 extends AbstractEvent {
    public Card20_217() {
        super(Side.SHADOW, 0, Culture.ISENGARD, "Build Me an Army", Phase.SHADOW);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canPlayFromDiscard(self.getOwner(), game, -Filters.countActive(game, Culture.ISENGARD, Race.ORC), Race.URUK_HAI);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 0, Integer.MAX_VALUE, Culture.ISENGARD, Race.ORC) {
                    @Override
                    protected void cardsToBeDiscardedCallback(Collection<PhysicalCard> cards) {
                        int count = cards.size();
                        action.appendEffect(
                                new ChooseAndPlayCardFromDiscardEffect(playerId, game, -count, Race.URUK_HAI) {
                                    @Override
                                    protected void afterCardPlayed(PhysicalCard cardPlayed) {
                                        action.appendEffect(
                                                new AddUntilStartOfPhaseModifierEffect(
                                                        new KeywordModifier(self, cardPlayed, Keyword.FIERCE), Phase.REGROUP));
                                        if (Filters.canSpot(game, 6, CardType.COMPANION))
                                            action.appendEffect(
                                                    new AddUntilStartOfPhaseModifierEffect(
                                                            new KeywordModifier(self, cardPlayed, Keyword.DAMAGE, 1), Phase.REGROUP));
                                    }
                                });
                    }
                });
        return action;
    }
}
