package com.gempukku.lotro.cards.set32.wraith;

import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.logic.modifiers.MinionSiteNumberModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;
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
 * Game Text: Fierce. The site number of eath [WRAITH] minion is -3. Response: If the Free Peoples player discards
 * a condition, exert Ulaire Nertea to play a [WRAITH] minion from your discard pile; its twilight cost is -8.
 */
public class Card32_072 extends AbstractMinion {
    public Card32_072() {
        super(4, 9, 2, 5, Race.NAZGUL, Culture.WRAITH, Names.nertea, "Revived", true);
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
                && !((DiscardCardsFromPlayResult) effectResult).getPerformingPlayer().equals(self.getOwner())
                && PlayConditions.canPlayFromDiscard(playerId, game, -8, Culture.WRAITH, CardType.MINION)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, -8, Culture.WRAITH, CardType.MINION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
