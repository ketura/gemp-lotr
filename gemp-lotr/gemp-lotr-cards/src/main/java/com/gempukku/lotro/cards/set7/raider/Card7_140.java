package com.gempukku.lotro.cards.set7.raider;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.RemoveBurdenEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.timing.Action;

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
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && PlayConditions.canSelfExert(self, game)
                && game.getGameState().getBurdens() >= 1) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveBurdenEffect(self));
            action.appendCost(
                    new ExertCharactersEffect(self, self));
            boolean hasInitiative = PlayConditions.hasInitiative(game, Side.SHADOW);
            int healCount = hasInitiative ? 2 : 1;
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, playerId, healCount, healCount, Keyword.EASTERLING));
            return Collections.singletonList(action);
        }
        return null;
    }
}
