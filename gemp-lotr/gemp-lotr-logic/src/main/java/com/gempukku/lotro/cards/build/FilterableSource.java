package com.gempukku.lotro.cards.build;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.DefaultGame;

public interface FilterableSource {
    Filterable getFilterable(DefaultActionContext<DefaultGame> actionContext);
}
