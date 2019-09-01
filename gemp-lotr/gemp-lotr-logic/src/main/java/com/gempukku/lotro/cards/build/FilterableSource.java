package com.gempukku.lotro.cards.build;

import com.gempukku.lotro.common.Filterable;

public interface FilterableSource {
    Filterable getFilterable(ActionContext actionContext);
}
