package com.gempukku.lotro.cards.set20.dunland;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddUntilStartOfPhaseActionProxyEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.TakeControlOfASiteEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * 2
 * Sneak Attack
 * Event • Maneuver
 * Spot a [Dunland] Man and a site you control to spot an unbound companion.
 * Until the regroup phase, each time that companion loses a skirmish involving a [Dunland] Man, you may
 * take control of a site.
 * http://lotrtcg.org/coreset/dunland/sneakattack(r1).png
 */
public class Card20_028 extends AbstractEvent {
    public Card20_028() {
        super(Side.SHADOW, 2, Culture.DUNLAND, "Sneak Attack", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Culture.DUNLAND, Race.MAN)
                && PlayConditions.canSpot(game, Filters.siteControlled(self.getOwner()));
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, final PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose an unbound companion", Filters.unboundCompanion) {
                    @Override
                    protected void cardSelected(LotroGame game, final PhysicalCard companion) {
                        action.appendEffect(
                                new AddUntilStartOfPhaseActionProxyEffect(
                                        new AbstractActionProxy() {
                                            @Override
                                            public List<? extends OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult) {
                                                if (TriggerConditions.losesSkirmishInvolving(game, effectResult, companion, Filters.and(Culture.DUNLAND, Race.MAN))
                                                        && playerId.equals(self.getOwner())) {
                                                    OptionalTriggerAction action = new OptionalTriggerAction(self);
                                                    action.setVirtualCardAction(true);
                                                    action.appendEffect(
                                                            new TakeControlOfASiteEffect(self, playerId));
                                                    return Collections.singletonList(action);
                                                }
                                                return null;
                                            }
                                        }, Phase.REGROUP));
                    }
                });
        return action;
    }
}
