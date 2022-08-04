package com.gempukku.lotro.cards.set7.rohan;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 2
 * Type: Event • Maneuver
 * Game Text: Spot a mounted [ROHAN] Man to exert a minion. That minion's owner may exert a companion and if he or she
 * does so, you may exert a minion; repeat this until a player does not exert a character.
 */
public class Card7_223 extends AbstractEvent {
    public Card7_223() {
        super(Side.FREE_PEOPLE, 2, Culture.ROHAN, "Death They Cried", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Culture.ROHAN, Race.MAN, Filters.mounted);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ExertMinionContinuouslyEffect(action, playerId));
        return action;
    }

    private static class ExertMinionContinuouslyEffect extends ChooseAndExertCharactersEffect {
        private final CostToEffectAction _action;
        private final String _fpPlayerId;

        private ExertMinionContinuouslyEffect(CostToEffectAction action, String playerId) {
            super(action, playerId, 0, 1, CardType.MINION);
            _action = action;
            _fpPlayerId = playerId;
        }

        @Override
        protected void forEachCardExertedCallback(PhysicalCard character) {
            _action.appendEffect(
                    new ChooseAndExertCharactersEffect(_action, character.getOwner(), 0, 1, CardType.COMPANION) {
                        @Override
                        protected void forEachCardExertedCallback(PhysicalCard character) {
                            _action.appendEffect(new ExertMinionContinuouslyEffect(_action, _fpPlayerId));
                        }
                    });
        }
    }
}
