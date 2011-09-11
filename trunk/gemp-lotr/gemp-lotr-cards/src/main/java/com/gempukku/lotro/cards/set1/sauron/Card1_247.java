package com.gempukku.lotro.cards.set1.sauron;

import com.gempukku.lotro.cards.AbstractResponseEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.SkirmishResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Response: If a [SAURON] Orc wins a skirmish, make that Orc fierce until the regroup phase.
 */
public class Card1_247 extends AbstractResponseEvent {
    public Card1_247() {
        super(Side.SHADOW, Culture.SAURON, "Enheartened Foe");
    }

    @Override
    public List<PlayEventAction> getOptionalAfterActions(String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (PlayConditions.winsSkirmish(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.and(Filters.culture(Culture.SAURON), Filters.race(Race.ORC)))
                && PlayConditions.canPayForShadowCard(game, self, 0)) {
            SkirmishResult skirmishResult = (SkirmishResult) effectResult;

            final PlayEventAction action = new PlayEventAction(self);
            action.addEffect(
                    new ChooseActiveCardEffect(playerId, "Choose an Orc", Filters.culture(Culture.SAURON), Filters.race(Race.ORC), Filters.in(skirmishResult.getWinners())) {
                        @Override
                        protected void cardSelected(PhysicalCard winningSauronOrc) {
                            action.addEffect(
                                    new AddUntilStartOfPhaseModifierEffect(
                                            new KeywordModifier(self, Filters.sameCard(winningSauronOrc), Keyword.FIERCE), Phase.REGROUP));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }
}
