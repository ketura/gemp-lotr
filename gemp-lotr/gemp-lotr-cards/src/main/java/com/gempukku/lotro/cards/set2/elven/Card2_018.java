package com.gempukku.lotro.cards.set2.elven;
import java.util.List;
import java.util.Collections;
import java.util.List;
import java.util.Collections;import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.PreventCardEffect;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: Mines of Moria
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Condition
 * Game Text: Tale. Plays to your support area. While you can spot an Elf companion, the minion archery total is -1.
 * Response: If an Elf is about to take a wound, discard this condition to prevent that wound.
 */
public class Card2_018 extends AbstractPermanent {
    public Card2_018() {
        super(Side.FREE_PEOPLE, 2, CardType.CONDITION, Culture.ELVEN, "Hosts of the Last Alliance");
        addKeyword(Keyword.TALE);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new ArcheryTotalModifier(self, Side.SHADOW, new SpotCondition(Race.ELF, CardType.COMPANION), -1));
}

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isGettingWounded(effect, game, Race.ELF)) {
            final WoundCharactersEffect woundEffect = (WoundCharactersEffect) effect;
            Collection<PhysicalCard> woundedCharacters = woundEffect.getAffectedCardsMinusPrevented(game);
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose an Elf", Race.ELF, Filters.in(woundedCharacters)) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard elf) {
                            action.appendEffect(
                                    new PreventCardEffect(woundEffect, elf));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
