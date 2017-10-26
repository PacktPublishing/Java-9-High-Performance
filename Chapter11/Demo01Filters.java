package com.packt.java9hp.ch11_newapis;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Demo01Filters {
    static List<Senator> senators = Senate.getSenateVotingStats();

    public static void main(String... args) {
        demo_filters1();
        demo_filters2();System.out.println();
        demo_flatMap();
        demo_findFirst();
        demo_findAny();
        demo_anyMatch();
        demo_allMatch();
        demo_noneMatch();
        demo_distinct();
        demo_limit();
        demo_max();
        demo_min();
        demo_takeWhile();
        demo_dropWhile();
    }

    private static void demo_filters1() {
        long c1 = senators.stream()
                .filter(s -> s.getParty() == "Party1")
                .count();
        System.out.println("Members of Party1: " + c1);

        long c2 = senators.stream()
                .filter(s -> s.getParty() == "Party2")
                .count();
        System.out.println("Members of Party2: " + c2);
        System.out.println("Members of the senate: " + (c1 + c2));
        System.out.println();

        int issue = 3;
        c1 = senators.stream()
                .filter(s -> s.getParty() == "Party1")  //has to be first to avoid unnecessary load
                .filter(s -> s.getVoteNo()[issue] != s.getVoteYes()[issue])
                .count();
        System.out.println("Members of Party1 who voted on Issue" + issue + ": " + c1);

        c2 = senators.stream()
                .filter(s -> s.getParty() == "Party2" && s.getVoteNo()[issue] != s.getVoteYes()[issue])
                .count();
        System.out.println("Members of Party2 who voted on Issue" + issue + ": " + c2);
        System.out.println("Members of the senate who voted on Issue" + issue + ": " + (c1 + c2));
        System.out.println();

        c1 = senators.stream()
                .filter(s -> s.getParty() == "Party1" && s.getVoteYes()[issue] == 1)
                .count();
        System.out.println("Members of Party1 who voted Yes on Issue" + issue + ": " + c1);

        c2 = senators.stream()
                .filter(s -> s.getParty() == "Party2" && s.getVoteYes()[issue] == 1)
                .count();
        System.out.println("Members of Party2 who voted Yes on Issue" + issue + ": " + c2);
        System.out.println("Members of the senate voted Yes on Issue" + issue + ": " + (c1 + c2));
    }

    private static long countAndPrint(List<Senator> senators, Predicate<Senator> pred1, Predicate<Senator> pred2, String prefix) {
        long c = senators.stream().filter(pred1::test).filter(pred2::test).count();
        System.out.println(prefix + c);
        return c;
    }

    private static void demo_filters2() {
        int issue = 3;

        Predicate<Senator> party1 = s -> s.getParty() == "Party1";
        Predicate<Senator> party2 = s -> s.getParty() == "Party2";
        Predicate<Senator> voted3 = s -> s.getVoteNo()[issue] != s.getVoteYes()[issue];
        Predicate<Senator> yes3 = s -> s.getVoteYes()[issue] == 1;

        long c1 = countAndPrint(senators, party1, s -> true, "Members of Party1: ");
        long c2 = countAndPrint(senators, party2, s -> true, "Members of Party2: ");
        System.out.println("Members of the senate: " + (c1 + c2));

        c1 = countAndPrint(senators, party1, voted3, "Members of Party1 who voted on Issue" + issue + ": ");
        c2 = countAndPrint(senators, party2, voted3, "Members of Party2 who voted on Issue" + issue + ": ");
        System.out.println("Members of the senate who voted on Issue" + issue + ": " + (c1 + c2));

        c1 = countAndPrint(senators, party1, yes3, "Members of Party1 who voted Yes on Issue" + issue + ": ");
        c2 = countAndPrint(senators, party2, yes3, "Members of Party2 who voted Yes on Issue" + issue + ": ");
        System.out.println("Members of the senate voted Yes on Issue" + issue + ": " + (c1 + c2));
    }

    private static void demo_flatMap() {
        long c1 = senators.stream()
                .flatMap(s -> s.getParty() == "Party1" ? Stream.of(s) : Stream.empty())
                .count();
        System.out.println("Members of Party1: " + c1);

        c1 = senators.stream()
                .flatMap(s -> Stream.ofNullable(s.getParty() == "Party1" ? s : null)) //Java 9
                .count();
        System.out.println("Members of Party1: " + c1);

        long c2 = senators.stream()
                .map(s -> s.getParty() == "Party2" ? Optional.of(s) : Optional.empty())
                .flatMap(o -> o.map(Stream::of).orElseGet(Stream::empty))
                .count();
        System.out.println("Members of Party2: " + c2);

        c2 = senators.stream()
                .map(s -> s.getParty() == "Party2" ? Optional.of(s) : Optional.empty())
                .flatMap(Optional::stream) //Java 9
                .count();
        System.out.println("Members of Party2: " + c2);
        System.out.println("Members of the senate: " + (c1 + c2));

    }

    private static void demo_findFirst() {
        senators.stream().filter(s -> s.getParty() == "Party1" && s.getVoteYes()[3] == 1)
                .findFirst().ifPresent(s -> System.out.println("First senator of Party1 found who voted Yes on issue 3: " + s.getName()));
    }

    private static void demo_findAny() {
        senators.stream().filter(s -> s.getVoteYes()[3] == 1)
                .findAny().ifPresent(s -> System.out.println("A senator found who voted Yes on issue 3: " + s));
    }

    private static void demo_anyMatch() {
        boolean found = senators.stream().anyMatch(s -> (s.getParty() == "Party1" && s.getVoteYes()[3] == 1));
        String res = found ? "At least one senator of Party1 voted Yes on issue 3"
                : "Nobody of Party1 voted Yes on issue 3";
        System.out.println(res);
    }

    private static void demo_allMatch() {
        boolean yes = senators.stream().allMatch(s -> (s.getParty() == "Party1" && s.getVoteYes()[3] == 1));
        String res = yes ? "All senators of Party1 voted Yes on issue 3"
                : "Not all senators of Party1 voted Yes on issue 3";
        System.out.println(res);
    }

    private static void demo_noneMatch() {
        boolean yes = senators.stream().noneMatch(s -> (s.getParty() == "Party1" && s.getVoteYes()[3] == 1));
        String res = yes ? "None of the senators of Party1 voted Yes on issue 3"
                : "Some of senators of Party1 voted Yes on issue 3";
        System.out.println(res);
    }

    private static void demo_distinct() {
        senators.stream().map(s -> s.getParty())
                .distinct().forEach(System.out::println);
    }

    private static void demo_limit() {
        System.out.println("These are the first 3 senators of Party1 in the list:");
        senators.stream().filter(s -> s.getParty() == "Party1")
                .limit(3)
                .forEach(System.out::println);
        System.out.println("These are the first 2 senators of Party2 in the list:");
        senators.stream().filter(s -> s.getParty() == "Party2")
                .limit(2)
                .forEach(System.out::println);
    }

    private static void demo_max() {
        senators.stream()
                .max(Comparator.comparing(Senate::timesVotedYes))
                .ifPresent(s -> System.out.println("A senator voted Yes most of times (" + Senate.timesVotedYes(s) + "): " + s));
    }

    private static void demo_min() {
        senators.stream()
                .min(Comparator.comparing(Senate::timesVotedYes))
                .ifPresent(s -> System.out.println("A senator voted Yes least of times (" + Senate.timesVotedYes(s) + "): " + s));
    }

    private static void demo_takeWhile() { //Java 9
        System.out.println("Here is count of times the first 5 senators voted Yes:");
        senators.stream().limit(5).forEach(s -> System.out.println(s + ": " + Senate.timesVotedYes(s)));
        System.out.println("Stop printing at a senator who voted Yes more than 4 times:");
        senators.stream().limit(5).takeWhile(s -> Senate.timesVotedYes(s) < 5)
                .forEach(s -> System.out.println(s + ": " + Senate.timesVotedYes(s)));
    }

    private static void demo_dropWhile() { //Java 9
        System.out.println("Here is count of times the first 5 senators voted Yes:");
        senators.stream().limit(5).forEach(s -> System.out.println(s + ": " + Senate.timesVotedYes(s)));
        System.out.println("Start printing at a senator who voted Yes more than 4 times:");
        senators.stream().limit(5).dropWhile(s -> Senate.timesVotedYes(s) < 5)
                .forEach(s -> System.out.println(s + ": " + Senate.timesVotedYes(s)));
        System.out.println("...");
    }
}