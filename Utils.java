public class Utils {
    public static String encrypt(String input) {
        StringBuilder sb = new StringBuilder();
        for (char c : input.toCharArray()) {
            sb.append((char)(c + 3));
        }
        return sb.toString();
    }
}
