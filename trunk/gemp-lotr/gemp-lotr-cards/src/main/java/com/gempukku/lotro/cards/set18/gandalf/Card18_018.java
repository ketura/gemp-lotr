package com.gempukku.lotro.cards.set18.gandalf;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPutCardFromDiscardIntoHandEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.PossessionClass;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Treachery & Deceit
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 2
 * Type: Possession • Hand Weapon
 * Strength: +2
 * Vitality: +1
 * Game Text: Bearer must be a [GANDALF] Man. While Grimbeorn is bearing this possession, each time he wins a skirmish,
 * you may take a [GANDALF] card from your discard pile into hand.
 */
public class Card18_018 extends AbstractAttachableFPPossession {
    public Card18_018() {
        super(2, 2, 1, Culture.GANDALF, PossessionClass.HAND_WEAPON, "Beorning Axe");
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.GANDALF, Race.MAN);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, Filters.hasAttached(self), Filters.name("Grimbeorn"))) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndPutCardFromDiscardIntoHandEffect(action, playerId, 1, 1, Culture.GANDALF));
            return Collections.singletonList(action);
        }
        return null;
    }
}
