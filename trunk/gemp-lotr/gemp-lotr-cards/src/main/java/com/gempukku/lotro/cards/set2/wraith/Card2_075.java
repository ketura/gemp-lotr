package com.gempukku.lotro.cards.set2.wraith;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.FreePeoplePlayerMayNotAssignCharacterModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.RemoveKeywordModifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Mines of Moria
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 2
 * Type: Minion • Man
 * Strength: 4
 * Vitality: 1
 * Site: 2
 * Game Text: Nazgul are not roaming. The Free Peoples player may not assign a character to skirmish Bill Ferny.
 * Discard Bill Ferny if underground.
 */
public class Card2_075 extends AbstractMinion {
    public Card2_075() {
        super(2, 4, 1, 2, Race.MAN, Culture.WRAITH, "Bill Ferny", true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(new RemoveKeywordModifier(self, Filters.race(Race.NAZGUL), Keyword.ROAMING));
        modifiers.add(
                new FreePeoplePlayerMayNotAssignCharacterModifier(self, Filters.sameCard(self)));
        return modifiers;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (game.getModifiersQuerying().hasKeyword(game.getGameState(), game.getGameState().getCurrentSite(), Keyword.UNDERGROUND)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new DiscardCardsFromPlayEffect(self, self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
