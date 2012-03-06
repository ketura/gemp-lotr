package com.gempukku.lotro.cards.set18.gandalf;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;

/**
 * Set: Treachery & Deceit
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 3
 * Type: Event • Maneuver
 * Game Text: If the fellowship is in region 1 or region 2, exert 2 Ents to make an Ent defender +1 until the regroup
 * phase. If the fellowship is in region 3, spot a [GANDALF] companion to draw a card.
 */
public class Card18_020 extends AbstractEvent {
    public Card18_020() {
        super(Side.FREE_PEOPLE, 3, Culture.GANDALF, "Ents Marching", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                &&
                ((GameUtils.getRegion(game.getGameState()) == 3 && PlayConditions.canSpot(game, Culture.GANDALF, CardType.COMPANION))
                        || ((GameUtils.getRegion(game.getGameState()) == 1 || GameUtils.getRegion(game.getGameState()) == 2) && PlayConditions.canExert(self, game, 1, 2, Race.ENT)));
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        if (GameUtils.getRegion(game.getGameState()) == 3)
            action.appendEffect(
                    new DrawCardsEffect(action, playerId, 1));
        else {
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 2, 2, Race.ENT));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose an Ent", Race.ENT) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.appendEffect(
                                    new AddUntilStartOfPhaseModifierEffect(
                                            new KeywordModifier(self, card, Keyword.DEFENDER, 1), Phase.REGROUP));
                        }
                    });
        }
        return action;
    }
}
