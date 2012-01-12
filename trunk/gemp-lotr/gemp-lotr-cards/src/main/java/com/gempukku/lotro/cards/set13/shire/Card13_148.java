package com.gempukku.lotro.cards.set13.shire;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.CharacterWonSkirmishResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 2
 * Type: Condition • Support Area
 * Game Text: Each time a Hobbit wins a skirmish, you may heal another Hobbit once (or heal another Hobbit twice if
 * the Hobbit that won the skirmish was Frodo or Bilbo).
 */
public class Card13_148 extends AbstractPermanent {
    public Card13_148() {
        super(Side.FREE_PEOPLE, 2, CardType.CONDITION, Culture.SHIRE, Zone.SUPPORT, "Fates Entwined");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, Race.HOBBIT)) {
            CharacterWonSkirmishResult winResult = (CharacterWonSkirmishResult) effectResult;
            int times = 1;
            final PhysicalCard winner = winResult.getWinner();
            if (Filters.or(Filters.frodo, Filters.name("Bilbo")).accepts(game.getGameState(), game.getModifiersQuerying(), winner))
                times = 2;

            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, playerId, 1, 1, times, Filters.not(winner), Race.HOBBIT));
            return Collections.singletonList(action);
        }
        return null;
    }
}
