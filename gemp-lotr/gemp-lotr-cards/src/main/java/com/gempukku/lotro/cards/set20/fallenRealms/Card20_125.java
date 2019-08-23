package com.gempukku.lotro.cards.set20.fallenRealms;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * 2
 * Haradhrim Bow
 * Fallen Realms	Possession • Bow
 * Bearer must be a Southron.
 * Bearer is an Archer.
 * Archery: Exert bearer to make the Free Peoples player exert an unbound companion.
 */
public class Card20_125 extends AbstractAttachable {
    public Card20_125() {
        super(Side.SHADOW, CardType.POSSESSION, 2, Culture.FALLEN_REALMS, PossessionClass.RANGED_WEAPON, "Haradhrim Bow");
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Keyword.SOUTHRON;
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new KeywordModifier(self, Filters.hasAttached(self), Keyword.ARCHER));
}

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.ARCHERY, self, 0)
                && PlayConditions.canExert(self, game, Filters.hasAttached(self))) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.hasAttached(self)));
            action.appendEffect(
                    new ChooseAndExertCharactersEffect(action, game.getGameState().getCurrentPlayerId(), 1, 1, Filters.unboundCompanion));
            return Collections.singletonList(action);
        }
        return null;
    }
}
