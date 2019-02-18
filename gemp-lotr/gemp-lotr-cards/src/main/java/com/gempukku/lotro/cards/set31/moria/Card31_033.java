package com.gempukku.lotro.cards.set31.moria;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.RemovePlayedEventFromGameEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndRemoveFromTheGameCardsInDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Short Rest
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 1
 * Type: Event
 * Game Text: You may exert The Great Goblin twice to play this event from your discard pile. Exert 2 Orcs
 * to discard a [DWARVEN] possession or [DWARVEN] artifact borne by a companion.
 */
public class Card31_033 extends AbstractEvent {
    public Card31_033() {
        super(Side.SHADOW, 1, Culture.MORIA, "Goblin Song", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canExert(self, game, 1, 2, Race.ORC);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
	action.appendCost(
		new ChooseAndExertCharactersEffect(action, playerId, 2, 2, Race.ORC));
        action.appendEffect(
                new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Filters.and(Culture.DWARVEN, Filters.or(CardType.POSSESSION, CardType.ARTIFACT)), Filters.attachedTo(CardType.COMPANION)));
        return action;
    }

    @Override
    public List<? extends Action> getPhaseActionsFromDiscard(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.isPhase(game, Phase.MANEUVER)
                && PlayConditions.canExert(self, game, 2, Filters.name("The Great Goblin"))
		&& (PlayConditions.canExert(self, game, 1, 2, Filters.and(Race.ORC, Filters.not(Filters.name("The Great Goblin"))))
                    || (PlayConditions.canExert(self, game, 1, Filters.and(Race.ORC, Filters.not(Filters.name("The Great Goblin"))))
                        && PlayConditions.canExert(self, game, 3, Filters.name("The Great Goblin"))))) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, 2, Filters.name("The Great Goblin")));
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 2, 2, Race.ORC));
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Filters.and(Culture.DWARVEN, Filters.or(CardType.POSSESSION, CardType.ARTIFACT)), Filters.attachedTo(CardType.COMPANION)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
