package com.gempukku.lotro.cards.set2.shire;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mines of Moria
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: Stealth. To play, exert 2 Hobbits. Plays to your support area. The twilight cost of each search card and
 * each tracker is +2.
 */
public class Card2_100 extends AbstractPermanent {
    public Card2_100() {
        super(Side.FREE_PEOPLE, 0, CardType.CONDITION, Culture.SHIRE, Zone.SUPPORT, "Fearing the Worst");
        addKeyword(Keyword.STEALTH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && PlayConditions.canExertMultiple(self, game.getGameState(), game.getModifiersQuerying(), 1, 2, Filters.race(Race.HOBBIT));
    }

    @Override
    public PlayPermanentAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayPermanentAction action = super.getPlayCardAction(playerId, game, self, twilightModifier);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 2, 2, Filters.race(Race.HOBBIT)));
        return action;
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new TwilightCostModifier(self, Filters.or(Filters.keyword(Keyword.SEARCH), Filters.keyword(Keyword.TRACKER)), 2));
    }
}
