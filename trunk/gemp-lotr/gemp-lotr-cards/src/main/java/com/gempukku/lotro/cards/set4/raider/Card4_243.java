package com.gempukku.lotro.cards.set4.raider;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 6
 * Type: Event
 * Game Text: Archery: Exert a [RAIDER] archer to make the minion archery total +1 for each burden (limit +5).
 */
public class Card4_243 extends AbstractOldEvent {
    public Card4_243() {
        super(Side.SHADOW, Culture.RAIDER, "Rapid Fire", Phase.ARCHERY);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && PlayConditions.canExert(self, game, Filters.culture(Culture.RAIDER), Filters.keyword(Keyword.ARCHER));
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.culture(Culture.RAIDER), Filters.keyword(Keyword.ARCHER)));
        int total = Math.min(5, game.getGameState().getBurdens());
        action.appendEffect(
                new AddUntilEndOfPhaseModifierEffect(
                        new ArcheryTotalModifier(self, Side.SHADOW, total), Phase.ARCHERY));
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 6;
    }
}
