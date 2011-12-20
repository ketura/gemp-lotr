package com.gempukku.lotro.cards.set13.elven;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.ReinforceTokenEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndRemoveCultureTokensFromCardEffect;
import com.gempukku.lotro.cards.modifiers.ResistanceModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

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
        super(2, 0, 0, Culture.ELVEN, PossessionClass.MOUNT, "Asfaloth", true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
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
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new ResistanceModifier(self, Filters.hasAttached(self), 1));
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canRemoveTokens(game, Token.ELVEN, 2, Filters.any)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndRemoveCultureTokensFromCardEffect(self, playerId, Token.ELVEN, 2, Filters.any));
            action.appendEffect(
                    new HealCharactersEffect(self, self.getAttachedTo()));
            if (PlayConditions.canSpot(game, self.getAttachedTo(), Filters.inSkirmish)
                    && game.getGameState().isFierceSkirmishes())
                action.appendEffect(
                        new ChooseAndHealCharactersEffect(action, playerId, Race.ELF, Filters.not(self.getAttachedTo())));
            return Collections.singletonList(action);
        }
        return null;
    }
}
