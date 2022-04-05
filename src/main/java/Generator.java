import java.util.Random;

class Generator {
    static String createCardNumber() {
        Random random = new Random();
        long card = 0;

        while (card < 99999999L)
            card = Math.abs(random.nextLong() % 999999999L);
        card = 400000000000000L + card;
        card = card * 10 + activateLuhnAlgorithm(card);
        return String.valueOf(card);
    }

    static long activateLuhnAlgorithm(Long num) {
        long res = 0L;
        long temp;

        for (int i = 0; num != 0; i++,  num /= 10) {
            temp = num % 10;

            if (i % 2 == 0) {
                temp *= 2;
                if (temp > 9)
                    temp -= 9;
            }
            res += temp;
        }
        res = res % 10;
        if (res != 0)
            res = 10 - res;
        return res;
    }

    static String createPin() {
        Random random = new Random();
        String pin = Integer.toString(random.nextInt(9999));

        while (pin.length() < 3)
            pin = Integer.toString(random.nextInt(9999));
        if (pin.length() == 3)
            pin = "0" + pin;
        return pin;
    }



}
