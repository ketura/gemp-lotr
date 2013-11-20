package com.gempukku.lotro.cards.set20.fallenRealms;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * ❺ Easterling Detachment [Fal]
 * Minion • Man
 * Strength: 9   Vitality: 3   Roaming: 4
 * Easterling. Enduring. (For each wound on this character, this character is strength +2.)
 * Skirmish: Heal this minion to make it damage +1.
 * http://lotrtcg.org/coreset/fallenrealms/easterlingdetachment(r3).jpg
 */
public class Card20_114 extends AbstractMinion {
    public Card20_114() {
        super(5, 9, 3, 4, Race.MAN, Culture.FALLEN_REALMS, "Easterling Detachment");
        addKeyword(Keyword.EASTERLING);
        addKeyword(Keyword.ENDURING);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && PlayConditions.canHeal(self, game, self)) {
            ActivateCardAction action =new ActivateCardAction(self);
            action.appendCost(
                    new HealCharactersEffect(self, self));
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new KeywordModifier(self, self, Keyword.DAMAGE, 1)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
