package com.gempukku.lotro.cards.set21.site;

import com.gempukku.lotro.cards.AbstractNewSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Main Deck
 * Twilight Cost: 3
 * Type: Site
 * Site: 6
 * Game Text: River. Sanctuary. At the start of the regroup phase,
 * you may play a [LAKETOWN] or [DWARF] ally from your draw deck.
 */
public class Card21_54 extends AbstractSite {
    public Card21_54() {
        super("Esgaroth", Block.HOBBIT, 6, 3, Direction.RIGHT);
        addKeyword(Keyword.RIVER);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.REGROUP)
                && playerId.equals(game.getGameState().getCurrentPlayerId())
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndPlayCardFromDeckEffect(playerId, game, Filters.or(Culture.LAKETOWN, Culture.DWARF), CardType.ALLY));
            return Collections.singletonList(action);
        }
        return null;
    }
}