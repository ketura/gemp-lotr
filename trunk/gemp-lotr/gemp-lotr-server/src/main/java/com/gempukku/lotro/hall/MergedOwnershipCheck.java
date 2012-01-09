package com.gempukku.lotro.hall;

import com.gempukku.lotro.game.OwnershipCheck;

public class MergedOwnershipCheck implements OwnershipCheck {
    private OwnershipCheck[] _ownershipChecks;

    public MergedOwnershipCheck(OwnershipCheck... ownershipChecks) {
        _ownershipChecks = ownershipChecks;
    }

    @Override
    public int getItemCount(String blueprintId) {
        int count = 0;
        for (OwnershipCheck ownershipCheck : _ownershipChecks)
            count += ownershipCheck.getItemCount(blueprintId);
        return count;
    }
}
