import java.io.*;

public class Main {
    public static void main(String[] args) {


        SuffixTree tree = new SuffixTree(args[0], args[1]);
        System.out.println(tree.getLongestCommonSubstring());

//        try {
//            BufferedReader in = new BufferedReader(new FileReader(new File("./in.txt")));
//            String n = in.readLine();
//            String st1 = in.readLine();
//            String st2 = in.readLine();
//            SuffixTree tree = new SuffixTree(st1, st2);
//            BufferedWriter fw = new BufferedWriter(new FileWriter(new File("./out.txt")));
//            fw.write(tree.getLongestCommonSubstring());
//            fw.close();
//
//           }
//            catch (Exception e){
//                System.out.println("");
//            }
    }
}
