package com.gempukku.lotro.cards.set1.gondor;

import com.gempukku.lotro.cards.AbstractResponseOldEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.CancelEventEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.Skirmish;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.PlayEventResult;

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
public class Card1_115 extends AbstractResponseOldEvent {
    public Card1_115() {
        super(Side.FREE_PEOPLE, Culture.GONDOR, "Strength of Kings");
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public List<PlayEventAction> getOptionalAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.played(game, effectResult, CardType.EVENT, Keyword.SKIRMISH)
                && checkPlayRequirements(playerId, game, self, 0, false)) {
            Skirmish skirmish = game.getGameState().getSkirmish();
            if (skirmish != null) {
                PhysicalCard fpCharacter = skirmish.getFellowshipCharacter();
                if (fpCharacter.getBlueprint().getRace() == Race.MAN
                        && fpCharacter.getBlueprint().getCulture() == Culture.GONDOR) {
                    PlayEventAction action = new PlayEventAction(self);
                    action.appendEffect(new CancelEventEffect(self, (PlayEventResult) effectResult));
                    return Collections.singletonList(action);
                }
            }
        }
        return null;
    }
}
