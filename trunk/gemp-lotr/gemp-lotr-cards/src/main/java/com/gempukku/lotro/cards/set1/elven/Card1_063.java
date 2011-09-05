package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardFromPlayEffect;
import com.gempukku.lotro.logic.timing.Action;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Maneuver: Exert an Elf to discard a [SAURON] minion, a [SAURON] condition, or a [SAURON] possession.
 */
public class Card1_063 extends AbstractEvent {
    public Card1_063() {
        super(Side.FREE_PEOPLE, CardType.EVENT, Culture.ELVEN, "Stand Against Darkness", Phase.MANEUVER);
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.ELF), Filters.canExert());
    }

    @Override
    public Action getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, Keyword.MANEUVER, "Exert and Elf to discard a SAURON minion, a SAURON condition, or a SAURON possession");
        action.addCost(
                new ChooseActiveCardEffect(playerId, "Choose an Elf", Filters.keyword(Keyword.ELF), Filters.canExert()) {
                    @Override
                    protected void cardSelected(PhysicalCard elf) {
                        action.addCost(new ExertCharacterEffect(elf));
                    }
                });
        action.addEffect(
                new ChooseActiveCardEffect(playerId, "Choose SAURON minion, condition or possession", Filters.culture(Culture.SAURON),
                        Filters.or(Filters.type(CardType.MINION), Filters.type(CardType.CONDITION), Filters.type(CardType.POSSESSION))) {
                    @Override
                    protected void cardSelected(PhysicalCard sauronCard) {
                        action.addEffect(new DiscardCardFromPlayEffect(self, sauronCard));
                    }
                }
        );
        return action;
    }
}
