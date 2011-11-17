package com.gempukku.lotro.cards.set1.wraith;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AttachCardEffect;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.TransferPermanentEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 1
 * Type: Possession • Hand Weapon
 * Strength: +1
 * Game Text: Bearer must be a Nazgul. This weapon may be borne in addition to 1 other hand weapon. Skirmish: Discard
 * this possession to transfer Blade Tip from your support area or discard pile to a companion bearer is skirmishing.
 */
public class Card1_216 extends AbstractAttachable {
    public Card1_216() {
        super(Side.SHADOW, CardType.POSSESSION, 1, Culture.WRAITH, PossessionClass.HAND_WEAPON, "Morgul Blade");
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, Filters.hasAttached(self), 1);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Race.NAZGUL;
    }

    @Override
    public boolean isExtraPossessionClass() {
        return true;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.SKIRMISH, self, 0)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new DiscardCardsFromPlayEffect(self, self));

            final PhysicalCard bladeTipInSupport = Filters.findFirstActive(game.getGameState(), game.getModifiersQuerying(), Filters.name("Blade Tip"), Filters.owner(playerId), Filters.zone(Zone.SUPPORT));
            final Collection<PhysicalCard> bladeTipsInDiscard = Filters.filter(game.getGameState().getDiscard(playerId), game.getGameState(), game.getModifiersQuerying(), Filters.name("Blade Tip"));

            List<Effect> possibleEffects = new LinkedList<Effect>();
            if (bladeTipInSupport != null) {
                possibleEffects.add(
                        new ChooseActiveCardEffect(self, playerId, "Choose a companion", CardType.COMPANION, Filters.inSkirmishAgainst(self.getAttachedTo())) {
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
                        new ChooseActiveCardEffect(self, playerId, "Choose a companion", CardType.COMPANION, Filters.inSkirmishAgainst(self.getAttachedTo())) {
                            @Override
                            protected void cardSelected(LotroGame game, PhysicalCard card) {
                                action.insertEffect(
                                        new AttachCardEffect(bladeTipsInDiscard.iterator().next(), card));
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
