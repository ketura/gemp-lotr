package com.gempukku.lotro.cards.set20.sauron;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.TriggerConditions;
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

import java.util.Collections;
import java.util.List;

/**
 * 2
 * •Bound to This Fate
 * Sauron	Condition • Support Area
 * Each time a [Sauron] minion wins a skirmish, you may make that minion fierce until the regroup phase.
 * The Free Peoples player may exert a companion of a different culture than the losing companion to prevent this.
 */
public class Card20_353 extends AbstractPermanent {
    public Card20_353() {
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.SAURON, Zone.SUPPORT, "Bound to This Fate", null, true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, Culture.SAURON, CardType.MINION, Filters.owner(playerId))) {
            final CharacterWonSkirmishResult result = (CharacterWonSkirmishResult) effectResult;
            final PhysicalCard winner = result.getWinner();
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.setText("Make " + GameUtils.getFullName(winner) + " fierce");
            action.appendEffect(
                    new PreventableEffect(action,
                            new AddUntilStartOfPhaseModifierEffect(
                                    new KeywordModifier(self, winner, Keyword.FIERCE), Phase.REGROUP) {
                                @Override
                                public String getText(LotroGame game) {
                                    return "Make " + GameUtils.getFullName(winner) + " fierce";
                                }
                            }, game.getGameState().getCurrentPlayerId(),
                            new PreventableEffect.PreventionCost() {
                                @Override
                                public Effect createPreventionCostForPlayer(SubAction subAction, String playerId) {
                                    Culture loserCulture = null;
                                    if (result.getInvolving().size() == 1) {
                                        final PhysicalCard loser = result.getInvolving().iterator().next();
                                        if (loser.getBlueprint().getCardType() == CardType.COMPANION)
                                            loserCulture = loser.getBlueprint().getCulture();
                                    }

                                    Filterable filter;
                                    if (loserCulture != null)
                                        filter = Filters.and(CardType.COMPANION, Filters.not(loserCulture));
                                    else
                                        filter = CardType.COMPANION;
                                    return new ChooseAndExertCharactersEffect(action, playerId, 1, 1, filter) {
                                        @Override
                                        public String getText(LotroGame game) {
                                            return "Exert a companion of a different culture than the losing companion";
                                        }
                                    };
                                }
                            }
                    ));
            return Collections.singletonList(action);
        }
        return null;
    }
}
