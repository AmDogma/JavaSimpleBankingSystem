
class Scan {
    static int readInt() {
        var sc = new java.util.Scanner(System.in);
        try {
            return sc.nextInt();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    static String readString(String message) {
        java.util.Scanner sc = new java.util.Scanner(System.in);

        System.out.println(message);
        return sc.next();
    }

}
