package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseActionProxyEffect;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.CardAffectsCardEffect;
import com.gempukku.lotro.cards.effects.DiscardCardAtRandomFromHandEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.SkirmishResult;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Skirmish: Make an Elf strength +1. If a minion loses this skirmish to that Elf, that minion's owner
 * discards 2 cards at random from hand.
 */
public class Card1_029 extends AbstractEvent {
    public Card1_029() {
        super(Side.FREE_PEOPLE, Culture.ELVEN, "Ancient Enmity", Phase.SKIRMISH);
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(playerId, "Choose an Elf", Filters.race(Race.ELF)) {
                    @Override
                    protected void cardSelected(final PhysicalCard elf) {
                        action.appendEffect(new CardAffectsCardEffect(self, elf));
                        action.appendEffect(new AddUntilEndOfPhaseModifierEffect(new StrengthModifier(self, Filters.sameCard(elf), 1), Phase.SKIRMISH));
                        action.appendEffect(
                                new AddUntilEndOfPhaseActionProxyEffect(
                                        new AbstractActionProxy() {
                                            @Override
                                            public List<? extends Action> getRequiredAfterTriggers(LotroGame lotroGame, EffectResult effectResult) {
                                                if (PlayConditions.winsSkirmish(effectResult, elf)) {
                                                    SkirmishResult skirmishResult = (SkirmishResult) effectResult;
                                                    List<PhysicalCard> losers = skirmishResult.getLosers();
                                                    Set<String> opponents = new HashSet<String>();
                                                    for (PhysicalCard loser : losers)
                                                        opponents.add(loser.getOwner());

                                                    List<Action> actions = new LinkedList<Action>();
                                                    for (String opponent : opponents) {
                                                        ActivateCardAction action = new ActivateCardAction(self, Keyword.SKIRMISH);
                                                        action.appendEffect(new DiscardCardAtRandomFromHandEffect(opponent));
                                                        action.appendEffect(new DiscardCardAtRandomFromHandEffect(opponent));
                                                        actions.add(action);
                                                    }
                                                    return actions;
                                                }
                                                return null;
                                            }
                                        }, Phase.SKIRMISH
                                ));
                    }
                }
        );
        return action;
    }
}
