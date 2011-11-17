package com.gempukku.lotro.cards.set7.rohan;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.PreventCardEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 1
 * Type: Possession • Armor
 * Game Text: Bearer must be a [ROHAN] Man. Response: If you have initiative and bearer is about to take a wound in
 * a skirmish, discard 3 cards from hand to prevent that wound.
 */
public class Card7_245 extends AbstractAttachableFPPossession {
    public Card7_245() {
        super(1, 0, 0, Culture.ROHAN, PossessionClass.ARMOR, "Riding Armor");
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.ROHAN, Race.MAN);
    }

    @Override
    public List<? extends Action> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isGettingWounded(effect, game, Filters.hasAttached(self))
                && PlayConditions.hasInitiative(game, Side.FREE_PEOPLE)
                && game.getGameState().getCurrentPhase() == Phase.SKIRMISH
                && PlayConditions.canDiscardFromHand(game, playerId, 3, Filters.any)) {
            WoundCharactersEffect woundEffect = (WoundCharactersEffect) effect;

            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 3));
            action.appendEffect(
                    new PreventCardEffect(woundEffect, self.getAttachedTo()));
            return Collections.singletonList(action);
        }
        return null;
    }
}
