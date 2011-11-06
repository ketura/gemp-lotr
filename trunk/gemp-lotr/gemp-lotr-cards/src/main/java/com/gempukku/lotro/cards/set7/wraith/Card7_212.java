package com.gempukku.lotro.cards.set7.wraith;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.KillResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 6
 * Type: Minion • Nazgul
 * Strength: 11
 * Vitality: 4
 * Site: 3
 * Game Text: Fierce. While you can spot 6 companions, Ulaire Enquea is damage +1. Each time a companion is killed,
 * you may exert Ulaire Enquea to exert the Ring-bearer.
 */
public class Card7_212 extends AbstractMinion {
    public Card7_212() {
        super(6, 11, 4, 3, Race.NAZGUL, Culture.WRAITH, "Ulaire Enquea", true);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new KeywordModifier(self, self, new SpotCondition(6, CardType.COMPANION), Keyword.DAMAGE, 1));
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.KILL
                && PlayConditions.canSelfExert(self, game)) {
            KillResult result = (KillResult) effectResult;
            List<OptionalTriggerAction> actions = new LinkedList<OptionalTriggerAction>();
            for (PhysicalCard killedCompanion : Filters.filter(result.getKilledCards(), game.getGameState(), game.getModifiersQuerying(), CardType.COMPANION)) {
                OptionalTriggerAction action = new OptionalTriggerAction(self);
                action.appendCost(
                        new ExertCharactersEffect(self, self));
                action.appendEffect(
                        new ExertCharactersEffect(self, Keyword.RING_BEARER));
                actions.add(action);
            }
            return actions;
        }
        return null;
    }
}
