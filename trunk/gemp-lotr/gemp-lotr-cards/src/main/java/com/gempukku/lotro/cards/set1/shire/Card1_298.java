package com.gempukku.lotro.cards.set1.shire;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.CancelSkirmishEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.Action;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Stealth. Skirmish: At sites 1 to 5, cancel a skirmish involving a Hobbit. At any other site, make a
 * Hobbit strength +2.
 */
public class Card1_298 extends AbstractEvent {
    public Card1_298() {
        super(Side.FREE_PEOPLE, Culture.SHIRE, "Hobbit Stealth", Phase.SKIRMISH);
        addKeyword(Keyword.STEALTH);
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self) {
        PhysicalCard fpCharacter = game.getGameState().getSkirmish().getFellowshipCharacter();
        return game.getGameState().getCurrentSiteNumber() > 5 || (fpCharacter != null && game.getModifiersQuerying().hasKeyword(game.getGameState(), fpCharacter, Keyword.HOBBIT));
    }

    @Override
    public Action getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        if (game.getGameState().getCurrentSiteNumber() > 5) {
            action.addEffect(
                    new ChooseActiveCardEffect(playerId, "Choose a Hobbit", Filters.keyword(Keyword.HOBBIT)) {
                        @Override
                        protected void cardSelected(PhysicalCard hobbit) {
                            action.addEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new StrengthModifier(self, Filters.sameCard(hobbit), 2), Phase.SKIRMISH));
                        }
                    });
        } else {
            action.addEffect(
                    new CancelSkirmishEffect());
        }
        return action;
    }
}
