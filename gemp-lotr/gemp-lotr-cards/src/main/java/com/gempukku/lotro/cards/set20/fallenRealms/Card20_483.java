package com.gempukku.lotro.cards.set20.fallenRealms;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * ❶ Rhûnish Helm [Fal]
 * Possession • Helm
 * Toil 1. (When you play this card, you may reduce its twilight cost by 1. You do this by exerting one of your characters of the same culture as this card.)
 * Bearer must be an Easterling.
 * Bearer gains lurker.
 * <p/>
 * http://lotrtcg.org/coreset/fallenrealms/rhunishhelm(r3).jpg
 */
public class Card20_483 extends AbstractAttachable {
    public Card20_483() {
        super(Side.SHADOW, CardType.POSSESSION, 1, Culture.FALLEN_REALMS, PossessionClass.HELM, "Rhunish Helm");
        addKeyword(Keyword.TOIL, 1);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Keyword.EASTERLING;
    }

    @Override
    public Modifier getAlwaysOnModifier(LotroGame game, PhysicalCard self) {
        return new KeywordModifier(self, Filters.hasAttached(self), Keyword.LURKER);
    }
}
