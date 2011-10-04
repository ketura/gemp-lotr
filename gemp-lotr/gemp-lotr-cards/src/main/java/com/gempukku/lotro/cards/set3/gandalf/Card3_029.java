package com.gempukku.lotro.cards.set3.gandalf;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.DrawCardOrPutIntoHandResult;

import java.util.Collections;
import java.util.LinkedList;
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
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.GANDALF, Zone.FREE_SUPPORT, "Betrayal of Isengard");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.DRAW_CARD_OR_PUT_INTO_HAND
                && game.getGameState().getCurrentPhase() == Phase.SHADOW) {
            DrawCardOrPutIntoHandResult drawResult = (DrawCardOrPutIntoHandResult) effectResult;
            if (!drawResult.getPlayerId().equals(playerId)) {
                List<OptionalTriggerAction> actions = new LinkedList<OptionalTriggerAction>();
                for (int i = 0; i < drawResult.getCount(); i++) {
                    OptionalTriggerAction action = new OptionalTriggerAction(self);
                    action.appendEffect(
                            new RemoveTwilightEffect(1));
                    actions.add(action);
                }
                return actions;
            }
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.MANEUVER, self)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), Filters.name("Gandalf"))) {
            ActivateCardAction action = new ActivateCardAction(self, Keyword.MANEUVER);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.name("Gandalf")));
            action.appendEffect(
                    new WoundCharactersEffect(self, Filters.name("Saruman")));
            action.appendEffect(
                    new WoundCharactersEffect(self, Filters.name("Saruman")));
            return Collections.singletonList(action);
        }
        return null;
    }
}
