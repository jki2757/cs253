package edu.emory.cs.trie.autocomplete;

import edu.emory.cs.trie.TrieNode;

import java.util.*;

public class AutocompleteKim extends Autocomplete<List<String>> {
    public AutocompleteKim(String dict_file, int max) {
        super(dict_file, max);
    }

    public static void main(String[] args) {
        List<String> candidates;
        String dict_path = "src/main/resources/dict.txt";
        AutocompleteKim tmp = new AutocompleteKim(dict_path, 50);
        Scanner reader = new Scanner(System.in);
        while (true) {
            System.out.println("Enter prefix: ");
            String prefix = reader.nextLine();
            candidates = tmp.getCandidates(prefix);
            System.out.println("Candidates: " + candidates);
            if (candidates.isEmpty() != true) {
                System.out.println("Choose a word from candidates: ");//
                String chosen = reader.nextLine();
                tmp.pickCandidate(prefix, chosen);
            }
        }
    }

    @Override
    public List<String> getCandidates(String prefix) {
//        // TODO: must be modified
//        Comparator<String> stringLengthComparator = new Comparator<String>() {
//            @Override
//            public int compare(String s1, String s2) {
//                int val = 0;
//                    val = s1.length() - s2.length();
//
//                if (s1.length() == s2.length()) {
//                    val = s1.compareTo(s2);
//                }
//                return val;
//            }
//        };

        Queue<String> queue = new LinkedList<>();
        List<String> tmp = new ArrayList<String>();
        List<String> result = new ArrayList<String>();
        TrieNode<List<String>> ptNode;

        prefix = prefix.replaceAll("\\s", "");

        if ((ptNode = find(prefix)) == null) {
            System.out.println("prefix unmatched");
        } else {
            result = bfs(ptNode, prefix, queue, tmp, result);
            System.out.println("Queue built.");
        }
        return result;
    }

    public List<String> bfs(TrieNode<List<String>> ptNode, String prefix, Queue<String> queue, List<String> tmp, List<String> result) {

        if (ptNode.isEndState() == true && tmp.size() != 0) {
            if (result.contains(prefix) == false)
                tmp.add(prefix);

            queue.add(prefix);
            return null;

        } else if (ptNode.isEndState() == false && tmp.size() != 0) {
            queue.add(prefix);
            return null;

        } else if (ptNode.isEndState() == true && tmp.size() == 0) {
            if (ptNode.getValue() != null) {

                for (int i = 0; i < ptNode.getValue().size(); i++)
                    result.add(ptNode.getValue().get(i));
            }
            tmp.add(prefix);

        } else if (ptNode.isEndState() == false && tmp.size() == 0) {
            if (ptNode.getValue() != null) {

                for (int i = 0; i < ptNode.getValue().size(); i++) {
                    if (result.contains(prefix)) continue;
                    result.add(ptNode.getValue().get(i));
                }
            }
            tmp.add(prefix);
        }
        while (true) {
            // sort the list before hand using ptNode.getChildrenMap().keySet()
            //==============================================================================================
            //Set<Character> s = ptNode.getChildrenMap().keySet();
            List<Character> aList = new ArrayList<Character>(ptNode.getChildrenMap().keySet());
            Collections.sort(aList);
            for (int i = 0; i < aList.size(); i++)
                bfs(ptNode.getChildrenMap().get(aList.get(i)), prefix + aList.get(i), queue, tmp, result);
            //==============================================================================================
//            for(Character letter : ptNode.getChildrenMap().keySet()) {
//                bfs(ptNode.getChildrenMap().get(letter), prefix + letter, queue, tmp, result);
//            }
            if (result.size() >= getMax() || queue.isEmpty()) break;
            prefix = queue.poll();
            ptNode = find(prefix);
        }

        for (int k = 0; k < tmp.size(); k++) {
            if (result.contains(tmp.get(k))) continue;
            result.add(tmp.get(k));
        }
        return result;
    }

    @Override
    public void pickCandidate(String prefix, String candidate) {
        // TODO: must be filled
        candidate = candidate.replaceAll("\\s", "");
        TrieNode<List<String>> ptNode = find(prefix);
        List<String> list = new ArrayList<String>();
        if (find(candidate) != null && candidate.startsWith(prefix) == true && find(candidate).isEndState()) {
            if (ptNode.getValue() != null) {
                ptNode.getValue().remove(candidate);
                ptNode.getValue().add(0, candidate);
            } else {
                list.add(candidate);
                ptNode.setValue(list);
            }
        } else {
            System.out.println("Chosen Candidate does not exist.");
        }
    }
}