package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseActionProxyEffect;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardAtRandomFromHandEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.timing.results.CharacterLostSkirmishResult;

import java.util.Collections;
import java.util.List;

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
        super(Side.FREE_PEOPLE, 0, Culture.ELVEN, "Ancient Enmity", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, final PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose an Elf", Race.ELF) {
                    @Override
                    protected void cardSelected(LotroGame game, final PhysicalCard elf) {
                        action.appendEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, Filters.sameCard(elf), 1)));
                        action.appendEffect(
                                new AddUntilEndOfPhaseActionProxyEffect(
                                        new AbstractActionProxy() {
                                            @Override
                                            public List<? extends RequiredTriggerAction> getRequiredAfterTriggers(LotroGame lotroGame, EffectResult effectResult) {
                                                if (TriggerConditions.losesSkirmishInvolving(lotroGame, effectResult, CardType.MINION, elf)) {
                                                    CharacterLostSkirmishResult skirmishResult = (CharacterLostSkirmishResult) effectResult;
                                                    PhysicalCard loser = skirmishResult.getLoser();
                                                    RequiredTriggerAction action = new RequiredTriggerAction(self);
                                                    action.appendEffect(new DiscardCardAtRandomFromHandEffect(self, loser.getOwner(), true));
                                                    action.appendEffect(new DiscardCardAtRandomFromHandEffect(self, loser.getOwner(), true));
                                                    return Collections.singletonList(action);
                                                }
                                                return null;
                                            }
                                        }));
                    }
                }
        );
        return action;
    }
}
