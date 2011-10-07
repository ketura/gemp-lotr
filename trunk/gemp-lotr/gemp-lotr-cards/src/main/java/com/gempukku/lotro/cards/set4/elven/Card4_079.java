package com.gempukku.lotro.cards.set4.elven;

import com.gempukku.lotro.cards.AbstractResponseEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.LiberateASiteEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.SkirmishResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Response: If an Elf wins a skirmish, exert that Elf to liberate a site or wound a minion.
 */
public class Card4_079 extends AbstractResponseEvent {
    public Card4_079() {
        super(Side.FREE_PEOPLE, Culture.ELVEN, "Night Without End");
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public List<PlayEventAction> getOptionalAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), (Phase) null, self)
                && PlayConditions.winsSkirmish(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.race(Race.ELF))) {
            final List<PhysicalCard> winners = ((SkirmishResult) effectResult).getWinners();
            if (PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), Filters.in(winners))) {
                PlayEventAction action = new PlayEventAction(self);
                action.appendCost(
                        new ExertCharactersEffect(self, winners.get(0)));

                List<Effect> possibleEffects = new LinkedList<Effect>();
                possibleEffects.add(
                        new LiberateASiteEffect(self));
                possibleEffects.add(
                        new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, Filters.type(CardType.MINION)));

                action.appendEffect(
                        new ChoiceEffect(action, playerId, possibleEffects));
                return Collections.singletonList(action);
            }
        }

        return null;
    }
}
