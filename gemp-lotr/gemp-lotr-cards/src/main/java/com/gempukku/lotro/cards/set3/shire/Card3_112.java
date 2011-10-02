package com.gempukku.lotro.cards.set3.shire;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.RemoveKeywordModifier;

/**
 * Set: Realms of Elf-lords
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Stealth. Skirmish: Spot 2 [SHIRE] companions to make a minion lose all damage bonuses.
 */
public class Card3_112 extends AbstractEvent {
    public Card3_112() {
        super(Side.FREE_PEOPLE, Culture.SHIRE, "Seek and Hide", Phase.SKIRMISH);
        addKeyword(Keyword.STEALTH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.SHIRE), Filters.type(CardType.COMPANION));
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(playerId, "Choose a minion", Filters.type(CardType.MINION)) {
                    @Override
                    protected void cardSelected(PhysicalCard card) {
                        action.appendEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new RemoveKeywordModifier(self, Filters.sameCard(card), Keyword.DAMAGE), Phase.SKIRMISH));
                    }
                });
        return action;
    }
}
