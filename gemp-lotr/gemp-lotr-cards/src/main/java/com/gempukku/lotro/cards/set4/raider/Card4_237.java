package com.gempukku.lotro.cards.set4.raider;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: Plays to your support area. At the end of each phase during which the fellowship moved to site 7T, 8T,
 * or 9T and the twilight pool has fewer than 7 twilight tokens, you may add (8). Skirmish: Discard this condition
 * to make a [RAIDER] Man strength +2.
 */
public class Card4_237 extends AbstractPermanent {
    public Card4_237() {
        super(Side.SHADOW, 0, CardType.CONDITION, Culture.RAIDER, Zone.SUPPORT, "Itilien Wilderness", true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.END_OF_PHASE) {
            if (game.getGameState().getCurrentSiteBlock() == Block.TWO_TOWERS
                    && game.getGameState().getCurrentSiteNumber() >= 7 && game.getGameState().getCurrentSiteNumber() <= 9
                    && game.getGameState().getTwilightPool() < 7) {
                if (game.getGameState().getCurrentPhase() == Phase.FELLOWSHIP
                        || (game.getGameState().getCurrentPhase() == Phase.REGROUP && game.getGameState().getMoveCount() > 1)) {
                    OptionalTriggerAction action = new OptionalTriggerAction(self);
                    action.appendEffect(
                            new AddTwilightEffect(self, 8));
                    return Collections.singletonList(action);
                }
            }
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.SKIRMISH, self, 0)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new DiscardCardsFromPlayEffect(self, self));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose RAIDER Man", Filters.culture(Culture.RAIDER), Filters.race(Race.MAN)) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.insertEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new StrengthModifier(self, Filters.sameCard(self), 2), Phase.SKIRMISH));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
