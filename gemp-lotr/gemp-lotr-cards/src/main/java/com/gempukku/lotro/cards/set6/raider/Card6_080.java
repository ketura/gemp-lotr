package com.gempukku.lotro.cards.set6.raider;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.cards.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Ents of Fangorn
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 8
 * Type: Minion • Man
 * Strength: 13
 * Vitality: 4
 * Site: 4
 * Game Text: Southron. Archer. Archery: Remove (3) to add 1 to the minion archery total for each companion over 4
 * (limit +4).
 */
public class Card6_080 extends AbstractMinion {
    public Card6_080() {
        super(8, 13, 4, 4, Race.MAN, Culture.RAIDER, "Southron Archer Legion");
        addKeyword(Keyword.SOUTHRON);
        addKeyword(Keyword.ARCHER);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.ARCHERY, self, 3)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveTwilightEffect(3));
            int bonus = Math.min(4, Filters.countActive(game.getGameState(), game.getModifiersQuerying(), CardType.COMPANION) - 4);
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new ArcheryTotalModifier(self, Side.SHADOW, bonus), Phase.ARCHERY));
            return Collections.singletonList(action);
        }
        return null;
    }
}
