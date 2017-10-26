package com.packt.java9hp.ch11_newapis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Senate {
    public static List<Senator> getSenateVotingStats(){
        List<Senator> results = new ArrayList<>();
        results.add(new Senator("Senator1", "Party1", new int[]{1,0,0,0,0,0,1,0,0,1}, new int[]{0,1,0,1,0,0,0,0,1,0}));
        results.add(new Senator("Senator2", "Party2", new int[]{0,1,0,1,0,1,0,1,0,0}, new int[]{1,0,1,0,1,0,0,0,0,1}));
        results.add(new Senator("Senator3", "Party1", new int[]{1,0,0,0,0,0,1,0,0,1}, new int[]{0,1,0,1,0,0,0,0,1,0}));
        results.add(new Senator("Senator4", "Party2", new int[]{1,0,1,0,1,0,1,0,0,1}, new int[]{0,1,0,1,0,0,0,0,1,0}));
        results.add(new Senator("Senator5", "Party1", new int[]{1,0,0,1,0,0,0,0,0,1}, new int[]{0,1,0,0,0,0,1,0,1,0}));

        IntStream.rangeClosed(6, 98).forEach(i -> {
            double r1 = Math.random();
            String name = "Senator" + i;
            String party = r1 > 0.5 ? "Party1" : "Party2";
            int[] voteNo = new int[10];
            int[] voteYes = new int[10];
            IntStream.rangeClosed(0, 9).forEach(j -> {
                double r2 = Math.random();
                voteNo[j] = r2 > 0.4 ? 0 : 1;
                voteYes[j] = r2 < 0.6 ? 0 : 1;
            });
            results.add(new Senator(name, party, voteYes, voteNo));
        });
        results.add(new Senator("Senator99", "Party1", new int[]{0,0,0,0,0,0,0,0,0,0}, new int[]{1,1,1,1,1,1,1,1,1,1}));
        results.add(new Senator("Senator100", "Party2",new int[]{1,1,1,1,1,1,1,1,1,1}, new int[]{0,0,0,0,0,0,0,0,0,0}));
        return results;
    }

    public static int timesVotedYes(Senator senator){
        return Arrays.stream(senator.getVoteYes()).sum();
    }
}
