package com.gempukku.lotro.cards.set13.gondor;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.OptionalEffect;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Possession
 * Game Text: Bearer must be a [GONDOR] Man. Fellowship: Discard this possession from play to heal a companion and, if
 * that companion is a [SHIRE] companion, you may discard a condition he or she bears.
 */
public class Card13_073 extends AbstractAttachableFPPossession {
    public Card13_073() {
        super(1, 0, 0, Culture.GONDOR, null, "Kingsfoil");
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.GONDOR, Race.MAN);
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(final String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canSelfDiscard(self, game)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, playerId, CardType.COMPANION) {
                        @Override
                        protected void forEachCardChosenToHealCallback(final PhysicalCard character) {
                            if (character.getBlueprint().getCulture() == Culture.SHIRE)
                                action.appendEffect(
                                        new OptionalEffect(action, playerId,
                                                new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.CONDITION, Filters.attachedTo(character)) {
                                                    @Override
                                                    public String getText(LotroGame game) {
                                                        return "Discard a condition " + GameUtils.getCardLink(character) + " bears";
                                                    }
                                                }));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
