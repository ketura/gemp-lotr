package com.gempukku.lotro.cards.set18.site;
import java.util.List;
import java.util.Collections;
import java.util.List;
import java.util.Collections;import com.gempukku.lotro.logic.cardtype.AbstractShadowsSite;
import com.gempukku.lotro.logic.modifiers.CantBeTransferredModifier;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * Set: Treachery & Deceit
 * Twilight Cost: 0
 * Type: Site
 * Game Text: Dwelling. While the fellowship is at this site, followers cannot be transfered to a character.
 */
public class Card18_140 extends AbstractShadowsSite {
    public Card18_140() {
        super("Streets of Bree", 0, Direction.LEFT);
        addKeyword(Keyword.DWELLING);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new CantBeTransferredModifier(self, CardType.FOLLOWER));
}
}
