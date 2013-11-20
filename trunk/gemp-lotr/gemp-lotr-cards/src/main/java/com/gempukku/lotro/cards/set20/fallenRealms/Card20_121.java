package com.gempukku.lotro.cards.set20.fallenRealms;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.cards.modifiers.conditions.MinThreatCondition;
import com.gempukku.lotro.cards.modifiers.evaluator.ConditionEvaluator;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * ❺ •Easterling Strategist [Fal]
 * Minion • Man
 * Strength: 10   Vitality: 3   Roaming: 4
 * Easterling. Enduring. (For each wound on this character, this character is strength +2.)
 * Lurker. (During the skirmish phase(s), the Free Peoples player must resolve all skirmishes involving minions who do not have the lurker keyword before he may choose to resolve any skirmishes involving one or more minions who do have it.)
 * Skirmish: Heal this minion to make an Easterling strength +2 (or strength +3 if you can spot 2 threats).
 * <p/>
 * http://lotrtcg.org/coreset/fallenrealms/easterlingstrategist(r3).jpg
 */
public class Card20_121 extends AbstractMinion {
    public Card20_121() {
        super(5, 10, 3, 4, Race.MAN, Culture.FALLEN_REALMS, "Easterling Strategist", null, true);
        addKeyword(Keyword.EASTERLING);
        addKeyword(Keyword.ENDURING);
        addKeyword(Keyword.LURKER);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && PlayConditions.canHeal(self, game, self)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndHealCharactersEffect(action, playerId, self));
            action.appendEffect(
                    new ChooseAndAddUntilEOPStrengthBonusEffect(
                            action, self, playerId, new ConditionEvaluator(2, 3, new MinThreatCondition(2)), Keyword.EASTERLING));
            return Collections.singletonList(action);
        }
        return null;
    }
}
