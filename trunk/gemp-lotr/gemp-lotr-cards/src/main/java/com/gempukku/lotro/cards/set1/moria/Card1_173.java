package com.gempukku.lotro.cards.set1.moria;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddTwilightEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.effects.DiscardCardFromPlayEffect;
import com.gempukku.lotro.logic.effects.WoundCharacterEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;
import com.gempukku.lotro.logic.timing.results.WoundResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: Plays to your support area. Each time you play a [MORIA] weapon, add (1). Response: If a [MORIA] Orc is
 * about to take a wound, discard this condition to prevent that wound.
 */
public class Card1_173 extends AbstractPermanent {
    public Card1_173() {
        super(Side.SHADOW, 0, CardType.CONDITION, Culture.MORIA, Zone.SHADOW_SUPPORT, "Goblin Armory");
    }

    @Override
    public List<? extends Action> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.played(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.and(Filters.owner(self.getOwner()), Filters.culture(Culture.MORIA), Filters.or(Filters.keyword(Keyword.HAND_WEAPON), Filters.keyword(Keyword.RANGED_WEAPON))))) {
            DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, null, "Add (1)");
            action.addEffect(new AddTwilightEffect(1));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends Action> getOptionalBeforeActions(String playerId, LotroGame game, final Effect effect, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.WOUND) {
            WoundResult woundResult = (WoundResult) effectResult;
            PhysicalCard woundedCard = woundResult.getWoundedCard();
            if (woundedCard.getBlueprint().getCulture() == Culture.MORIA && woundedCard.getBlueprint().getRace() == Race.ORC) {
                DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, null, "Prevent wound");
                action.addCost(
                        new DiscardCardFromPlayEffect(self, self));
                action.addEffect(
                        new UnrespondableEffect() {
                            @Override
                            public void playEffect(LotroGame game) {
                                ((WoundCharacterEffect) effect).prevent();
                            }
                        });
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
