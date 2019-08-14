package com.gempukku.lotro.cards.set20.rohan;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collection;

/**
 * 1
 * For Death and Glory
 * Rohan	Event • Manuever
 * Exert a mounted [Rohan] Man to make him defender +1 until the regroup phase.
 */
public class Card20_324 extends AbstractEvent {
    public Card20_324() {
        super(Side.FREE_PEOPLE, 1, Culture.ROHAN, "For Death and Glory", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Culture.ROHAN, Race.MAN, Filters.mounted);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action= new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Culture.ROHAN, Race.MAN, Filters.mounted) {
                    @Override
                    protected void cardsToBeExertedCallback(Collection<PhysicalCard> characters) {
                        action.appendEffect(
                                new AddUntilStartOfPhaseModifierEffect(
                                        new KeywordModifier(self, Filters.in(characters), Keyword.DEFENDER, 1), Phase.REGROUP));
                    }
                });
        return action;
    }
}
