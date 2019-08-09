package com.gempukku.lotro.cards.set18.gondor;

import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.AddTokenEffect;
import com.gempukku.lotro.logic.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.PreventableEffect;
import com.gempukku.lotro.logic.effects.RemoveTokenEffect;
import com.gempukku.lotro.logic.effects.RemoveTwilightEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.common.Token;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.CantBeAssignedToSkirmishModifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Treachery & Deceit
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 3
 * Type: Condition • Support Area
 * Game Text: Tale. Each time a non-[WRAITH] minion is played, you may spot a Man to put a [GONDOR] token here.
 * Maneuver: Remove 3 [GONDOR] tokens from here to spot a minion. That minion cannot be assigned to a skirmish until
 * the regroup phase. Any Shadow player may remove (2) to prevent this.
 */
public class Card18_050 extends AbstractPermanent {
    public Card18_050() {
        super(Side.FREE_PEOPLE, 3, CardType.CONDITION, Culture.GONDOR, "The Faithful Stone");
        addKeyword(Keyword.TALE);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, CardType.MINION, Filters.not(Culture.WRAITH))
                && PlayConditions.canSpot(game, Race.MAN)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new AddTokenEffect(self, self, Token.GONDOR));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.MANEUVER, self)
                && PlayConditions.canRemoveTokens(game, self, Token.GONDOR, 3)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveTokenEffect(self, self, Token.GONDOR, 3));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a minion", CardType.MINION) {
                        @Override
                        protected void cardSelected(LotroGame game, final PhysicalCard minion) {
                            action.appendEffect(
                                    new PreventableEffect(action,
                                            new AddUntilStartOfPhaseModifierEffect(
                                                    new CantBeAssignedToSkirmishModifier(self, minion), Phase.REGROUP) {
                                                @Override
                                                public String getText(LotroGame game) {
                                                    return "Make " + GameUtils.getFullName(minion) + " not assignable to skirmish until the regroup phase";
                                                }
                                            }, GameUtils.getShadowPlayers(game),
                                            new PreventableEffect.PreventionCost() {
                                                @Override
                                                public Effect createPreventionCostForPlayer(SubAction subAction, String playerId) {
                                                    return new RemoveTwilightEffect(2);
                                                }
                                            }
                                    ));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
