package com.gempukku.lotro.cards.set17.elven;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

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
        super(Side.FREE_PEOPLE, 5, CardType.CONDITION, Culture.ELVEN, "The World Ahead");
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Race.ELF);
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
