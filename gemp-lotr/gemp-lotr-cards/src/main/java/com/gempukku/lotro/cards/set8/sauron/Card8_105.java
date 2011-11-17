package com.gempukku.lotro.cards.set8.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromStackedEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: Siege of Gondor
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 10
 * Type: Minion • Troll
 * Strength: 18
 * Vitality: 4
 * Site: 5
 * Game Text: Besieger. Fierce. The twilight cost of this minion is -2 for each [SAURON] minion stacked on a site. Each
 * time this minion wins a skirmish, you may play a besieger stacked on a site you control. That besieger is fierce
 * and damage +1 until the regroup phase.
 */
public class Card8_105 extends AbstractMinion {
    public Card8_105() {
        super(10, 18, 4, 5, Race.TROLL, Culture.SAURON, "Olog-hai of Mordor");
        addKeyword(Keyword.BESIEGER);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public int getTwilightCostModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        int count = 0;
        final Collection<PhysicalCard> sites = Filters.filterActive(gameState, modifiersQuerying, CardType.SITE);
        for (PhysicalCard site : sites)
            count += Filters.filter(gameState.getStackedCards(site), gameState, modifiersQuerying, Culture.SAURON, CardType.MINION).size();

        return -2 * count;
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, self)
                && PlayConditions.canPlayFromStacked(playerId, game, Filters.siteControlled(playerId), Keyword.BESIEGER)) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndPlayCardFromStackedEffect(playerId, Filters.siteControlled(playerId), Keyword.BESIEGER) {
                        @Override
                        protected void afterCardPlayed(PhysicalCard cardPlayed) {
                            action.appendEffect(
                                    new AddUntilStartOfPhaseModifierEffect(
                                            new KeywordModifier(self, cardPlayed, Keyword.FIERCE), Phase.REGROUP));
                            action.appendEffect(
                                    new AddUntilStartOfPhaseModifierEffect(
                                            new KeywordModifier(self, cardPlayed, Keyword.DAMAGE, 1), Phase.REGROUP));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
