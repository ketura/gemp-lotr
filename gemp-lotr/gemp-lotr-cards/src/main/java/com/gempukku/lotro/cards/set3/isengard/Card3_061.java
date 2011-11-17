package com.gempukku.lotro.cards.set3.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Realms of Elf-lords
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 3
 * Type: Minion • Orc
 * Strength: 7
 * Vitality: 3
 * Site: 4
 * Game Text: While you can spot another [ISENGARD] Orc, no player may use archery special abilities. Regroup:Exert
 * this minion and spot 3 wounds on the Ring-bearer to exert every companion.
 */
public class Card3_061 extends AbstractMinion {
    public Card3_061() {
        super(3, 7, 3, 4, Race.ORC, Culture.ISENGARD, "Isengard Warrior");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, final PhysicalCard self) {
        return Collections.singletonList(
                new AbstractModifier(self, "While you can spot another [ISENGARD] Orc, no player may use archery special abilities.", null, ModifierEffect.ACTION_MODIFIER) {
                    @Override
                    public boolean canPlayAction(GameState gameState, ModifiersQuerying modifiersQuerying, String performingPlayer, Action action) {
                        if (Filters.canSpot(gameState, modifiersQuerying, Filters.not(Filters.sameCard(self)), Culture.ISENGARD, Race.ORC)) {
                            PhysicalCard actionSource = action.getActionSource();
                            if (actionSource != null
                                    && action.getActionTimeword() == Phase.ARCHERY
                                    && actionSource.getBlueprint().getCardType() != CardType.EVENT)
                                return false;
                        }
                        return true;
                    }
                });
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.REGROUP, self, 0)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), self)
                && game.getGameState().getWounds(Filters.findFirstActive(game.getGameState(), game.getModifiersQuerying(), Keyword.RING_BEARER)) >= 3) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ExertCharactersEffect(self, self));
            action.appendEffect(
                    new ExertCharactersEffect(self, CardType.COMPANION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
