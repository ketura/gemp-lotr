package com.gempukku.lotro.cards.set18.men;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.modifiers.evaluator.CountCultureTokensEvaluator;
import com.gempukku.lotro.cards.modifiers.evaluator.LimitEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: Treachery & Deceit
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 2
 * Type: Possession • Hand Weapon
 * Game Text: Bearer must be a [MEN] minion. Bearer is strength +1 for each [MEN] token you can spot (limit +5).
 */
public class Card18_064 extends AbstractAttachable {
    public Card18_064() {
        super(Side.SHADOW, CardType.POSSESSION, 2, Culture.MEN, PossessionClass.HAND_WEAPON, "Corsair Scimitar");
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.MEN, CardType.MINION);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, Filters.hasAttached(self), null,
                new LimitEvaluator(new CountCultureTokensEvaluator(Token.MEN, Filters.any), 5));
    }
}
