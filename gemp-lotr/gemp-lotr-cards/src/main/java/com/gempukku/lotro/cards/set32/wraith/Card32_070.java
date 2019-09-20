package com.gempukku.lotro.cards.set32.wraith;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.MinionSiteNumberModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.timing.results.DiscardCardsFromPlayResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Clouds Burst
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 4
 * Type: Minion • Nazgul
 * Strength: 9
 * Vitality: 2
 * Site: 5
 * Game Text: Fierce. The site number of each [WRAITH] minion is -3.
 * Each time the Free Peoples player discards a condition, you may discard an ally.
 */
public class Card32_070 extends AbstractMinion {
    public Card32_070() {
        super(4, 9, 2, 5, Race.NAZGUL, Culture.WRAITH, Names.lemenya, "Revived", true);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new MinionSiteNumberModifier(self, Culture.WRAITH, null, -3));
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.forEachDiscardedFromPlay(game, effectResult, CardType.CONDITION)
                && ((DiscardCardsFromPlayResult) effectResult).getPerformingPlayer() != null
                && !((DiscardCardsFromPlayResult) effectResult).getPerformingPlayer().equals(self.getOwner())) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.ALLY));
            return Collections.singletonList(action);
        }
        return null;
    }
}
