package com.gempukku.lotro.cards.set7.wraith;

import com.gempukku.lotro.cards.AbstractResponseEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 2
 * Type: Event • Response
 * Game Text: If a skirmish that involved a Nazgul is about to end, discard a possession borne by a companion
 * in that skirmish.
 */
public class Card7_182 extends AbstractResponseEvent {
    public Card7_182() {
        super(Side.SHADOW, 2, Culture.WRAITH, "Loathsome");
    }

    @Override
    public List<PlayEventAction> getOptionalAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.SKIRMISH_ABOUT_TO_END
                && checkPlayRequirements(playerId, game, self, 0)
                && Filters.filter(game.getGameState().getSkirmish().getShadowCharacters(), game.getGameState(), game.getModifiersQuerying(), Race.NAZGUL).size() > 0) {
            PlayEventAction action = new PlayEventAction(self);
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.POSSESSION, Filters.attachedTo(CardType.COMPANION, Filters.inSkirmish)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
