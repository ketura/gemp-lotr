package com.gempukku.lotro.cards.set40.gondor;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseActionProxyEffect;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.PlayNextSiteEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Title: Ranger's Guile
 * Set: Second Edition
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 0
 * Type: Event - Skirmish
 * Card Number: 1R116
 * Game Text: Make a ranger strength +1. If that ranger wins this skirmish, you may play the fellowship's next site
 * (replacing opponent's site if necessary) or wound a roaming minion twice.
 */
public class Card40_116 extends AbstractEvent {
    public Card40_116() {
        super(Side.FREE_PEOPLE, 0, Culture.GONDOR, "Ranger's Guile", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 1, Keyword.RANGER) {
                    @Override
                    protected void selectedCharacterCallback(final PhysicalCard selectedCharacter) {
                        action.appendEffect(
                                new AddUntilEndOfPhaseActionProxyEffect(
                                        new AbstractActionProxy() {
                                            @Override
                                            public List<? extends OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult) {
                                                if (TriggerConditions.winsSkirmish(game, effectResult, selectedCharacter)
                                                        && playerId.equals(self.getOwner())) {
                                                    OptionalTriggerAction action = new OptionalTriggerAction(self);
                                                    action.setVirtualCardAction(true);
                                                    List<Effect> possibleEffects = new ArrayList<Effect>(2);
                                                    possibleEffects.add(
                                                            new PlayNextSiteEffect(action, playerId));
                                                    possibleEffects.add(
                                                            new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, 2,
                                                                    Filters.roamingMinion) {
                                                                @Override
                                                                public String getText(LotroGame game) {
                                                                    return "Wound a roaming minion twice";
                                                                }
                                                            });
                                                    action.appendEffect(
                                                            new ChoiceEffect(action, playerId, possibleEffects));
                                                    return Collections.singletonList(action);
                                                }
                                                return null;
                                            }
                                        }
                                ));
                    }
                });
        return action;
    }
}
