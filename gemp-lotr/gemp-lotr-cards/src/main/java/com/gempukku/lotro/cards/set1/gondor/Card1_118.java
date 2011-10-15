package com.gempukku.lotro.cards.set1.gondor;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseActionProxyEffect;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.modifiers.CantTakeWoundsModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Maneuver: Make Aragorn defender +1 and he takes no more than 1 wound during each skirmish phase until the
 * regroup phase.
 */
public class Card1_118 extends AbstractOldEvent {
    public Card1_118() {
        super(Side.FREE_PEOPLE, Culture.GONDOR, "Valiant Man of the West", Phase.MANEUVER);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose Aragorn", Filters.name("Aragorn")) {
                    @Override
                    protected void cardSelected(LotroGame game, final PhysicalCard card) {
                        action.appendEffect(
                                new AddUntilStartOfPhaseModifierEffect(
                                        new KeywordModifier(self, Filters.sameCard(card), Keyword.DEFENDER), Phase.REGROUP));
                        action.appendEffect(
                                new AddUntilStartOfPhaseActionProxyEffect(
                                        new AbstractActionProxy() {
                                            @Override
                                            public List<? extends Action> getRequiredAfterTriggers(LotroGame lotroGame, EffectResult effectResult) {
                                                if (PlayConditions.isWounded(effectResult, card)) {
                                                    ActivateCardAction action = new ActivateCardAction(self);
                                                    action.appendEffect(
                                                            new AddUntilEndOfPhaseModifierEffect(
                                                                    new CantTakeWoundsModifier(self, Filters.sameCard(card)), Phase.SKIRMISH));
                                                    return Collections.<Action>singletonList(action);
                                                }
                                                return null;
                                            }
                                        }, Phase.REGROUP));

                    }
                }
        );
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }
}
