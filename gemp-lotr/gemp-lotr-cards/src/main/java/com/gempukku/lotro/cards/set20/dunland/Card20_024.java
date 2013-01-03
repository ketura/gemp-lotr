package com.gempukku.lotro.cards.set20.dunland;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * 1
 * Murderers!
 * Dunland	Event • Skirmish
 * Make a [Dunland] man strength +2 (and damage +1 if you control two sites).
 */
public class Card20_024 extends AbstractEvent {
    public Card20_024() {
        super(Side.SHADOW, 1, Culture.DUNLAND, "Murderers!", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose a DUNLAND Man", Culture.DUNLAND, Race.MAN) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        action.appendEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, card, 2), Phase.SKIRMISH));
                        if (PlayConditions.canSpot(game, 2, Filters.siteControlled(playerId)))
                            action.appendEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new KeywordModifier(self, card, Keyword.DAMAGE, 1), Phase.SKIRMISH));
                    }
                });
        return action;
    }
}
