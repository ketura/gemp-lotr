package com.gempukku.lotro.cards.set1.wraith;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.TransferPermanentEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.Skirmish;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.effects.DiscardCardFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
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
        super(Side.SHADOW, CardType.POSSESSION, 1, Culture.WRAITH, Keyword.HAND_WEAPON, "Morgul Blade");
    }

    @Override
    public Modifier getAlwaysOnEffect(PhysicalCard self) {
        return new StrengthModifier(self, Filters.isAttachedTo(self), 1);
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.race(Race.NAZGUL);
    }

    @Override
    public boolean isExtraPossessionClass() {
        return true;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.SKIRMISH, self, 0)
                && self.getZone() == Zone.SHADOW_SUPPORT) {
            DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, null, "Discard this possession to transfer Blade Tip from your support area or discard pile to a companion bearer is skirmishing.");
            action.addCost(
                    new DiscardCardFromPlayEffect(self, self));
            Skirmish skirmish = game.getGameState().getSkirmish();
            if (skirmish != null && skirmish.getShadowCharacters().contains(self.getAttachedTo())) {
                final PhysicalCard fpChar = skirmish.getFellowshipCharacter();
                if (fpChar != null) {
                    final PhysicalCard bladeTip = Filters.findFirstActive(game.getGameState(), game.getModifiersQuerying(), Filters.name("Blade Tip"), Filters.owner(playerId), Filters.zone(Zone.SHADOW_SUPPORT));
                    if (bladeTip != null)
                        action.addEffect(
                                new TransferPermanentEffect(bladeTip, fpChar));
                }
            }
            return Collections.singletonList(action);
        }
        return null;
    }
}
