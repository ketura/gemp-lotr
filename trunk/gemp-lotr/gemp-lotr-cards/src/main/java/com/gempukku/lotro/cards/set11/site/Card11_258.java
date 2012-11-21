package com.gempukku.lotro.cards.set11.site;

import com.gempukku.lotro.cards.AbstractNewSite;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Shadows
 * Twilight Cost: 1
 * Type: Site
 * Game Text: Battleground. Each time a companion is killed, add a burden.
 */
public class Card11_258 extends AbstractNewSite {
    public Card11_258() {
        super("Slag Mounds", 1, Direction.RIGHT);
        addKeyword(Keyword.BATTLEGROUND);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.forEachKilled(game, effectResult, CardType.COMPANION)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new AddBurdenEffect(self.getOwner(), self, 1));
            return Collections.singletonList(action);
        }
        return null;
    }
}
