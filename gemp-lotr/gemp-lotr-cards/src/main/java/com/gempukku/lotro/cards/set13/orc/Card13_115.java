package com.gempukku.lotro.cards.set13.orc;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 5
 * Type: Minion • Orc
 * Strength: 12
 * Vitality: 2
 * Site: 4
 * Game Text: To play, spot an [ORC] card in your support area. When you play this, spot a Free Peoples condition
 * to discard from play each other card that has the same card title as that condition.
 */
public class Card13_115 extends AbstractMinion {
    public Card13_115() {
        super(5, 12, 2, 4, Race.ORC, Culture.ORC, "Orc Raid Commander");
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Culture.ORC, Zone.SUPPORT);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)) {
            final RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendCost(
                    new ChooseActiveCardEffect(self, self.getOwner(), "Choose a Free Peoples condition", Side.FREE_PEOPLE, CardType.CONDITION) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.appendEffect(
                                    new DiscardCardsFromPlayEffect(self.getOwner(), self, Filters.not(card), Filters.name(card.getBlueprint().getTitle())));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
