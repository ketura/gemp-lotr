package com.gempukku.lotro.cards.set4.isengard;

import com.gempukku.lotro.cards.AbstractResponseOldEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.KillResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Response: If a companion or ally is killed, exert an [ISENGARD] minion to add a burden (or 2 burdens
 * if Aragorn, Gandalf, or Theoden is killed).
 */
public class Card4_141 extends AbstractResponseOldEvent {
    public Card4_141() {
        super(Side.SHADOW, Culture.ISENGARD, "Beyond Dark Mountains");
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public List<PlayEventAction> getOptionalAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.KILL
                && PlayConditions.canPlayCardDuringPhase(game, (Phase) null, self)) {
            KillResult killResult = (KillResult) effectResult;
            Collection<PhysicalCard> killedChars = Filters.filter(killResult.getKilledCards(), game.getGameState(), game.getModifiersQuerying(), Filters.or(Filters.type(CardType.ALLY), Filters.type(CardType.COMPANION)));
            if (killedChars.size() > 0) {
                PlayEventAction action = new PlayEventAction(self);

                boolean hasSpecific = Filters.filter(killedChars, game.getGameState(), game.getModifiersQuerying(), Filters.or(Filters.aragorn, Filters.gandalf, Filters.name("Theoden"))).size() > 0;
                action.appendEffect(
                        new AddBurdenEffect(self, hasSpecific ? 2 : 1));
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
