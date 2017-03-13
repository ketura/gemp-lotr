package com.gempukku.lotro.cards.set22.moria;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: The Short Rest
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 1
 * Type: Event
 * Game Text: You may exert The Great Goblin twice to play this event from your discard pile. Exert 2 Orcs
 * to discard a possession or artifact borne by a [DWARVEN] companion.
 */
public class Card22_34 extends AbstractEvent {
    public Card22_34() {
        super(Side.SHADOW, 1, Culture.MORIA, "Goblin Song", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canExert(game, 1, 2, Race.ORC);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
		action.appendCost(
				new ChooseAndExertCharactersEffect(action, playerId, 2, 2, Race.ORC));
        action.appendEffect(
                new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Filters.or(CardType.POSSESSION, CardType.ARTIFACT), Filters.attachedTo(Filters.and(CardType.COMPANION, Race.DWARF))));
        return action;
    }

    @Override
    public List<? extends Action> getPhaseActionsFromDiscard(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.isPhase(game, Phase.SKIRMISH)
                && PlayConditions.canExert(self, game, 2, Filters.name("The Great Goblin"))
				&& PlayConditions.canPlayFromDiscard(playerId, game, self)) {
            final PlayEventAction playCardAction = getPlayCardAction(playerId, game, self, 0, false);
			playCardAction.appendCost(
					new ChooseAndExertCharactersEffect(action, playerId, 1, 1, 2, Filters.name("The Great Goblin")));
            return Collections.singletonList(playCardAction);
        }
        return null;
    }
}