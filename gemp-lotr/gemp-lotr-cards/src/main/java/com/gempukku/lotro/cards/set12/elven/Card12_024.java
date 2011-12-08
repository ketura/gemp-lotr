package com.gempukku.lotro.cards.set12.elven;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndReturnCardsToHandEffect;
import com.gempukku.lotro.cards.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Condition • Support Area
 * Game Text: Toil 1. (For each [ELVEN] character you exert when playing this, its twilight cost is -1)
 * Archery: Discard an [ELVEN] character from hand to make the fellowship archery total +1. Each time the fellowship
 * moves from an underground site, return your Elf to your hand.
 */
public class Card12_024 extends AbstractPermanent {
    public Card12_024() {
        super(Side.FREE_PEOPLE, 2, CardType.CONDITION, Culture.ELVEN, Zone.SUPPORT, "Taking the High Ground");
        addKeyword(Keyword.TOIL, 1);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.ARCHERY, self)
                && PlayConditions.canDiscardFromHand(game, playerId, 1, Culture.ELVEN, Filters.character)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 1, Culture.ELVEN, Filters.character));
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new ArcheryTotalModifier(self, Side.FREE_PEOPLE, 1), Phase.ARCHERY));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.movesFrom(game, effectResult, Keyword.UNDERGROUND)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new ChooseAndReturnCardsToHandEffect(action, self.getOwner(), 1, 1, Filters.owner(self.getOwner()), Race.ELF));
            return Collections.singletonList(action);
        }
        return null;
    }
}
