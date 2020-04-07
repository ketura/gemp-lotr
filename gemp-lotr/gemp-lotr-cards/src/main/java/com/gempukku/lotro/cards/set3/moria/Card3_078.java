package com.gempukku.lotro.cards.set3.moria;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

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
        super(Side.SHADOW, 1, Culture.MORIA, "Hide and Seek", Phase.SKIRMISH);
        addKeyword(Keyword.SEARCH);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, 2, Culture.SHIRE, CardType.COMPANION);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, final PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose MORIA minion", Culture.MORIA, CardType.MINION) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        action.appendEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new KeywordModifier(self, Filters.sameCard(card), Keyword.DAMAGE)));
                    }
                });
        return action;
    }
}
