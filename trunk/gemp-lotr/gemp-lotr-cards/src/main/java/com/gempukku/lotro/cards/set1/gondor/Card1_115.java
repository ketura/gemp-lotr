package com.gempukku.lotro.cards.set1.gondor;

import com.gempukku.lotro.cards.AbstractResponseEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.CancelEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.Skirmish;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Response: If a Skirmish event is played during a skirmish involving a [GONDOR] Man, cancel that event.
 */
public class Card1_115 extends AbstractResponseEvent {
    public Card1_115() {
        super(Side.FREE_PEOPLE, Culture.GONDOR, "Strength of Kings");
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public List<PlayEventAction> getOptionalBeforeActions(String playerId, LotroGame game, Effect effect, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.played(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.and(Filters.type(CardType.EVENT), Filters.keyword(Keyword.SKIRMISH)))) {
            Skirmish skirmish = game.getGameState().getSkirmish();
            if (skirmish != null) {
                PhysicalCard fpCharacter = skirmish.getFellowshipCharacter();
                if (game.getModifiersQuerying().hasKeyword(game.getGameState(), fpCharacter, Keyword.MAN)
                        && fpCharacter.getBlueprint().getCulture() == Culture.GONDOR) {
                    PlayEventAction action = new PlayEventAction(self);
                    action.addEffect(new CancelEffect(effect));
                    return Collections.singletonList(action);
                }
            }
        }
        return null;
    }
}
