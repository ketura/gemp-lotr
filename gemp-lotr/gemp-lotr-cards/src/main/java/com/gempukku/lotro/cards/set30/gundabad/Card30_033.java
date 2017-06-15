package com.gempukku.lotro.cards.set30.gundabad;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Main Deck
 * Side: Shadow
 * Culture: Gundabad
 * Twilight Cost: 2
 * Type: Minion • Orc
 * Strength: 7
 * Vitality: 2
 * Site: 3
 * Game Text: Damage +1. To play, exert an Orc. Shadow: Discard Fimbul from play to play a minion from your draw deck.
 */
public class Card30_033 extends AbstractMinion {
    public Card30_033() {
        super(3, 7, 2, 3, Race.ORC, Culture.GUNDABAD, "Fimbul", "Orkish Assassin", true);
		addKeyword(Keyword.DAMAGE, 1);
		addKeyword(Keyword.WARG_RIDER);
    }

	@Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canExert(self, game, Race.ORC);
	}
	
	@Override
    public PlayPermanentAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayPermanentAction action = super.getPlayCardAction(playerId, game, self, twilightModifier, ignoreRoamingPenalty);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.ORC));
        return action;
	}
	
    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)) {
            List<ActivateCardAction> actions = new LinkedList<ActivateCardAction>();
            if (PlayConditions.canSelfDiscard(self, game)) {
                ActivateCardAction action = new ActivateCardAction(self);
                action.setText("Play a minion from your draw deck");
                action.appendCost(
                        new SelfDiscardEffect(self));
                action.appendEffect(
                        new ChooseAndPlayCardFromDeckEffect(playerId, CardType.MINION));
                actions.add(action);
            }
            return actions;
        }
        return null;
    }
}