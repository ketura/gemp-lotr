package com.gempukku.lotro.cards.set3.moria;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;

/**
 * Set: Realms of Elf-lords
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Search. Skirmish: Spot 2 [SHIRE] companions to make a [MORIA] minion damage +1.
 */
public class Card3_078 extends AbstractEvent {
    public Card3_078() {
        super(Side.SHADOW, Culture.MORIA, "Hide and Seek", Phase.SKIRMISH);
        addKeyword(Keyword.SEARCH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.countSpottable(game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.SHIRE), Filters.type(CardType.COMPANION)) >= 2;
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose MORIA minion", Filters.culture(Culture.MORIA), Filters.type(CardType.MINION)) {
                    @Override
                    protected void cardSelected(PhysicalCard card) {
                        action.appendEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new KeywordModifier(self, Filters.sameCard(card), Keyword.DAMAGE), Phase.SKIRMISH));
                    }
                });
        return action;
    }
}
