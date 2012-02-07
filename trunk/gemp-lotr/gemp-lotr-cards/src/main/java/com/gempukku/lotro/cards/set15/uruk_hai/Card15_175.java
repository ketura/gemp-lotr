package com.gempukku.lotro.cards.set15.uruk_hai;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.TakeControlOfASiteEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 3
 * Type: Minion • Uruk-Hai
 * Strength: 8
 * Vitality: 3
 * Site: 5
 * Game Text: Damage +1. While you can spot a companion of strength 10 or more, this minion is strength +2.
 * Regroup: Exert 4 [URUK-HAI] minions (or discard 3 [URUK-HAI] minions) to take control of a site.
 */
public class Card15_175 extends AbstractMinion {
    public Card15_175() {
        super(3, 8, 3, 5, Race.URUK_HAI, Culture.URUK_HAI, "Uruk Infantry");
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, self, new SpotCondition(CardType.COMPANION, Filters.not(Filters.lessStrengthThan(10))), 2);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.REGROUP, self, 0)
                && (PlayConditions.canExert(self, game, 4, Culture.URUK_HAI, CardType.MINION)
                || PlayConditions.canDiscardFromPlay(self, game, 3, Culture.URUK_HAI, CardType.MINION))) {
            ActivateCardAction action = new ActivateCardAction(self);
            List<Effect> possibleCosts = new LinkedList<Effect>();
            possibleCosts.add(
                    new ChooseAndExertCharactersEffect(action, playerId, 4, 4, Culture.URUK_HAI, CardType.MINION) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Exert 4 URUK_HAI minions";
                        }
                    });
            possibleCosts.add(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 3, 3, Culture.URUK_HAI, CardType.MINION) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Discard 3 URUK_HAI minions";
                        }
                    });
            action.appendCost(
                    new ChoiceEffect(action, playerId, possibleCosts));
            action.appendEffect(
                    new TakeControlOfASiteEffect(self, playerId));
            return Collections.singletonList(action);
        }
        return null;
    }
}
