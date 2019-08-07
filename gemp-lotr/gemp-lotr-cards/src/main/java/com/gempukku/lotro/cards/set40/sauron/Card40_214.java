package com.gempukku.lotro.cards.set40.sauron;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfTurnModifierEffect;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.PreventableEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.CharacterWonSkirmishResult;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Title: *Bound to This Fate
 * Set: Second Edition
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 2
 * Type: Condition - Support Area
 * Card Number: 1U214
 * Game Text: Each time a [SAURON] minion wins a skirmish, you may make that minion fierce until the regroup phase.
 * The Free Peoples player may exert a character of a different culture than the losing companion to prevent this.
 */
public class Card40_214 extends AbstractPermanent {
    public Card40_214() {
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.SAURON, Zone.SUPPORT, "Bound to This Fate", null, true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, final LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, Culture.SAURON, CardType.MINION)) {
            final CharacterWonSkirmishResult winResult = (CharacterWonSkirmishResult) effectResult;
            final PhysicalCard winner = winResult.getWinner();
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.setText("Make " + GameUtils.getFullName(winner) + " fierce");
            action.appendEffect(
                    new PreventableEffect(
                            action, new AddUntilStartOfPhaseModifierEffect(
                            new KeywordModifier(self, winner, Keyword.FIERCE), Phase.REGROUP) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Make " + GameUtils.getFullName(winner) + " fierce";
                        }
                    }, GameUtils.getFreePeoplePlayer(game),
                            new PreventableEffect.PreventionCost() {
                                @Override
                                public Effect createPreventionCostForPlayer(SubAction subAction, String playerId) {
                                    final Collection<PhysicalCard> losers = Filters.filter(winResult.getInvolving(), game.getGameState(), game.getModifiersQuerying(), Side.FREE_PEOPLE, CardType.COMPANION);
                                    List<Culture> loserCultures = new LinkedList<Culture>();
                                    for (PhysicalCard loser : losers) {
                                        loserCultures.add(loser.getBlueprint().getCulture());
                                    }

                                    return new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.character, Filters.not(Filters.or(loserCultures.toArray(new Culture[0])))) {
                                        @Override
                                        public String getText(LotroGame game) {
                                            return "Exert character of a different culture than the losing companion";
                                        }
                                    };
                                }
                            }));
            return Collections.singletonList(action);
        }
        return null;
    }
}
