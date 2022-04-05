class Menu {
    private final Base dataBase;
    final static int logout = 3;

    Menu (String dbName) {
        dataBase = new Base(dbName);
    }

    public void session() {
        int option;

        do {
            System.out.println("1. Create an account\n2. Log into account\n0. Exit");
            option = Scan.readInt();
            if (option == 1)
                createCard();
            else if (option == 2)
                option = authorisation();
        } while ( option != 0 );

        System.out.println("Bye!");
    }

    private void createCard() {
        String newCard;
        String pin;

        do {
            newCard = Generator.createCardNumber();
        } while (dataBase.findCard(newCard));
        System.out.println("\nYour card has been created\n" + "Your card number:\n" + newCard);

        pin = Generator.createPin();
        System.out.println("Your card PIN:\n" + pin  + "\n");

        dataBase.insertNewCard(newCard, pin);
    }

    private int authorisation() {
        String card = Scan.readString("\nEnter your card number:");
        String pin = Scan.readString("Enter your PIN:");

        boolean logIn = dataBase.logIn(card, pin);

        if (logIn)
            System.out.println("\nYou have successfully logged in!");
        else
            System.out.println("\nWrong card number or PIN!\n");

        while (logIn) {
            System.out.println("\n1. Balance\n2. Add income\n3. " +
                    "Do transfer\n4. Close account\n5. Log out\n0. Exit");
            switch (Scan.readInt()) {
                case 1:
                    System.out.println("\nBalance: " + dataBase.balance(card)); // print here?
                    break;
                case 2:
                    addIncome(card);
                    break;
                case 3:
                    transfer(card);
                    break;
                case 4:
                    closeAccount(card);
                    return logout;
                case 5:
                    System.out.println("You have successfully logged out!");
                    return logout;
                case 0:
                    return 0;
                default:
                    break;
            }
        }
        return logout;
    }

    private void addIncome(String card) {
        System.out.println("\nEnter income:");
        int income = Scan.readInt();
        if (income > 0) {
            if (dataBase.addIncome(card, income))
                System.out.println("Income was added!");
        }
        else
            System.out.println("Wrong income value!");
    }

    private void transfer(String card) {
        System.out.println("\nTransfer");
        String cardTo = Scan.readString("Enter card number:");

        if (cardTo.length() != 16 ||
                Generator.activateLuhnAlgorithm(Long.parseLong(cardTo.substring(0, 15)))
                != Long.parseLong(cardTo.substring(15)))
            System.out.println("Probably you made a mistake in the card number. Please try again!");
        else if (cardTo.equals(card))
            System.out.println("You can't transfer money to the same account!");
        else if (!dataBase.findCard(cardTo))
            System.out.println("Such a card does not exist.");
        else {
            System.out.println("Enter how much money you want to transfer:");
            int amount = Scan.readInt();
            if (dataBase.balance(card) < amount)
                System.out.println("Not enough money!");
            else {
                if (dataBase.doTransfer(card, cardTo, amount))
                    System.out.println("Success!");
            }
        }
    }

    private void closeAccount(String card) {
        if (dataBase.delete(card))
            System.out.println("\nThe account has been closed!\n");
    }

}
