package com.gempukku.lotro.cards.set5.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddTokenEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Token;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Battle of Helm's Deep
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 2
 * Type: Minion • Uruk-Hai
 * Strength: 6
 * Vitality: 2
 * Site: 5
 * Game Text: Damage +1. When you play this minion, you may place an [ISENGARD] token on a machine.
 */
public class Card5_062 extends AbstractMinion {
    public Card5_062() {
        super(2, 6, 2, 5, Race.URUK_HAI, Culture.ISENGARD, "Uruk Sapper");
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a machine", Keyword.MACHINE) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.insertEffect(
                                    new AddTokenEffect(self, card, Token.ISENGARD));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
