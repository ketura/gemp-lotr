package com.gempukku.lotro.cards.set2.moria;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.effects.ExertCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mines of Moria
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 1
 * Type: Artifact
 * Strength: +1
 * Game Text: Bearer must be The Balrog. Skirmish: Exert The Balrog to discard a helm, shield, or armor borne by
 * a character The Balrog is skirmishing.
 */
public class Card2_074 extends AbstractAttachable {
    public Card2_074() {
        super(Side.SHADOW, CardType.ARTIFACT, 1, Culture.MORIA, PossessionClass.HAND_WEAPON, "Whip of Many Thongs", null, true);
    }

    @Override
    public Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.balrog;
    }

    @Override
    public int getStrength() {
        return 1;
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && PlayConditions.canExert(self, game, self.getAttachedTo())) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ExertCharactersEffect(action, self, self.getAttachedTo()));
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1,
                            Filters.or(
                                    PossessionClass.HELM,
                                    PossessionClass.SHIELD,
                                    PossessionClass.ARMOR),
                            Filters.attachedTo(
                                    Filters.inSkirmishAgainst(Filters.hasAttached(self)))));
            return Collections.singletonList(action);
        }
        return null;
    }
}
