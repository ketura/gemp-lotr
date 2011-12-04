package com.gempukku.lotro.cards.set11.men;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfTurnModifierEffect;
import com.gempukku.lotro.cards.modifiers.ResistanceModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 3
 * Type: Minion • Man
 * Strength: 9
 * Vitality: 2
 * Site: 4
 * Game Text: Ambush (1). When you play this minion at a plains site, you may spot an unbound companion to make that
 * companion resistance -1 until the end of the turn.
 */
public class Card11_079 extends AbstractMinion {
    public Card11_079() {
        super(3, 9, 2, 4, Race.MAN, Culture.MEN, "Fearsome Dunlending");
        addKeyword(Keyword.AMBUSH, 1);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)
                && PlayConditions.location(game, Keyword.PLAINS)
                && PlayConditions.canSpot(game, Filters.unboundCompanion)) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose an unbound companion", Filters.unboundCompanion) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.insertEffect(
                                    new AddUntilEndOfTurnModifierEffect(
                                            new ResistanceModifier(self, card, -1)));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
