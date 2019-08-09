package com.gempukku.lotro.cards.set20.fallenRealms;

import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.PlayCardResult;

import java.util.Collections;
import java.util.List;

/**
 * ❹ Easterling Conscript [Fal]
 * Minion • Man
 * Strength: 7   Vitality: 1   Roaming: 4
 * Easterling. Toil 1. (When you play this card, you may reduce its twilight cost by 1. You do this by exerting one of
 * your characters of the same culture as this card.)
 * When you play this minion using toil, you may discard a Free Peoples condition.
 * http://lotrtcg.org/coreset/fallenrealms/easterlingconscript(r3).jpg
 */
public class Card20_113 extends AbstractMinion {
    public Card20_113() {
        super(4, 7, 1, 4, Race.MAN, Culture.FALLEN_REALMS, "Easterling Conscript");
        addKeyword(Keyword.EASTERLING);
        addKeyword(Keyword.TOIL, 1);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self) && ((PlayCardResult) effectResult).isPaidToil()) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Side.FREE_PEOPLE, CardType.CONDITION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
