package com.gempukku.lotro.cards.set7.raider;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.effects.RemoveBurdenEffect;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 4
 * Type: Minion • Man
 * Strength: 9
 * Vitality: 2
 * Site: 4
 * Game Text: Easterling. Skirmish: Remove a burden and exert this minion to heal another Easterling (or 2 Easterlings
 * if you have initiative).
 */
public class Card7_140 extends AbstractMinion {
    public Card7_140() {
        super(4, 9, 2, 4, Race.MAN, Culture.RAIDER, "Easterling Assailant");
        addKeyword(Keyword.EASTERLING);
    }

    @Override
    public List<? extends Action> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && PlayConditions.canSelfExert(self, game)
                && game.getGameState().getBurdens() >= 1) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveBurdenEffect(playerId, self));
            action.appendCost(
                    new SelfExertEffect(action, self));
            boolean hasInitiative = PlayConditions.hasInitiative(game, Side.SHADOW);
            int healCount = hasInitiative ? 2 : 1;
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, playerId, healCount, healCount, Filters.not(self), Keyword.EASTERLING));
            return Collections.singletonList(action);
        }
        return null;
    }
}
