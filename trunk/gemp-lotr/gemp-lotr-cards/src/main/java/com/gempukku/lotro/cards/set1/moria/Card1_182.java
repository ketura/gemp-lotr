package com.gempukku.lotro.cards.set1.moria;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.Skirmish;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.KillResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 1
 * Type: Possession • Hand Weapon
 * Strength: +2
 * Game Text: Bearer must be a [MORIA] minion. If bearer kills a companion in a skirmish, add 1 burden (or 2 burdens if
 * that companion was a Hobbit).
 */
public class Card1_182 extends AbstractAttachable {
    public Card1_182() {
        super(Side.SHADOW, CardType.POSSESSION, 1, Culture.MORIA, Keyword.HAND_WEAPON, "Goblin Spear");
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Filters.culture(Culture.MORIA), Filters.type(CardType.MINION));
    }

    @Override
    public Modifier getAlwaysOnEffect(PhysicalCard self) {
        return new StrengthModifier(self, Filters.attachedTo(self), 2);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.KILL) {
            KillResult killResult = (KillResult) effectResult;
            PhysicalCard killedCard = killResult.getKilledCard();
            Skirmish skirmish = game.getGameState().getSkirmish();
            if (killedCard.getBlueprint().getCardType() == CardType.COMPANION && skirmish != null
                    && skirmish.getFellowshipCharacter() == killedCard && skirmish.getShadowCharacters().contains(self.getAttachedTo())) {
                int burdens = (killedCard.getBlueprint().getRace() == Race.HOBBIT) ? 2 : 1;
                RequiredTriggerAction action = new RequiredTriggerAction(self, null, "Add " + burdens + " burden(s)");
                for (int i = 0; i < burdens; i++)
                    action.addEffect(new AddBurdenEffect(killedCard.getOwner()));
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
