public class main {

    static String a = "Test text with var%1$s";

    public static void main(String[] args){
        System.out.println(String.format(a, "zero"));
    }
}
