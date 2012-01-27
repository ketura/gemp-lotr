package com.gempukku.lotro.cards.set15.men;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.PreventableEffect;
import com.gempukku.lotro.cards.effects.ReturnCardsToHandEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Effect;

/**
 * Set: The Hunters
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 1
 * Type: Event • Maneuver
 * Game Text: Exert your [MEN] Man and spot an unbound companion bearing 3 or more cards to return each Free Peoples
 * card that companion bears to its owner’s hand.
 * The Free Peoples player may add a burden to discard those cards instead.
 */
public class Card15_085 extends AbstractEvent {
    public Card15_085() {
        super(Side.SHADOW, 1, Culture.MEN, "Lying Counsel", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canExert(self, game, Filters.owner(playerId), Culture.MEN, Race.MAN)
                && PlayConditions.canSpot(game, Filters.unboundCompanion,
                new Filter() {
                    @Override
                    public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                        return gameState.getAttachedCards(physicalCard).size() >= 3;
                    }
                });
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.owner(playerId), Culture.MEN, Race.MAN));
        action.appendCost(
                new ChooseActiveCardEffect(self, playerId, "Choose an unbound companion", Filters.unboundCompanion,
                        new Filter() {
                            @Override
                            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                                return gameState.getAttachedCards(physicalCard).size() >= 3;
                            }
                        }) {
                    @Override
                    protected void cardSelected(LotroGame game, final PhysicalCard card) {
                        action.appendEffect(
                                new PreventableEffect(action,
                                        new ReturnCardsToHandEffect(self, Filters.and(Side.FREE_PEOPLE, Filters.attachedTo(card))) {
                                            @Override
                                            public String getText(LotroGame game) {
                                                return "Return each Free Peoples card attached to " + GameUtils.getCardLink(card);
                                            }
                                        }, game.getGameState().getCurrentPlayerId(),
                                        new PreventableEffect.PreventionCost() {
                                            @Override
                                            public Effect createPreventionCostForPlayer(SubAction subAction, String playerId) {
                                                return new AddBurdenEffect(self, 1);
                                            }
                                        }, new DiscardCardsFromPlayEffect(self, Side.FREE_PEOPLE, Filters.attachedTo(card))
                                ));
                    }
                });
        return action;
    }
}
