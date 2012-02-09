package com.gempukku.lotro.cards.set17.elven;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 5
 * Type: Condition • Support Area
 * Game Text: To play, spot an Elf. Each time the fellowship moves to a region one site, add a threat. Each time
 * the fellowship moves to a region two site, heal an Elf. Each time the fellowship moves to a region three site,
 * draw a card.
 */
public class Card17_013 extends AbstractPermanent {
    public Card17_013() {
        super(Side.FREE_PEOPLE, 5, CardType.CONDITION, Culture.ELVEN, Zone.SUPPORT, "The World Ahead");
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Race.ELF);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.movesTo(game, effectResult, Filters.region(1))) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new AddThreatsEffect(self.getOwner(), self, 1));
            return Collections.singletonList(action);
        }
        if (TriggerConditions.movesTo(game, effectResult, Filters.region(2))) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, self.getOwner(), Race.ELF));
            return Collections.singletonList(action);
        }
        if (TriggerConditions.movesTo(game, effectResult, Filters.region(3))) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new DrawCardsEffect(action, self.getOwner(), 1));
            return Collections.singletonList(action);
        }
        return null;
    }
}
