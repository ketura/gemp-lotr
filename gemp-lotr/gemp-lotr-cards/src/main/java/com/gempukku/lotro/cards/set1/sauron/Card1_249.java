package com.gempukku.lotro.cards.set1.sauron;

import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.Skirmish;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Plays to your support area. Each [SAURON] Orc skirmishing a [GONDOR] character is strength +2. Discard
 * this condition if your [SAURON] Orc loses a skirmish.
 */
public class Card1_249 extends AbstractPermanent {
    public Card1_249() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.SAURON, "Gleaming Spires Will Crumble");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(new StrengthModifier(self,
                Filters.and(
                        Culture.SAURON,
                        Race.ORC,
                        new Filter() {
                            @Override
                            public boolean accepts(LotroGame game, PhysicalCard physicalCard) {
                                Skirmish skirmish = game.getGameState().getSkirmish();
                                return (skirmish != null && skirmish.getShadowCharacters().contains(physicalCard)
                                        && skirmish.getFellowshipCharacter() != null
                                        && skirmish.getFellowshipCharacter().getBlueprint().getCulture() == Culture.GONDOR);
                            }
                        }
                ), 2));
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.losesSkirmish(game, effectResult, Filters.and(Culture.SAURON, Race.ORC))) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new SelfDiscardEffect(self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
