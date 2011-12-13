package com.gempukku.lotro.cards.set12.shire;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.actions.SubCostToEffectAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.OptionalEffect;
import com.gempukku.lotro.cards.effects.PutPlayedEventIntoHandEffect;
import com.gempukku.lotro.cards.modifiers.OverwhelmedByMultiplierModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.StackActionEffect;
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
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose a Hobbit", Race.HOBBIT) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        action.insertEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new OverwhelmedByMultiplierModifier(self, card, 3), Phase.SKIRMISH));
                    }
                });
        action.appendEffect(
                new UnrespondableEffect() {
                    @Override
                    protected void doPlayEffect(LotroGame game) {
                        if (PlayConditions.location(game, Keyword.DWELLING)
                                && PlayConditions.canDiscardFromHand(game, playerId, 2, Filters.any)) {
                            SubCostToEffectAction subAction = new SubCostToEffectAction(action);
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
