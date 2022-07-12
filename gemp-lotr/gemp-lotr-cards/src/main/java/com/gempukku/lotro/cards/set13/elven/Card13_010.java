package com.gempukku.lotro.cards.set13.elven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;
import com.gempukku.lotro.logic.effects.ReinforceTokenEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndRemoveCultureTokensFromCardEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Possession • Mount
 * Resistance: +1
 * Game Text: Bearer must be an Elf. When you play Asfaloth, you may reinforce an [ELVEN] token. Skirmish: Remove
 * 2 [ELVEN] tokens to heal bearer (or heal bearer and heal another Elf if bearer is in a fierce skirmish).
 */
public class Card13_010 extends AbstractAttachableFPPossession {
    public Card13_010() {
        super(2, 0, 0, Culture.ELVEN, PossessionClass.MOUNT, "Asfaloth", "Swift Blossom", true);
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Race.ELF;
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ReinforceTokenEffect(self, playerId, Token.ELVEN));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public int getResistance() {
        return 1;
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canRemoveTokensFromAnything(game, Token.ELVEN, 2)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndRemoveCultureTokensFromCardEffect(self, playerId, Token.ELVEN, 1, Filters.any));
            action.appendCost(
                    new ChooseAndRemoveCultureTokensFromCardEffect(self, playerId, Token.ELVEN, 1, Filters.any));
            action.appendEffect(
                    new HealCharactersEffect(self, self.getOwner(), self.getAttachedTo()));
            if (PlayConditions.canSpot(game, self.getAttachedTo(), Filters.inSkirmish)
                    && game.getGameState().isFierceSkirmishes())
                action.appendEffect(
                        new ChooseAndHealCharactersEffect(action, self.getOwner(), Race.ELF, Filters.not(self.getAttachedTo())));
            return Collections.singletonList(action);
        }
        return null;
    }
}
