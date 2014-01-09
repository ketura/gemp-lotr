package com.gempukku.lotro.db;

import java.util.Set;

public interface IpBanDAO {
    public Set<String> getIpBans();
    public Set<String> getIpPrefixBans();
    public void addIpBan(String ip);
    public void addIpPrefixBan(String ipPrefix);
}
