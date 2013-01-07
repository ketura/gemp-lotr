package com.gempukku.lotro.cards.set20.elven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseActionProxyEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPutCardFromDiscardOnTopOfDeckEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * 1
 * Elven Reflexes
 * Elven	Event • Skirmish
 * Make an Elf strength +2. If that Elf wins this skirmish, put an [Elven] card from your discard pile on top of your draw deck.
 */
public class Card20_082 extends AbstractEvent {
    public Card20_082() {
        super(Side.FREE_PEOPLE, 1, Culture.ELVEN, "Elven Reflexes", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 2, Race.ELF) {
                    @Override
                    protected void selectedCharacterCallback(final PhysicalCard selectedCharacter) {
                        action.appendEffect(
                                new AddUntilEndOfPhaseActionProxyEffect(
                                        new AbstractActionProxy() {
                                            @Override
                                            public List<? extends RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult) {
                                                if (TriggerConditions.winsSkirmish(game, effectResult, selectedCharacter)) {
                                                    RequiredTriggerAction action = new RequiredTriggerAction(self);
                                                    action.setVirtualCardAction(true);
                                                    action.appendEffect(
                                                            new ChooseAndPutCardFromDiscardOnTopOfDeckEffect(action, playerId, 1, 1, Culture.ELVEN));
                                                    return Collections.singletonList(action);
                                                }
                                                return null;
                                            }
                                        }, Phase.SKIRMISH));
                    }
                });
        return action;
    }
}
