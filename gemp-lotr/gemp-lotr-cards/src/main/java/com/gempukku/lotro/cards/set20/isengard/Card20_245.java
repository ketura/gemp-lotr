package com.gempukku.lotro.cards.set20.isengard;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

/**
 * 1
 * Voice of Saruman
 * Isengard	Event • Maneuver
 * Spell.
 * Exert Saruman to make the Free Peoples Player exert a companion for each [Isengard] spell stacked on an [Isengard] artifact (limit 3).
 */
public class Card20_245 extends AbstractEvent {
    public Card20_245() {
        super(Side.SHADOW, 1, Culture.ISENGARD, "Voice of Saruman", Phase.MANEUVER);
        addKeyword(Keyword.SPELL);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Filters.saruman);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.saruman));
        action.appendEffect(
                new UnrespondableEffect() {
                    @Override
                    protected void doPlayEffect(LotroGame game) {
                        int count = 0;
                        for (PhysicalCard artifact : Filters.filterActive(game, Culture.ISENGARD, CardType.ARTIFACT, Filters.hasStacked(Culture.ISENGARD, Keyword.SPELL))) {
                            count+=Filters.filter(game.getGameState().getStackedCards(artifact), game, Culture.ISENGARD, Keyword.SPELL).size();
                        }
                        count = Math.min(3, count);
                        for (int i=0; i<count; i++)
                            action.appendEffect(
                                    new ChooseAndExertCharactersEffect(action, game.getGameState().getCurrentPlayerId(), 1, 1, CardType.COMPANION));
                    }
                });
        return action;
    }
}
