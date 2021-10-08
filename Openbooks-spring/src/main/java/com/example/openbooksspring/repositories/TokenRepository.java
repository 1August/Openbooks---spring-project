package com.example.openbooksspring.repositories;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TokenRepository {
    private final List<String> blackListTokens = new ArrayList<>();

    public boolean hasInBlackList(String token){ return blackListTokens.contains(token); }

    public boolean addTokenToBlackList(String token){ return (!hasInBlackList(token) && blackListTokens.add(token)); }
    public boolean removeTokenFromBlackList(String token){ return (!hasInBlackList(token) || blackListTokens.remove(token)); }

    public List<String> getBlackListTokens() { return blackListTokens; }
}
