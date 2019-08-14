package com.gempukku.lotro.cards.set4.gondor;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseActionProxyEffect;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.modifiers.MinionSiteNumberModifier;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Skirmish: Spot a Ring-bound Man to make a minion's site number +2. If that minion loses this skirmish,
 * you may wound a minion.
 */
public class Card4_128 extends AbstractEvent {
    public Card4_128() {
        super(Side.FREE_PEOPLE, 1, Culture.GONDOR, "New Errand", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return Filters.canSpot(game, Race.MAN, Keyword.RING_BOUND);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose a minion", CardType.MINION) {
                    @Override
                    protected void cardSelected(final LotroGame game, final PhysicalCard minion) {
                        action.insertEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new MinionSiteNumberModifier(self, Filters.sameCard(minion), null, 2)));
                        action.appendEffect(
                                new AddUntilEndOfPhaseActionProxyEffect(
                                        new AbstractActionProxy() {
                                            @Override
                                            public List<? extends OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult) {
                                                if (TriggerConditions.losesSkirmish(game, effectResult, Filters.sameCard(minion))
                                                        && playerId.equals(game.getGameState().getCurrentPlayerId())) {
                                                    final OptionalTriggerAction action = new OptionalTriggerAction(self);
                                                    action.setVirtualCardAction(true);
                                                    action.appendEffect(
                                                            new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, CardType.MINION));
                                                    return Collections.singletonList(action);
                                                }
                                                return null;
                                            }
                                        }));
                    }
                });
        return action;
    }
}
