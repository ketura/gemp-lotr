package com.gempukku.lotro.cards.set31.shire;

import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.effects.AddBurdenEffect;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.CheckPhaseLimitEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.ResistanceModifier;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * •Burglar's Contract [Shire]
 * Possession
 * Twilight Cost 1
 * Resistance bonus: +1
 * 'Bearer must be Bilbo.
 * Skirmish: Add a doubt and exert Bilbo to make another companion strength +2 (limit +4).'
 */
public class Card31_041 extends AbstractAttachableFPPossession {
    public Card31_041() {
        super(1, 0, 0, Culture.SHIRE, null, "Burglar's Contract", null, true);
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new ResistanceModifier(self, Filters.hasAttached(self), 1));
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.name("Bilbo");
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canAddBurdens(game, playerId, self)
                && PlayConditions.canExert(self, game, Filters.name("Bilbo"))) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new AddBurdenEffect(playerId, self, 1));
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.name("Bilbo")));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose another companion to add strength bonus", CardType.COMPANION, Filters.not(Filters.name("Bilbo"))) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.appendEffect(
                                    new CheckPhaseLimitEffect(action, self, card.getCardId()+"-", 2, Phase.SKIRMISH,
                                            new AddUntilEndOfPhaseModifierEffect(
                                                    new StrengthModifier(self, card, 2))));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
