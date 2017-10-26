package com.packt.java9hp.ch11_newapis;

public class Senator {
    private int[] voteYes, voteNo;
    private String name, party;

    public Senator(String name, String party, int[] voteYes, int[] voteNo) {
        this.voteYes = voteYes;
        this.voteNo = voteNo;
        this.name = name;
        this.party = party;
    }

    public int[] getVoteYes() {
        return voteYes;
    }

    public int[] getVoteNo() {
        return voteNo;
    }

    public String getName() {
        return name;
    }

    public String getParty() {
        return party;
    }

    public String toString() {
        return getName() + ", P" + getParty().substring(getParty().length() - 1);
    }
}
