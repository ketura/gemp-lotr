package com.gempukku.lotro.cards.set4.elven;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.LiberateASiteEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Event
 * Game Text: Skirmish or Regroup: Spot 3 Elf companions to liberate a site or exert a minion.
 */
public class Card4_084 extends AbstractOldEvent {
    public Card4_084() {
        super(Side.FREE_PEOPLE, Culture.ELVEN, "Sword-wall", Phase.SKIRMISH, Phase.REGROUP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && Filters.countSpottable(game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.ELF)) >= 3;
    }

    @Override
    public int getTwilightCost() {
        return 2;
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);

        List<Effect> possibleEffects = new LinkedList<Effect>();
        possibleEffects.add(
                new LiberateASiteEffect(self) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Liberate a site";
                    }
                });
        possibleEffects.add(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.type(CardType.MINION)) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Exert a minion";
                    }
                });

        action.appendEffect(
                new ChoiceEffect(action, playerId, possibleEffects));
        return action;
    }
}
