package com.packt.java9hp.ch11_newapis;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Demo05NewStreamAPI {
    public static void main(String... args) {
        demo01();
        demo02();
        demo03();
    }

    public static void demo01() {
        List<Senator> senators = Senate.getSenateVotingStats();

        long c1 = senators.stream()
                .flatMap(s -> Stream.ofNullable(s.getParty() == "Party1" ? s : null))
                .count();
        System.out.println("OfNullable: Members of Party1: " + c1);

        long c2 = senators.stream()
                .map(s -> s.getParty() == "Party2" ? Optional.of(s) : Optional.empty())
                .flatMap(Optional::stream)
                .count();
        System.out.println("Optional.stream(): Members of Party2: " + c2);

        senators.stream().limit(5).takeWhile(s -> Senate.timesVotedYes(s) < 5)
                .forEach(s -> System.out.println("takeWhile(<5): " + s + ": " + Senate.timesVotedYes(s)));

        senators.stream().limit(5).dropWhile(s -> Senate.timesVotedYes(s) < 5)
                .forEach(s -> System.out.println("dropWhile(<5): " + s + ": " + Senate.timesVotedYes(s)));
    }

    private static void demo02(){
        String result = IntStream.iterate(1, i -> i + 2)
                .limit(5)
                .mapToObj(i -> String.valueOf(i))
                .collect(Collectors.joining(", "));
        System.out.println("Iterate: " + result);
    }

    private static void demo03(){
        String result = IntStream.iterate(1, i -> i < 11, i -> i + 2)
                .mapToObj(i -> String.valueOf(i))
                .collect(Collectors.joining(", "));
        System.out.println("Iterate: " + result);
    }

}
