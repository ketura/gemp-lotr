package com.gempukku.lotro.cards.set10.wraith;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.modifiers.CantExertWithCardModifier;
import com.gempukku.lotro.cards.modifiers.conditions.PhaseCondition;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mount Doom
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 5
 * Type: Minion • Nazgul
 * Strength: 10
 * Vitality: 3
 * Site: 3
 * Game Text: Enduring. Fierce. Shadow cards cannot exert Ulaire Cantea during a skirmish phase. Skirmish: Heal Ulaire
 * Cantea to discard a possession borne by a character he is skirmishing.
 */
public class Card10_067 extends AbstractMinion {
    public Card10_067() {
        super(5, 10, 3, 3, Race.NAZGUL, Culture.WRAITH, "Ulaire Cantea", true);
        addKeyword(Keyword.ENDURING);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new CantExertWithCardModifier(self, self, new PhaseCondition(Phase.SKIRMISH), Side.SHADOW));
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && PlayConditions.canHeal(self, game, self)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new HealCharactersEffect(self, self));
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.POSSESSION, Filters.attachedTo(Filters.inSkirmishAgainst(self))));
            return Collections.singletonList(action);
        }
        return null;
    }
}
