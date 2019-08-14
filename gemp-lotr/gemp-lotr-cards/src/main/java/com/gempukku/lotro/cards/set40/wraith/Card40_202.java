package com.gempukku.lotro.cards.set40.wraith;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.*;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Title: To Mordor We Will Take You!
 * Set: Second Edition
 * Side: Shadow
 * Culture: Ringwraith
 * Twilight Cost: 2
 * Type: Event - Skirmish
 * Card Number: 1U202
 * Game Text: Make a Nazgul strength +2. If that Nazgul wins this skirmish, the Free Peoples player must exert the Ring-bearer or add a burden.
 */
public class Card40_202 extends AbstractEvent {
    public Card40_202() {
        super(Side.SHADOW, 2, Culture.WRAITH, "To Mordor We Will Take You!", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 2, Race.NAZGUL) {
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
                                                    List<Effect> possibleEffects = new ArrayList<Effect>(2);
                                                    final String freePeoplePlayer = GameUtils.getFreePeoplePlayer(game);
                                                    possibleEffects.add(
                                                            new AddBurdenEffect(freePeoplePlayer, self, 1));
                                                    possibleEffects.add(
                                                            new ExertCharactersEffect(action, self, Filters.ringBearer));
                                                    action.appendEffect(
                                                            new ChoiceEffect(action, freePeoplePlayer, possibleEffects));
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
