package com.gempukku.lotro.cards.set32.dwarven;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Clouds Burst
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 3
 * Type: Event • Regroup
 * Game Text: Exert a Dwarf companion to play Thráin, a Dwarven possession or Dwarven artifact from your draw
 * deck or discard pile.
 */
public class Card32_010 extends AbstractEvent {
    public Card32_010() {
        super(Side.FREE_PEOPLE, 3, Culture.DWARVEN, "Thror's Heirlooms", Phase.REGROUP);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Culture.DWARVEN, CardType.COMPANION);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        List<Effect> possibleEffects = new LinkedList<Effect>();
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Culture.DWARVEN, CardType.COMPANION));
        possibleEffects.add(
                new ChooseAndPlayCardFromDeckEffect(playerId, Filters.or(Filters.name("Thrain"), Filters.and(Culture.DWARVEN, Filters.or(CardType.POSSESSION, CardType.ARTIFACT)))));
        if (PlayConditions.canPlayFromDiscard(playerId, game, Filters.or(Filters.name("Thrain"), Filters.and(Culture.DWARVEN, Filters.or(CardType.POSSESSION, CardType.ARTIFACT)))))
            possibleEffects.add(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, Filters.or(Filters.name("Thrain"), Filters.and(Culture.DWARVEN, Filters.or(CardType.POSSESSION, CardType.ARTIFACT)))));
        action.appendEffect(
                new ChoiceEffect(action, playerId, possibleEffects));
        return action;
    }
}
