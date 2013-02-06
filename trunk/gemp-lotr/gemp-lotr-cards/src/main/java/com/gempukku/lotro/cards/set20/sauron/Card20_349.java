package com.gempukku.lotro.cards.set20.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.cards.modifiers.conditions.SpotCulturesCondition;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * 5
 * •Barad-Dur Captain
 * Sauron	Minion • Orc
 * 13	3	6
 * While you cannot spot 3 Free Peoples cultures, this minion is damage +1.
 * Skirmish: Exert Barad-Dur Captain to wound a companion he is skirmishing.
 */
public class Card20_349 extends AbstractMinion {
    public Card20_349() {
        super(5, 13, 3, 6, Race.ORC, Culture.SAURON, "Barad-Dur Captain", null, true);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new KeywordModifier(self, self,
                new SpotCulturesCondition(3, Side.FREE_PEOPLE), Keyword.DAMAGE, 1);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && PlayConditions.canSelfExert(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, CardType.COMPANION, Filters.inSkirmishAgainst(self)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
