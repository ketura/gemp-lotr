package com.gempukku.lotro.cards.set20.dunland;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.PreventableEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.Effect;

/**
 * 1
 * Plundered
 * Dunland	Event • Skirmish
 * Discard a possession borne by a companion skirmishing a [Dunland] Man. The Free Peoples player may exert
 * that companion to prevent this.
 */
public class Card20_025 extends AbstractEvent {
    public Card20_025() {
        super(Side.SHADOW, 1, Culture.DUNLAND, "Plundered", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose a possession", CardType.POSSESSION, Filters.attachedTo(CardType.COMPANION, Filters.inSkirmishAgainst(Culture.DUNLAND, Race.MAN))) {
                    @Override
                    protected void cardSelected(LotroGame game, final PhysicalCard possession) {
                        action.appendEffect(
                                new PreventableEffect(action,
                                        new DiscardCardsFromPlayEffect(self, possession), game.getGameState().getCurrentPlayerId(),
                                        new PreventableEffect.PreventionCost() {
                                            @Override
                                            public Effect createPreventionCostForPlayer(SubAction subAction, String playerId) {
                                                return new ExertCharactersEffect(action, self, possession.getAttachedTo());
                                            }
                                        }));
                    }
                });
        return action;
    }
}
