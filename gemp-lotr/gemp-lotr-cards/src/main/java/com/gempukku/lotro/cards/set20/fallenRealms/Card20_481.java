package com.gempukku.lotro.cards.set20.fallenRealms;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.effects.*;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.timing.results.PlayCardResult;

import java.util.Collections;
import java.util.List;

/**
 * ❸ Easterling Plotter [Fal]
 * Minion • Man
 * Strength: 6   Vitality: 1   Roaming: 4
 * Easterling. Toil 1. (When you play this card, you may reduce its twilight cost by 1. You do this by exerting one of your characters of the same culture as this card.)
 * When you play this minion using toil, you may spot another Easterling to wound a companion. The Free Peoples player may add a threat to prevent this.
 * Skirmish: Remove a threat to make this minion strength +2.
 * <p/>
 * http://lotrtcg.org/coreset/fallenrealms/easterlingplotter(r3).jpg
 */
public class Card20_481 extends AbstractMinion {
    public Card20_481() {
        super(3, 6, 1, 4, Race.MAN, Culture.FALLEN_REALMS, "Easterling Plotter");
        addKeyword(Keyword.EASTERLING);
        addKeyword(Keyword.TOIL, 1);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self) && ((PlayCardResult) effectResult).isPaidToil()
                && PlayConditions.canSpot(game, Filters.not(self), Keyword.EASTERLING)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new PreventableEffect(
                            action, new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, CardType.COMPANION) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Wound a companion";
                        }
                    }, GameUtils.getFreePeoplePlayer(game),
                            new PreventableEffect.PreventionCost() {
                                @Override
                                public Effect createPreventionCostForPlayer(CostToEffectAction subAction, String playerId) {
                                    return new AddThreatsEffect(playerId, self, 1);
                                }
                            }));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && PlayConditions.canRemoveThreat(game, self, 1)) {
            ActivateCardAction action =new ActivateCardAction(self);
            action.appendCost(
                    new RemoveThreatsEffect(self, 1));
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new StrengthModifier(self, self, 2)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
