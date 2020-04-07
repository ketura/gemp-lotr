package com.gempukku.lotro.cards.set4.isengard;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Names;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.PlayUtils;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractResponseEvent;
import com.gempukku.lotro.logic.effects.AddBurdenEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
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
public class Card4_141 extends AbstractResponseEvent {
    public Card4_141() {
        super(Side.SHADOW, 0, Culture.ISENGARD, "Beyond Dark Mountains");
    }

    @Override
    public List<PlayEventAction> getPlayResponseEventAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.forEachKilled(game, effectResult, Filters.or(CardType.COMPANION, CardType.ALLY))
                && PlayConditions.canExert(self, game, Culture.ISENGARD, CardType.MINION)
                && PlayUtils.checkPlayRequirements(game, self, Filters.any, 0, 0, false, false)) {
            ForEachKilledResult killResult = (ForEachKilledResult) effectResult;
            PlayEventAction action = new PlayEventAction(self);
            action.setText(GameUtils.getFullName(killResult.getKilledCard()) + " was killed");
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Culture.ISENGARD, CardType.MINION));
            boolean hasSpecific = Filters.or(Filters.aragorn, Filters.gandalf, Filters.name(Names.theoden)).accepts(game, killResult.getKilledCard());
            action.appendEffect(
                    new AddBurdenEffect(self.getOwner(), self, hasSpecific ? 2 : 1));
            return Collections.singletonList(action);
        }
        return null;
    }
}
