package com.gempukku.lotro.cards.set3.moria;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.effects.PreventableEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collections;

/**
 * Set: Realms of Elf-lords
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Maneuver: Spot a [MORIA] minion to wound Boromir 3 times. The Free Peoples player may discard 2 Free
 * Peoples possessions to prevent this.
 */
public class Card3_080 extends AbstractEvent {
    public Card3_080() {
        super(Side.SHADOW, 0, Culture.MORIA, "Such a Little Thing", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return Filters.canSpot(game, Culture.MORIA, CardType.MINION);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, LotroGame game, PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new PreventableEffect(action,
                        new UnrespondableEffect() {
                            @Override
                            public String getText(LotroGame game) {
                                return "Wound Boromir 3 times";
                            }

                            @Override
                            protected void doPlayEffect(LotroGame game) {
                                action.appendEffect(
                                        new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, Filters.boromir));
                                action.appendEffect(
                                        new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, Filters.boromir));
                                action.appendEffect(
                                        new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, Filters.boromir));
                            }
                        }, Collections.singletonList(game.getGameState().getCurrentPlayerId()),
                        new PreventableEffect.PreventionCost() {
                            @Override
                            public Effect createPreventionCostForPlayer(CostToEffectAction subAction, String playerId) {
                                return new ChooseAndDiscardCardsFromPlayEffect(subAction, playerId, 2, 2, Side.FREE_PEOPLE, CardType.POSSESSION) {
                                    @Override
                                    public String getText(LotroGame game) {
                                        return "Discard 2 Free People possessions";
                                    }
                                };
                            }
                        }
                ));
        return action;
    }
}
