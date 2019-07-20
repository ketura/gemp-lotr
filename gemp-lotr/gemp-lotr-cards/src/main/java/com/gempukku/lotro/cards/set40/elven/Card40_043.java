package com.gempukku.lotro.cards.set40.elven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseActionProxyEffect;
import com.gempukku.lotro.cards.effects.OptionalEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPutCardFromDiscardOnTopOfDeckEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Title: Elven Reflexes
 * Set: Second Edition
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 0
 * Type: Event - Skirmish
 * Card Number: 1R43
 * Game Text: Make an Elf strength +1. If that Elf wins this skirmish, you may place up to 2 [ELVEN] cards from your discard pile on top of your draw deck.
 */
public class Card40_043 extends AbstractEvent {
    public Card40_043() {
        super(Side.FREE_PEOPLE, 0, Culture.ELVEN, "Elven Reflexes", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 1, Race.ELF) {
                    @Override
                    protected void selectedCharacterCallback(final PhysicalCard selectedCharacter) {
                        action.appendEffect(
                                new AddUntilEndOfPhaseActionProxyEffect(
                                        new AbstractActionProxy() {
                                            @Override
                                            public List<? extends OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult) {
                                                if (TriggerConditions.winsSkirmish(game, effectResult, selectedCharacter)) {
                                                    OptionalTriggerAction action = new OptionalTriggerAction(self);
                                                    action.setVirtualCardAction(true);
                                                    action.appendEffect(
                                                            new OptionalEffect(action, playerId,
                                                                    new ChooseAndPutCardFromDiscardOnTopOfDeckEffect(action, playerId, 0, 2, Culture.ELVEN) {
                                                                        @Override
                                                                        public String getText(LotroGame game) {
                                                                            return "Put up to 2 [ELVEN] cards from your discard pile on top of your draw deck";
                                                                        }
                                                                    }));
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
