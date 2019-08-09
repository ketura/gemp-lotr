package com.gempukku.lotro.cards.set6.sauron;

import com.gempukku.lotro.logic.actions.AbstractCostToEffectAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.modifiers.AbstractExtraPlayCostModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.actions.PlayPermanentAction;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.RemoveBurdenEffect;
import com.gempukku.lotro.logic.effects.SpotEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Ents of Fangorn
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 1
 * Type: Minion • Wraith
 * Strength: 5
 * Vitality: 1
 * Site: 4
 * Game Text: Twilight. Damage +1. To play this minion, remove a burden or spot a twilight minion.
 */
public class Card6_100 extends AbstractMinion {
    public Card6_100() {
        super(1, 5, 1, 4, Race.WRAITH, Culture.SAURON, "Dead Ones");
        addKeyword(Keyword.TWILIGHT);
        addKeyword(Keyword.DAMAGE);
    }

    @Override
    public List<? extends AbstractExtraPlayCostModifier> getExtraCostToPlayModifiers(LotroGame game, final PhysicalCard self) {
        return Collections.singletonList(
                new AbstractExtraPlayCostModifier(self, "Extra cost to play", self) {
                    @Override
                    public void appendExtraCosts(LotroGame game, AbstractCostToEffectAction action, PhysicalCard card) {
                        List<Effect> possibleCosts = new LinkedList<Effect>();
                        possibleCosts.add(
                                new RemoveBurdenEffect(self.getOwner(), self));
                        possibleCosts.add(
                                new SpotEffect(1, CardType.MINION, Keyword.TWILIGHT) {
                                    @Override
                                    public String getText(LotroGame game) {
                                        return "Spot a twilight minion";
                                    }
                                });
                        action.appendCost(
                                new ChoiceEffect(action, self.getOwner(), possibleCosts));
                    }

                    @Override
                    public boolean canPayExtraCostsToPlay(LotroGame game, PhysicalCard card) {
                        return (game.getGameState().getBurdens() >= 1
                                || PlayConditions.canSpot(game, CardType.MINION, Keyword.TWILIGHT));
                    }
                });
    }
}
