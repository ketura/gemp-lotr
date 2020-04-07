package com.gempukku.lotro.cards.set10.shire;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ReplaceInSkirmishEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: Mount Doom
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Event • Skirmish
 * Game Text: If a Hobbit companion is not assigned to a skirmish, exert that Hobbit to have him or her replace
 * a companion (except the Ring-bearer) in a skirmish.
 */
public class Card10_105 extends AbstractEvent {
    public Card10_105() {
        super(Side.FREE_PEOPLE, 0, Culture.SHIRE, "Brave and Loyal", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Race.HOBBIT, CardType.COMPANION, Filters.notAssignedToSkirmish);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.HOBBIT, CardType.COMPANION, Filters.notAssignedToSkirmish) {
                    @Override
                    protected void forEachCardExertedCallback(PhysicalCard character) {
                        action.appendEffect(
                                new ReplaceInSkirmishEffect(character, CardType.COMPANION, Filters.not(Filters.ringBearer)));
                    }
                });
        return action;
    }
}
