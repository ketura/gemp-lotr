package com.gempukku.lotro.cards.set20.fallenRealms;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.RemoveThreatsEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * ❼ •Easterling Captain, Champion of Rhûn [Fal]
 * Minion • Man
 * Strength: 10   Vitality: 3   Roaming: 4
 * Easterling. Enduring. (For each wound on this character, this character is strength +2.) Fierce. (During a turn,
 * after all normal skirmishes are resolved, this minion must be defended against a second time.)
 * When you play this minion, you may remove a threat to play a [Fal] card from your discard pile.
 * http://lotrtcg.org/coreset/fallenrealms/easterlingcaptaincor(r3).jpg
 */
public class Card20_112 extends AbstractMinion {
    public Card20_112() {
        super(7, 10, 3, 4, Race.MAN, Culture.FALLEN_REALMS, "Easterling Captain", "Champion of Rhun", true);
        addKeyword(Keyword.EASTERLING);
        addKeyword(Keyword.ENDURING);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)
                && PlayConditions.canRemoveThreat(game, self, 1)
                && PlayConditions.canPlayFromDiscard(playerId, game, Culture.FALLEN_REALMS)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new RemoveThreatsEffect(self, 1));
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, Culture.FALLEN_REALMS));
            return Collections.singletonList(action);
        }
        return null;
    }
}
