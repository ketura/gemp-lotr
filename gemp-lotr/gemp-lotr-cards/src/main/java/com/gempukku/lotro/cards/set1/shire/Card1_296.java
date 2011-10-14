package com.gempukku.lotro.cards.set1.shire;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.CancelSkirmishEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.Skirmish;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Stealth. Skirmish: At sites 1 to 4, cancel a skirmish involving a Hobbit. At any other site, make a
 * Hobbit strength +3.
 */
public class Card1_296 extends AbstractEvent {
    public Card1_296() {
        super(Side.FREE_PEOPLE, Culture.SHIRE, "Hobbit Intuition", Phase.SKIRMISH);
        addKeyword(Keyword.STEALTH);
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        if (game.getGameState().getCurrentSiteNumber() > 4 || game.getGameState().getCurrentSiteBlock() != Block.FELLOWSHIP) {
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a Hobbit", Filters.race(Race.HOBBIT)) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard hobbit) {
                            action.appendEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new StrengthModifier(self, Filters.sameCard(hobbit), 3), Phase.SKIRMISH));
                        }
                    });
        } else {
            final Skirmish skirmish = game.getGameState().getSkirmish();
            if (skirmish != null && skirmish.getFellowshipCharacter() != null && skirmish.getFellowshipCharacter().getBlueprint().getRace() == Race.HOBBIT)
                action.appendEffect(
                        new CancelSkirmishEffect());
        }
        return action;
    }
}
