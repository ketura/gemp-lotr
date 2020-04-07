package com.gempukku.lotro.cards.set3.gandalf;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.RemoveTwilightEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.timing.results.DrawCardOrPutIntoHandResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Realms of Elf-lords
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Plays to your support area. Each time an opponent draws a card (or takes a card into hand) during
 * the Shadow phase, you may remove (1). Maneuver: Exert Gandalf to wound Saruman twice.
 */
public class Card3_029 extends AbstractPermanent {
    public Card3_029() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.GANDALF, "Betrayal of Isengard");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.forEachCardDrawnOrPutIntoHandByOpponent(game, effectResult, playerId)
                && game.getGameState().getCurrentPhase() == Phase.SHADOW) {
            DrawCardOrPutIntoHandResult drawResult = (DrawCardOrPutIntoHandResult) effectResult;
            if (!drawResult.getPlayerId().equals(playerId)) {
                OptionalTriggerAction action = new OptionalTriggerAction(self);
                action.appendEffect(
                        new RemoveTwilightEffect(1));
                return Collections.singletonList(action);
            }
        }
        return null;
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.MANEUVER, self)
                && PlayConditions.canExert(self, game, Filters.gandalf)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.gandalf));
            action.appendEffect(
                    new WoundCharactersEffect(self, Filters.saruman));
            action.appendEffect(
                    new WoundCharactersEffect(self, Filters.saruman));
            return Collections.singletonList(action);
        }
        return null;
    }
}
