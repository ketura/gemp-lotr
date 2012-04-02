package com.gempukku.lotro.cards.set4.isengard;

import com.gempukku.lotro.cards.AbstractResponseOldEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Names;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.ForEachKilledResult;

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
        if (TriggerConditions.forEachKilled(game, effectResult, Filters.or(CardType.COMPANION, CardType.ALLY))
                && PlayConditions.canExert(self, game, Culture.ISENGARD, CardType.MINION)
                && checkPlayRequirements(playerId, game, self, 0, 0, false, false)) {
            ForEachKilledResult killResult = (ForEachKilledResult) effectResult;
            PlayEventAction action = new PlayEventAction(self);
            action.setText(GameUtils.getCardLink(killResult.getKilledCard()) + " was killed");
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Culture.ISENGARD, CardType.MINION));
            boolean hasSpecific = Filters.or(Filters.aragorn, Filters.gandalf, Filters.name(Names.theoden)).accepts(game.getGameState(), game.getModifiersQuerying(), killResult.getKilledCard());
            action.appendEffect(
                    new AddBurdenEffect(self, hasSpecific ? 2 : 1));
            return Collections.singletonList(action);
        }
        return null;
    }
}
