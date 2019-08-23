package com.gempukku.lotro.cards.set20.dwarven;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * 3
 * •Gloin, Venerable Dwarf
 * Companion • Dwarf
 * 5	4	6
 * Damage +1.
 * While you can spot Gimli, Gloin is strength +1 and his twilight cost is -1.
 * Skirmish: Exert Gloin to make another Dwarf strength +2 (and damage +1 if that Dwarf is Gimli).
 * http://lotrtcg.org/coreset/dwarven/gloinvd(r1).png
 */
public class Card20_057 extends AbstractCompanion {
    public Card20_057() {
        super(3, 5, 4, 6, Culture.DWARVEN, Race.DWARF, null, "Gloin", "Venerable Dwarf", true);
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public int getTwilightCostModifier(LotroGame game, PhysicalCard self) {
        if (Filters.canSpot(game, Filters.gimli))
            return -1;
        return 0;
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new StrengthModifier(self, self, new SpotCondition(Filters.gimli), 1));
}

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(final String playerId, final LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canSelfExert(self, game)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 2, Race.DWARF) {
                        @Override
                        protected void selectedCharacterCallback(PhysicalCard selectedCharacter) {
                            if (Filters.gimli.accepts(game, selectedCharacter)) {
                                action.appendEffect(
                                        new AddUntilEndOfPhaseModifierEffect(
                                                new KeywordModifier(self, selectedCharacter, Keyword.DAMAGE, 1)));
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
