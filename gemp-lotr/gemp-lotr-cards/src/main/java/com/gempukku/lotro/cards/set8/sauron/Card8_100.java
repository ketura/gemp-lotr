package com.gempukku.lotro.cards.set8.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.ReturnCardsToHandEffect;
import com.gempukku.lotro.cards.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Siege of Gondor
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 4
 * Type: Minion • Orc
 * Strength: 10
 * Vitality: 2
 * Site: 5
 * Game Text: Besieger. Archery: Return this minion to your hand to make the fellowship archery total -2 and prevent
 * the Free Peoples player from using archery special abilities.
 */
public class Card8_100 extends AbstractMinion {
    public Card8_100() {
        super(4, 10, 2, 5, Race.ORC, Culture.SAURON, "Gorgoroth Servitor");
        addKeyword(Keyword.BESIEGER);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.ARCHERY, self, 0)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ReturnCardsToHandEffect(self, self));
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new ArcheryTotalModifier(self, Side.FREE_PEOPLE, -2), Phase.ARCHERY));
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new AbstractModifier(self, null, null, ModifierEffect.ACTION_MODIFIER) {
                                @Override
                                public boolean canPlayAction(GameState gameState, ModifiersQuerying modifiersQuerying, String performingPlayer, Action action) {
                                    if (performingPlayer.equals(gameState.getCurrentPlayerId())
                                            && action.getActionSource() != null && action.getActionSource().getBlueprint().getCardType() != CardType.EVENT
                                            && action.getActionTimeword() == Phase.ARCHERY)
                                        return false;
                                    return true;
                                }
                            }, Phase.ARCHERY));
            return Collections.singletonList(action);
        }
        return null;
    }
}
