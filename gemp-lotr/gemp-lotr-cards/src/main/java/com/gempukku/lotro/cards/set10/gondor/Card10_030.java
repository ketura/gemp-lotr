package com.gempukku.lotro.cards.set10.gondor;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseActionProxyEffect;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Mount Doom
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 0
 * Type: Event • Skirmish
 * Game Text: Make an exhausted [GONDOR] companion strength +2. If that companion wins this skirmish, heal that
 * companion or make him or her damage +1.
 */
public class Card10_030 extends AbstractEvent {
    public Card10_030() {
        super(Side.FREE_PEOPLE, 0, Culture.GONDOR, "End of the Game", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose exhauster GONDOR companion", Culture.GONDOR, CardType.COMPANION, Filters.exhausted) {
                    @Override
                    protected void cardSelected(LotroGame game, final PhysicalCard card) {
                        action.appendEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, card, 2), Phase.SKIRMISH));
                        action.appendEffect(
                                new AddUntilEndOfPhaseActionProxyEffect(
                                        new AbstractActionProxy() {
                                            @Override
                                            public List<? extends Action> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult) {
                                                if (PlayConditions.winsSkirmish(game, effectResult, card)) {
                                                    RequiredTriggerAction action = new RequiredTriggerAction(self);
                                                    List<Effect> possibleEffects = new LinkedList<Effect>();
                                                    possibleEffects.add(
                                                            new HealCharactersEffect(self, card));
                                                    possibleEffects.add(
                                                            new AddUntilEndOfPhaseModifierEffect(
                                                                    new KeywordModifier(self, card, Keyword.DAMAGE, 1), Phase.SKIRMISH) {
                                                                @Override
                                                                public String getText(LotroGame game) {
                                                                    return "Make " + GameUtils.getCardLink(card) + " damage +1";
                                                                }
                                                            });
                                                }
                                                return null;
                                            }
                                        }, Phase.SKIRMISH));
                    }
                });
        return action;
    }
}
