package com.gempukku.lotro.cards.set8.dwarven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseOpponentEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: Siege of Gondor
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Event • Regroup
 * Game Text: Spot a Dwarf who is damage +X and exert that Dwarf twice to make an opponent discard X Shadow cards.
 */
public class Card8_003 extends AbstractEvent {
    public Card8_003() {
        super(Side.FREE_PEOPLE, 2, Culture.DWARVEN, "Blood Runs Chill", Phase.REGROUP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canExert(self, game, 2, Race.DWARF, Keyword.DAMAGE);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, final LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, 2, Race.DWARF, Keyword.DAMAGE) {
                    @Override
                    protected void forEachCardExertedCallback(PhysicalCard character) {
                        final int count = game.getModifiersQuerying().getKeywordCount(game.getGameState(), character, Keyword.DAMAGE);
                        action.appendEffect(
                                new ChooseOpponentEffect(playerId) {
                                    @Override
                                    protected void opponentChosen(String opponentId) {
                                        action.insertEffect(
                                                new ChooseAndDiscardCardsFromPlayEffect(action, opponentId, count, count, Side.SHADOW));
                                    }
                                });
                    }
                });
        return action;
    }
}
