package com.gempukku.lotro.cards.set2.moria;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.effects.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.modifiers.ShouldSkipPhaseModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mines of Moria
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: Plays to your support area. While you can spot The Balrog, skip the archery phase. Maneuver: Exert
 * The Balrog to discard a ranged weapon.
 */
public class Card2_056 extends AbstractPermanent {
    public Card2_056() {
        super(Side.SHADOW, 0, CardType.CONDITION, Culture.MORIA, Zone.SHADOW_SUPPORT, "Fill With Fear");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new ShouldSkipPhaseModifier(self,
                        new SpotCondition(Filters.name("The Balrog")), Phase.ARCHERY));
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.MANEUVER, self, 0)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), Filters.name("The Balrog"))) {
            ActivateCardAction action = new ActivateCardAction(self, Keyword.MANEUVER);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.name("The Balrog")));
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Filters.keyword(Keyword.RANGED_WEAPON)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
