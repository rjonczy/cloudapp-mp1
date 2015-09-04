import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class MP1 {
    Random generator;
    String userName;
    String inputFileName;
    String delimiters = " \t,;.?!-:@[](){}_*/";
    String[] stopWordsArray = {"i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours",
            "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its",
            "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that",
            "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having",
            "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while",
            "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before",
            "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again",
            "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each",
            "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than",
            "too", "very", "s", "t", "can", "will", "just", "don", "should", "now"};

    void initialRandomGenerator(String seed) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA");
        messageDigest.update(seed.toLowerCase().trim().getBytes());
        byte[] seedMD5 = messageDigest.digest();

        int ile = seedMD5.length;

        long longSeed = 0;
        for (int i = 0; i < seedMD5.length; i++) {
            longSeed += ((long) seedMD5[i] & 0xffL) << (8 * i);
        }

        this.generator = new Random(longSeed);
    }

    Integer[] getIndexes() throws NoSuchAlgorithmException {
        Integer n = 10000;
        Integer number_of_lines = 50000;
        Integer[] ret = new Integer[n];
        this.initialRandomGenerator(this.userName);
        for (int i = 0; i < n; i++) {
            ret[i] = generator.nextInt(number_of_lines);
        }
        return ret;
    }

    public MP1(String userName, String inputFileName) {
        this.userName = userName;
        this.inputFileName = inputFileName;
    }

    //sort desc by Comparator
    private static Map<String, Integer> sortByComparator(Map<String, Integer> unsortMap) {

        // Convert Map to List
        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(unsortMap.entrySet());

        // Sort list with comparator, to compare the Map values
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {

            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {

                int compResult = o2.getValue().compareTo(o1.getValue());

                //if 2 keys have the same value than compare strings
                if(compResult == 0)
                    return o1.getKey().compareTo(o2.toString());

                return compResult;

            }
        });

        // Convert sorted map back to a Map
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Iterator<Map.Entry<String, Integer>> it = list.iterator(); it.hasNext();) {
            Map.Entry<String, Integer> entry = it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }



    public String[] process() throws Exception {

        String[] ret = new String[20];
        List<String> lines = new ArrayList<String>();
        List<String> words = new ArrayList<String>();

        Map<String, Integer> wordsCountMap = new HashMap<String, Integer>();

        //read file into lines array
        BufferedReader br = new BufferedReader(new FileReader(this.inputFileName));
        String line;
        while ((line = br.readLine()) != null) {
            lines.add(line);
        }

        //get indexes and iterate thru
        Integer[] indexes = this.getIndexes();

        for (Integer index : indexes) {

            //get line from
            line = lines.get(index);

            //lowercase and trim (remove any tailing and leading spaces)
            line = line.toLowerCase().trim();
            StringTokenizer st = new StringTokenizer(line, delimiters);

            //add tokens/words to words list
            String word;
            while (st.hasMoreTokens()) {
                word = st.nextToken();

                //add word to words list if token doesn't exists in stopWordsArray
                if(!Arrays.asList(stopWordsArray).contains(word)) {
                    words.add(word);
                    //check how many times word exists in map
                    Integer count = wordsCountMap.get(word);
                    if(count == null) {
                        count = 0;
                    }
                    wordsCountMap.put(word, (count+1));

                }
            }

        }

        //sort map
        Map<String, Integer> wordsCountSortedMap = sortByComparator(wordsCountMap);

        int i = 0;
        for (Map.Entry<String, Integer> entry : wordsCountSortedMap.entrySet()) {
            if(i > ret.length - 1) break;
            ret[i] = entry.getKey();
            i++;
        }

        return ret;
    }

    public static void main(String[] args) throws Exception {

        if (args.length < 0){
            System.out.println("MP1 <User ID>");
        }
        else {
            String userName = args[0];
            String inputFileName = "./input.txt";

            MP1 mp = new MP1(userName, inputFileName);
            String[] topItems = mp.process();

            for (String s : topItems) {
                System.out.println(s);
            }
        }
    }
}
