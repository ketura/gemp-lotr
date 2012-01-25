package com.gempukku.lotro.cards.set15.gollum;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.ActivateCardResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Free
 * Culture: Gollum
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Bearer must be Smeagol. While you can spot 2 Ring-bound Hobbits, each time an opponent uses a skirmish
 * special ability in a skirmish involving Gollum or Smeagol, you may wound a minion.
 */
public class Card15_039 extends AbstractAttachable {
    public Card15_039() {
        super(Side.FREE_PEOPLE, CardType.CONDITION, 1, Culture.GOLLUM, null, "Called to Mordor");
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.smeagol;
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.activated(game, effectResult, Filters.not(Filters.owner(playerId)))
                && ((ActivateCardResult) effectResult).getActionTimeword() == Phase.SKIRMISH
                && PlayConditions.canSpot(game, Filters.inSkirmish, Filters.gollumOrSmeagol)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, CardType.MINION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
