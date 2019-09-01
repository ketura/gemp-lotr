package com.gempukku.lotro.cards.set3.isengard;

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
import com.gempukku.lotro.logic.effects.AddBurdenEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.ExhaustCharacterEffect;
import com.gempukku.lotro.logic.effects.PreventableEffect;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;

/**
 * Set: Realms of Elf-lords
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Maneuver: Spot an [ISENGARD] minion to exhaust Aragorn. The Free Peoples player may add 2 burdens
 * to prevent this.
 */
public class Card3_050 extends AbstractEvent {
    public Card3_050() {
        super(Side.SHADOW, 0, Culture.ISENGARD, "Can You Protect Me From Yourself?", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return Filters.canSpot(game, Culture.ISENGARD, CardType.MINION);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, final LotroGame game, final PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose Aragorn", Filters.aragorn) {
                    @Override
                    protected void cardSelected(final LotroGame game, PhysicalCard aragorn) {
                        action.appendEffect(
                                new PreventableEffect(action,
                                        new ExhaustCharacterEffect(self, action, aragorn),
                                        Collections.singletonList(game.getGameState().getCurrentPlayerId()),
                                        new PreventableEffect.PreventionCost() {
                                            @Override
                                            public Effect createPreventionCostForPlayer(CostToEffectAction subAction, String playerId) {
                                                return new AddBurdenEffect(game.getGameState().getCurrentPlayerId(), self, 2);
                                            }
                                        }
                                ));
                    }
                });
        return action;
    }
}
