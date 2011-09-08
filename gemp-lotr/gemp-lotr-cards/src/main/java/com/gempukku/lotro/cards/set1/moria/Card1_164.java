package com.gempukku.lotro.cards.set1.moria;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.WoundCharacterEffect;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Archery: Spot a [MORIA] archer to wound an Elf.
 */
public class Card1_164 extends AbstractEvent {
    public Card1_164() {
        super(Side.SHADOW, Culture.MORIA, "Bitter Hatred", Phase.ARCHERY);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.addEffect(
                new ChooseActiveCardEffect(playerId, "Choose an Elf", Filters.keyword(Keyword.ELF)) {
                    @Override
                    protected void cardSelected(PhysicalCard elf) {
                        action.addEffect(new WoundCharacterEffect(elf));
                    }
                });
        return action;
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return PlayConditions.canPayForShadowCard(game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.MORIA), Filters.keyword(Keyword.ARCHER));
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }
}
