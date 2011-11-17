package com.gempukku.lotro.cards.set10.raider;

import com.gempukku.lotro.cards.AbstractResponseEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.PlayCardResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mount Doom
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 0
 * Type: Event • Response
 * Game Text: If a Free Peoples possession is played, spot a [RAIDER] card to discard that possession. If that
 * possession was a [GONDOR] or [ROHAN] possession, wound an unbound Man.
 */
public class Card10_051 extends AbstractResponseEvent {
    public Card10_051() {
        super(Side.SHADOW, 0, Culture.RAIDER, "Stampeded");
    }

    @Override
    public List<PlayEventAction> getOptionalAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.played(game, effectResult, CardType.POSSESSION, Side.FREE_PEOPLE)
                && PlayConditions.canSpot(game, Culture.RAIDER)
                && checkPlayRequirements(playerId, game, self, 0, false)) {
            PlayCardResult playResult = (PlayCardResult) effectResult;
            final PhysicalCard playedCard = playResult.getPlayedCard();
            PlayEventAction action = new PlayEventAction(self);
            action.appendEffect(
                    new DiscardCardsFromPlayEffect(self, playedCard));
            final Culture possessionCulture = playedCard.getBlueprint().getCulture();
            if (possessionCulture == Culture.GONDOR || possessionCulture == Culture.ROHAN)
                action.appendEffect(
                        new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, Filters.unboundCompanion, Race.MAN));
            return Collections.singletonList(action);
        }
        return null;
    }
}
