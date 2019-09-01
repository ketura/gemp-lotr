package com.gempukku.lotro.cards.set12.shire;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.*;
import com.gempukku.lotro.logic.modifiers.OverwhelmedByMultiplierModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

/**
 * Set: Black Rider
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Event • Skirmish
 * Game Text: Prevent a Hobbit from being overwhelmed unless his or her strength is tripled. Then, if the fellowship is
 * at a dwelling site, you may discard 2 cards from hand to return this event to your hand.
 */
public class Card12_126 extends AbstractEvent {
    public Card12_126() {
        super(Side.FREE_PEOPLE, 1, Culture.SHIRE, "No Worse for Wear", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, LotroGame game, final PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose a Hobbit", Race.HOBBIT) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        action.insertEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new OverwhelmedByMultiplierModifier(self, card, 3)));
                    }
                });
        action.appendEffect(
                new UnrespondableEffect() {
                    @Override
                    protected void doPlayEffect(LotroGame game) {
                        if (PlayConditions.location(game, Keyword.DWELLING)
                                && PlayConditions.canDiscardFromHand(game, playerId, 2, Filters.any)) {
                            SubAction subAction = new SubAction(action);
                            subAction.appendCost(
                                    new ChooseAndDiscardCardsFromHandEffect(subAction, playerId, false, 2));
                            subAction.appendEffect(
                                    new PutPlayedEventIntoHandEffect(action));
                            action.insertEffect(
                                    new OptionalEffect(
                                            action, playerId,
                                            new StackActionEffect(subAction) {
                                                @Override
                                                public String getText(LotroGame game) {
                                                    return "Discard 2 cards from hand to return " + GameUtils.getCardLink(self) + " to hand";
                                                }
                                            }));
                        }
                    }
                });
        return action;
    }
}
