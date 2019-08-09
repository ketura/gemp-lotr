package com.gempukku.lotro.cards.set12.dwarven;
import java.util.List;
import java.util.Collections;
import java.util.List;
import java.util.Collections;import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 0
 * Type: Condition • Support Area
 * Game Text: Each Dwarf gains muster. (At the start of the regroup phase, you may discard a card from hand to draw
 * a card.) Each time the fellowship moves from a plains site, discard a card from your hand.
 */
public class Card12_014 extends AbstractPermanent {
    public Card12_014() {
        super(Side.FREE_PEOPLE, 0, CardType.CONDITION, Culture.DWARVEN, "Stalwart Support");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new KeywordModifier(self, Race.DWARF, Keyword.MUSTER));
}

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.movesFrom(game, effectResult, Keyword.PLAINS)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new ChooseAndDiscardCardsFromHandEffect(action, self.getOwner(), true, 1));
            return Collections.singletonList(action);
        }
        return null;
    }
}
