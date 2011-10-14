package com.gempukku.lotro.cards.set2.gondor;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;

/**
 * Set: Mines of Moria
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Skirmish: Exert Aragorn to make another companion strength +2 (or +3 if that companion has the Aragorn
 * signet).
 */
public class Card2_036 extends AbstractEvent {
    public Card2_036() {
        super(Side.FREE_PEOPLE, Culture.GONDOR, "No Mere Ranger", Phase.SKIRMISH);
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), Filters.name("Aragorn"));
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.name("Aragorn")));
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose another companion", Filters.type(CardType.COMPANION), Filters.not(Filters.name("Aragorn"))) {
                    @Override
                    protected void cardSelected(PhysicalCard card) {
                        int bonus = (card.getBlueprint().getSignet() == Signet.ARAGORN) ? 3 : 2;
                        action.insertEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, Filters.sameCard(card), bonus), Phase.SKIRMISH));
                    }
                });
        return action;
    }
}
