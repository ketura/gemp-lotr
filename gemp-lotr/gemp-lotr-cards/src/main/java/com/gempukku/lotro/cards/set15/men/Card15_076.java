package com.gempukku.lotro.cards.set15.men;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.RevealCardEffect;
import com.gempukku.lotro.cards.modifiers.RemoveGameTextModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 3
 * Type: Condition • Support Area
 * Game Text: When you play this, spot 2 [MEN] Men to randomly reveal a site from the Free Peoples player’s
 * adventure deck. Note its cardtitle. Each site on the adventure path with the same card title as the selected site
 * loses its gametext.
 */
public class Card15_076 extends AbstractPermanent {
    public Card15_076() {
        super(Side.SHADOW, 3, CardType.CONDITION, Culture.MEN, Zone.SUPPORT, "Destroyed Homestead");
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new RemoveGameTextModifier(self, Filters.and(CardType.SITE, Zone.ADVENTURE_PATH, Filters.name((String) self.getWhileInZoneData())));
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)
                && PlayConditions.canSpot(game, 2, Culture.MEN, Race.MAN)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            final List<PhysicalCard> randomCards = GameUtils.getRandomCards(game.getGameState().getAdventureDeck(game.getGameState().getCurrentPlayerId()), 1);
            if (randomCards.size() == 1) {
                PhysicalCard randomSite = randomCards.get(0);
                action.appendEffect(
                        new RevealCardEffect(self, randomCards.get(0)));
                self.setWhileInZoneData(randomSite.getBlueprint().getName());
            }
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public String getDisplayableInformation(PhysicalCard self) {
        if (self.getWhileInZoneData() != null)
            return "Random site name is: " + self.getWhileInZoneData();
        return null;
    }
}
