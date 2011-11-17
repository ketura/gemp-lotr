package com.gempukku.lotro.cards.set10.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mount Doom
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 6
 * Type: Minion • Orc
 * Strength: 13
 * Vitality: 3
 * Site: 4
 * Game Text: Besieger. When you play Gothmog, the Free Peoples player must wound a companion for each site you control.
 * While you control a site, Gothmog is fierce.
 */
public class Card10_088 extends AbstractMinion {
    public Card10_088() {
        super(6, 13, 3, 4, Race.ORC, Culture.SAURON, "Gothmog", true);
        addKeyword(Keyword.BESIEGER);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            int countControlled = Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Filters.siteControlled(self.getOwner()));
            for (int i = 0; i < countControlled; i++)
                action.appendEffect(
                        new ChooseAndWoundCharactersEffect(action, game.getGameState().getCurrentPlayerId(), 1, 1, CardType.COMPANION));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new KeywordModifier(self, self, new SpotCondition(Filters.siteControlled(self.getOwner())), Keyword.FIERCE, 1));
    }
}
