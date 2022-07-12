package com.gempukku.lotro.cards.set10.raider;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mount Doom
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 6
 * Type: Minion • Man
 * Strength: 11
 * Vitality: 3
 * Site: 4
 * Game Text: Easterling. To play, spot an Easterling. While this minion is unwounded, he is damage +2. Skirmish: Wound
 * 2 other [RAIDER] Men to heal this minion.
 */
public class Card10_040 extends AbstractMinion {
    public Card10_040() {
        super(6, 11, 3, 4, Race.MAN, Culture.RAIDER, "Easterling Berserker");
        addKeyword(Keyword.EASTERLING);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Keyword.EASTERLING);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(final LotroGame game, final PhysicalCard self) {
        return Collections.singletonList(
                new KeywordModifier(self, self, new Condition() {
                    @Override
                    public boolean isFullfilled(LotroGame game) {
                        return game.getGameState().getWounds(self) == 0;
                    }
                }, Keyword.DAMAGE, 2));
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && PlayConditions.canWound(self, game, 1, 2, Filters.not(self), Culture.RAIDER, Race.MAN)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndWoundCharactersEffect(action, playerId, 2, 2, Filters.not(self), Culture.RAIDER, Race.MAN));
            action.appendEffect(
                    new HealCharactersEffect(self, self.getOwner(), self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
