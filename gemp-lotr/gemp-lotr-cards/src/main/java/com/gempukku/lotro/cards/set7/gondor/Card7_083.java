package com.gempukku.lotro.cards.set7.gondor;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseActionProxyEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Event • Skirmish
 * Game Text: Make a [GONDOR] Man strength +2. If that Man is skirmishing a [SAURON] minion, discard that minion
 * at the end of the skirmish.
 */
public class Card7_083 extends AbstractEvent {
    public Card7_083() {
        super(Side.FREE_PEOPLE, 1, Culture.GONDOR, "City of Men", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose a GONDOR Man", Culture.GONDOR, Race.MAN) {
                    @Override
                    protected void cardSelected(LotroGame game, final PhysicalCard card) {
                        game.getModifiersEnvironment().addUntilEndOfPhaseModifier(
                                new StrengthModifier(self, card, 2), Phase.SKIRMISH);
                        if (Filters.inSkirmishAgainst(Culture.SAURON, CardType.MINION).accepts(game.getGameState(), game.getModifiersQuerying(), card)) {
                            action.appendEffect(
                                    new AddUntilEndOfPhaseActionProxyEffect(
                                            new AbstractActionProxy() {
                                                @Override
                                                public List<? extends Action> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult) {
                                                    if (TriggerConditions.endOfPhase(game, effectResult, Phase.SKIRMISH)) {
                                                        RequiredTriggerAction action = new RequiredTriggerAction(self);
                                                        action.appendEffect(
                                                                new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Culture.SAURON, CardType.MINION, Filters.inSkirmishAgainst(card)));
                                                        return Collections.singletonList(action);
                                                    }
                                                    return null;
                                                }
                                            }, Phase.SKIRMISH));
                        }
                    }
                });
        return action;
    }
}
