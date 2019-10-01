package com.gempukku.lotro.cards.set2.gondor;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayUtils;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractResponseEvent;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.PreventableEffect;
import com.gempukku.lotro.logic.effects.RemoveTwilightEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mines of Moria
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Response: If a [GONDOR] companion wins a skirmish, discard an exhausted Orc. That minion's owner may
 * remove (3) to prevent this.
 */
public class Card2_033 extends AbstractResponseEvent {
    public Card2_033() {
        super(Side.FREE_PEOPLE, 0, Culture.GONDOR, "Flee in Terror");
    }

    @Override
    public List<PlayEventAction> getPlayResponseEventAfterActions(final String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (PlayConditions.canPlayCardDuringPhase(game, (Phase) null, self)
                && TriggerConditions.winsSkirmish(game, effectResult, Filters.and(Culture.GONDOR, CardType.COMPANION))
                && PlayUtils.checkPlayRequirements(game, self, Filters.any, 0, 0, false, false)) {
            final PlayEventAction action = new PlayEventAction(self);
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a minion", Race.ORC, Filters.exhausted) {
                        @Override
                        protected void cardSelected(LotroGame game, final PhysicalCard orc) {
                            action.insertEffect(
                                    new PreventableEffect(action,
                                            new DiscardCardsFromPlayEffect(playerId, self, orc),
                                            Collections.singletonList(orc.getOwner()),
                                            new PreventableEffect.PreventionCost() {
                                                @Override
                                                public Effect createPreventionCostForPlayer(CostToEffectAction subAction, String playerId) {
                                                    return new RemoveTwilightEffect(3);
                                                }
                                            }
                                    ));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
