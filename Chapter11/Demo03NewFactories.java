package com.packt.java9hp.ch11_newapis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Map.entry;

public class Demo03NewFactories {
    public static void main(String... args) {
        demo_java8_list();
        demo_java8_set();
        demo_java8_map();
        demo_java9();
    }

    private static void demo_java8_list() {

        List<String> list = new ArrayList<>();
        list.add("unmodifiableList1: Life");
        list.add(" is");
        list.add(" good! ");
        list.add(null);
        list.add("\n\n");
        List<String> unmodifiableList1 = Collections.unmodifiableList(list);
        //unmodifiableList1.add(" Well..."); //UnsupportedOperationException
        //unmodifiableList1.set(2, " sad."); //UnsupportedOperationException
        unmodifiableList1.stream().forEach(System.out::print);

        list.set(2, " sad. ");
        list.set(4, " ");
        list.add("Well...\n\n");
        unmodifiableList1.stream().forEach(System.out::print);

        List<String> list2 =
                Arrays.asList("unmodifiableList2: Life", " is", " good! ", null, "\n\n");
        List<String> unmodifiableList2 = Collections.unmodifiableList(list2);
        //unmodifiableList2.add(" Well..."); //UnsupportedOperationException
        //unmodifiableList2.set(2, " sad."); //UnsupportedOperationException
        unmodifiableList2.stream().forEach(System.out::print);

        list2.set(2, " sad. ");
        //list2.add("Well...\n\n");  //UnsupportedOperationException
        unmodifiableList2.stream().forEach(System.out::print);

        List<String> immutableList1 =
                Collections.unmodifiableList(new ArrayList<>() {{
                    add("immutableList1: Life");
                    add(" is");
                    add(" good! ");
                    add(null);
                    add("\n\n");
                }});
        //immutableList1.set(2, " sad.");  //UnsupportedOperationException
        //immutableList1.add("Well...\n\n");  //UnsupportedOperationException
        immutableList1.stream().forEach(System.out::print);

        List<String> immutableList2 =
                Collections.unmodifiableList(Stream.of("immutableList2: Life", " is", " good! ", null, "\n\n")
                        .collect(Collectors.toList()));
        //immutableList2.set(2, " sad.");  //UnsupportedOperationException
        //immutableList2.add("Well...\n\n");  //UnsupportedOperationException
        immutableList2.stream().forEach(System.out::print);

        List<String> immutableList3 = Stream.of("immutableList3: Life", " is", " good! ", null, "\n\n")
                .collect(Collectors.collectingAndThen(Collectors.toList(),
                        Collections::unmodifiableList));
        //immutableList3.set(2, " sad.");  //UnsupportedOperationException
        //immutableList3.add("Well...\n\n");  //UnsupportedOperationException
        immutableList3.stream().forEach(System.out::print);
    }

    private static void demo_java8_set() {

        Set<String> set = new HashSet<>();
        set.add("unmodifiableSet1: Life");
        set.add(" is");
        set.add(" good! ");
        set.add(null);
        Set<String> unmodifiableSet1 = Collections.unmodifiableSet(set);
        //unmodifiableSet1.remove(" good! "); //UnsupportedOperationException
        //unmodifiableSet1.add("...Well..."); //UnsupportedOperationException
        unmodifiableSet1.stream().forEach(System.out::print);
        System.out.println("\n");

        set.remove(" good! ");
        set.add("...Well...");
        unmodifiableSet1.stream().forEach(System.out::print);
        System.out.println("\n");

        Set<String> set2 = new HashSet<>(Arrays.asList("unmodifiableSet2: Life", " is", " good! ", null));
        Set<String> unmodifiableSet2 = Collections.unmodifiableSet(set2);
        //unmodifiableSet2.remove(" good! "); //UnsupportedOperationException
        //unmodifiableSet2.add("...Well..."); //UnsupportedOperationException
        unmodifiableSet2.stream().forEach(System.out::print);
        System.out.println("\n");

        set2.remove(" good! ");
        set2.add("...Well...");
        unmodifiableSet2.stream().forEach(System.out::print);
        System.out.println("\n");

        Set<String> immutableSet1 =
                Collections.unmodifiableSet(new HashSet<>() {{
                    add("immutableSet1: Life");
                    add(" is");
                    add(" good! ");
                    add(null);
                }});
        //immutableSet1.remove(" good! "); //UnsupportedOperationException
        //immutableSet1.add("...Well..."); //UnsupportedOperationException
        immutableSet1.stream().forEach(System.out::print);
        System.out.println("\n");

        Set<String> immutableSet2 =
                Collections.unmodifiableSet(Stream.of("immutableSet2: Life", " is", " good! ", null)
                        .collect(Collectors.toSet()));
        //immutableSet2.remove(" good!"); //UnsupportedOperationException
        //immutableSet2.add("...Well..."); //UnsupportedOperationException
        immutableSet2.stream().forEach(System.out::print);
        System.out.println("\n");

        Set<String> immutableSet3 = Stream.of("immutableSet3: Life", " is", " good! ", null)
                .collect(Collectors.collectingAndThen(Collectors.toSet(),
                        Collections::unmodifiableSet));
        //immutableList5.set(2, "sad.");  //UnsupportedOperationException
        //immutableList5.add("Well...");  //UnsupportedOperationException
        immutableSet3.stream().forEach(System.out::print);
        System.out.println("\n");
    }

