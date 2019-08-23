package com.gempukku.lotro.cards.set40.wraith;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.effects.*;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Title: Morgul Blade
 * Set: Second Edition
 * Side: Shadow
 * Culture: Ringwraith
 * Twilight Cost: 1
 * Type: Possession - Hand Weapon
 * Strength: +1
 * Card Number: 1R190
 * Game Text: Bearer must be a Nazgul. This possession may be borne in addition to 1 other hand weapon. Skirmish: Discard this possession to transfer Blade Tip from your support area or discard pile to a companion bearer is skirmishing.
 */
public class Card40_190 extends AbstractAttachable {
    public Card40_190() {
        super(Side.SHADOW, CardType.POSSESSION, 1, Culture.WRAITH, PossessionClass.HAND_WEAPON, "Morgul Blade");
    }

    @Override
    public boolean isExtraPossessionClass(LotroGame game, PhysicalCard self, PhysicalCard attachedTo) {
        return true;
    }

    @Override
    public int getStrength() {
        return 1;
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Race.NAZGUL;
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));

            final PhysicalCard bladeTipInSupport = Filters.findFirstActive(game, Filters.name("Blade Tip"), Filters.owner(playerId), Zone.SUPPORT);
            final Collection<PhysicalCard> bladeTipsInDiscard = Filters.filter(game.getGameState().getDiscard(playerId), game, Filters.name("Blade Tip"));

            List<Effect> possibleEffects = new LinkedList<Effect>();
            if (bladeTipInSupport != null) {
                possibleEffects.add(
                        new ChooseActiveCardEffect(self, playerId, "Choose a companion", CardType.COMPANION, Filters.inSkirmishAgainst(self.getAttachedTo()), Filters.not(Filters.hasAttached(Filters.name("Blade Tip")))) {
                            @Override
                            protected void cardSelected(LotroGame game, PhysicalCard card) {
                                action.insertEffect(
                                        new TransferPermanentEffect(bladeTipInSupport, card));
                            }

                            @Override
                            public String getText(LotroGame game) {
                                return "Transfer Blade Tip from support area";
                            }
                        });
            }
            if (bladeTipsInDiscard.size() > 0) {
                possibleEffects.add(
                        new ChooseActiveCardEffect(self, playerId, "Choose a companion", CardType.COMPANION, Filters.inSkirmishAgainst(self.getAttachedTo()), Filters.not(Filters.hasAttached(Filters.name("Blade Tip")))) {
                            @Override
                            protected void cardSelected(LotroGame game, PhysicalCard card) {
                                action.insertEffect(
                                        new TransferPermanentNotFromPlayEffect(bladeTipsInDiscard.iterator().next(), card));
                            }

                            @Override
                            public String getText(LotroGame game) {
                                return "Transfer Blade Tip from discard";
                            }
                        });
            }
            action.appendEffect(
                    new ChoiceEffect(action, playerId, possibleEffects));
            return Collections.singletonList(action);
        }
        return null;
    }
}
