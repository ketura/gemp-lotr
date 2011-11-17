package com.gempukku.lotro.cards.set2.sauron;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Mines of Moria
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 2
 * Type: Event
 * Game Text: Maneuver: Exert a [SAURON] Orc to discard an ally (or 2 [ELVEN] allies).
 */
public class Card2_088 extends AbstractOldEvent {
    public Card2_088() {
        super(Side.SHADOW, Culture.SAURON, "Memory of Many Things", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.SAURON), Filters.race(Race.ORC));
    }

    @Override
    public int getTwilightCost() {
        return 2;
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.culture(Culture.SAURON), Filters.race(Race.ORC)));
        List<Effect> possibleEffects = new LinkedList<Effect>();
        possibleEffects.add(
                new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.ALLY) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Discard an ally";
                    }
                });
        possibleEffects.add(
                new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 2, 2, CardType.ALLY, Filters.culture(Culture.ELVEN)) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Discard 2 ELVEN allies";
                    }
                });
        action.appendEffect(
                new ChoiceEffect(action, playerId, possibleEffects));
        return action;
    }
}