    private static void demo_java8_map() {

        Map<Integer, String> map = new HashMap<>();
        map.put(1, "unmodifiableleMap: Life");
        map.put(2, " is");
        map.put(3, " good! ");
        map.put(4, null);
        map.put(5, "\n\n");
        Map<Integer, String> unmodifiableleMap = Collections.unmodifiableMap(map);
        //unmodifiableleMap.put(3, " sad."); //UnsupportedOperationException
        //unmodifiableleMap.put(6, "Well...\n\n");  //UnsupportedOperationException
        unmodifiableleMap.values().stream().forEach(System.out::print);

        map.put(3, " sad. ");
        map.put(4, "");
        map.put(5, "");
        map.put(6, "Well...\n\n");
        unmodifiableleMap.values().stream().forEach(System.out::print);

        Map<Integer, String> immutableMap1 =
                Collections.unmodifiableMap(new HashMap<>() {{
                    put(1, "immutableMap1: Life");
                    put(2, " is");
                    put(3, " good! ");
                    put(4, null);
                    put(5, "\n\n");
                }});
        //immutableMap1.put(3, " sad. "); //UnsupportedOperationException
        //immutableMap1.put(6, "Well...\n\n");  //UnsupportedOperationException
        immutableMap1.values().stream().forEach(System.out::print);

        String[][] mapping = new String[][]{{"1", "immutableMap2: Life"}, {"2", " is"}, {"3", " good! "}, {"4", null}, {"5", "\n\n"}};

        Map<Integer, String> immutableMap2 =
                Collections.unmodifiableMap(Arrays.stream(mapping)
                        .collect(Collectors.toMap(a -> Integer.valueOf(a[0]), a -> a[1] == null ? "" : a[1])));
        //The problem is toMap() invokes the underlying Map implementation being built's merge() function which does not allow values to be null
        immutableMap2.values().stream().forEach(System.out::print);

        mapping = new String[][]{{"1", "immutableMap3: Life"}, {"2", " is"}, {"3", " good! "}, {"5", "\n\n"}};
        Map<Integer, String> immutableMap3 =
                Collections.unmodifiableMap(Arrays.stream(mapping)
                        .collect(Collectors.toMap(a -> Integer.valueOf(a[0]), a -> a[1])));
        //immutableMap3.put(3, " sad."); //UnsupportedOperationException
        //immutableMap3.put(6, "Well...\n\n");  //UnsupportedOperationException
        immutableMap3.values().stream().forEach(System.out::print);

        mapping[0][1] = "immutableMap4: Life";

        Map<Integer, String> immutableMap4 = Arrays.stream(mapping)
                .collect(Collectors.collectingAndThen(Collectors.toMap(a -> Integer.valueOf(a[0]), a -> a[1]),
                        Collections::unmodifiableMap));
        //immutableMap4.put(3, " sad."); //UnsupportedOperationException
        //immutableMap4.put(6, "Well...\n\n");  //UnsupportedOperationException
        immutableMap4.values().stream().forEach(System.out::print);
    }

    private static void demo_java9() {
        List<String> immutableList = List.of("immutableList: Life", " is", " is", " good!\n\n");//, null);
        //immutableList.set(2, "sad.");  //UnsupportedOperationException
        //immutableList.add("Well...\n");  //UnsupportedOperationException
        immutableList.stream().forEach(System.out::print);

        Set<String> immutableSet = Set.of("immutableSet: Life", " is", " good!");//, " is" , null);
        //immutableSet.remove(" good!\n\n"); //UnsupportedOperationException
        //immutableSet.add("...Well...\n\n"); //UnsupportedOperationException
        immutableSet.stream().forEach(System.out::print);
        System.out.println("\n");

        Map<Integer, String> immutableMap = Map.of(1, "immutableMap: Life", 2, " is", 3, " good!");//, 4, null);
        //immutableMap.put(3, " sad."); //UnsupportedOperationException
        //immutableMap.put(4, "Well...\n\n");  //UnsupportedOperationException
        immutableMap.values().stream().forEach(System.out::print);
        System.out.println("\n");

        Map.Entry<Integer, String> entry1 = Map.entry(1, "immutableMap2: Life");
        Map.Entry<Integer, String> entry2 = Map.entry(2, " is");
        Map.Entry<Integer, String> entry3 = Map.entry(3, " good!");
        Map<Integer, String> immutableMap2 = Map.ofEntries(entry1, entry2, entry3);
        immutableMap2.values().stream().forEach(System.out::print);
        System.out.println("\n");

        Map<Integer, String> immutableMap3 = Map.ofEntries(entry(1, "immutableMap3: Life"), entry(2, " is"), entry(3, " good!"));
        immutableMap3.values().stream().forEach(System.out::print);
        System.out.println("\n");

        Set<String> immutableSetEmpty = Set.of();
        List<String> immutableListEmpty = List.of();
        Map<Integer, String> immutableMapEmpty1 = Map.of();
        Map<Integer, String> immutableMapEmpty2 = Map.ofEntries();
    }

}
