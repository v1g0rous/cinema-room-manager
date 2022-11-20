import java.util.Scanner;

public class Cinema {
    static final int MAX_SEATS = 60;
    static final String WHITE_SPACE = " ";
    static final String AVAILABLE_SEAT = "S";
    static final String BOOKED_SEAT = "B";
    static int purchasedSeats = 0;
    static int currentIncome = 0;
    static int rowsNumber = 0;
    static int seatsNumberPerRow = 0;
    static String[][] cinemaHall;

    public static void main(String[] args) {
        createCinemaHall();
        cinemaController();
    }

    /**
     * Creates a cinema hall with the given size.
     * Available seats marked as "S"
     */
    public static void createCinemaHall() {
        int auxRow = 1;
        int auxSeat = 1;

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the number of rows:");
        rowsNumber = scanner.nextInt();

        System.out.println("Enter the number of seats in each row:");
        seatsNumberPerRow = scanner.nextInt();

        int totalRows = rowsNumber + auxRow;
        int totalSeats = seatsNumberPerRow + auxSeat;

        cinemaHall = new String[totalRows][totalSeats];

        for (int row = 0; row < totalRows; row++) {
            for (int seat = 0; seat < totalSeats; seat++) {
                if (row == 0 && seat == 0) {
                    cinemaHall[row][seat] = WHITE_SPACE;
                } else if (row == 0 && seat > 0) {
                    cinemaHall[row][seat] = String.valueOf(seat);
                } else if (seat == 0) {
                    cinemaHall[row][seat] = String.valueOf(row);
                } else {
                    cinemaHall[row][seat] = AVAILABLE_SEAT;
                }
            }
        }
    }

    /**
     * Calls needed command depending on
     * menu item chosen by User
     */
    public static void cinemaController() {
        int selectedMenuItem = selectMenuItem();
        switch (selectedMenuItem) {
            case 0 -> { return; }
            case 1 -> showSeats();
            case 2 -> buyTicket();
            case 3 -> showStats();
        }
    }

    /**
     * Displays a menu where user
     * can choose needed command
     */
    public static int selectMenuItem() {
        System.out.println();
        System.out.println("1. Show the seats");
        System.out.println("2. Buy a ticket");
        System.out.println("3. Statistics");
        System.out.println("0. Exit");

        Scanner scanner = new Scanner(System.in);
        int menuItem = scanner.nextInt();
        return menuItem;
    }

    /**
     * Visualize seats of cinema hall
     * by outputting them to the console
     */
    public static void showSeats() {
        System.out.println("Cinema:");
        for (int i = 0; i < cinemaHall.length; i++) {
            for (int j = 0; j < cinemaHall[i].length; j++) {
                System.out.print(cinemaHall[i][j] + WHITE_SPACE);
            }
            System.out.println();
        }
        cinemaController();
    }

    /**
     * Books a seat
     * by given row and seat number
     * Shows ticket price for the seat
     */
    public static void buyTicket() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a row number:");
        int rowNumberToBook = scanner.nextInt();
        System.out.println("Enter a seat number in that row:");
        int seatNumberToBook = scanner.nextInt();
        System.out.println();

        String error = validateTicket(rowNumberToBook, seatNumberToBook);

        if (error.length() > 0) {
            System.out.println(error);
            buyTicket();
        } else {
            int ticketPrice = calculateTicketPrice(rowNumberToBook);
            currentIncome += ticketPrice;
            System.out.println("Ticket price: " + "$" + ticketPrice);
            setBookedSeat(rowNumberToBook,seatNumberToBook);
            cinemaController();
        }
    }

    /**
     * Shows Cinema Hall statistics
     */
    public static void showStats() {
        System.out.printf("Number of purchased tickets: %s%n", getPurchasedSeats());
        System.out.printf("Percentage: %.2f%%%n", getBookedPercentage());
        System.out.printf("Current income: $%s%n", getCurrentIncome());
        System.out.printf("Total income: $%s%n", getTotalIncome(rowsNumber,seatsNumberPerRow));
        cinemaController();
    }

    /**
     * Checks if chosen seat and row are reservable
     */
    public static String validateTicket(int rowNumberToBook, int seatNumberToBook) {
        String error = "";
        Boolean seatOutOfBound = (seatNumberToBook > cinemaHall[rowsNumber].length - 1);
        Boolean rowOutOfBound = (rowNumberToBook > cinemaHall.length - 1);
        Boolean zeroSeatOrRow = rowNumberToBook == 0 || seatNumberToBook == 0;

        if (seatOutOfBound || rowOutOfBound || zeroSeatOrRow) {
            error = "Wrong input!";
        } else if (cinemaHall[rowNumberToBook][seatNumberToBook] == BOOKED_SEAT) {
            error = "That ticket has already been purchased!";
        }
        return error;
    }

    /**
     * Calculates ticket price by given rowNumber
     */
    public static int calculateTicketPrice(int rowNumberToBook) {
        int ticketPrice = 0;
        if (getTotalNumberOfSeats() > 0 && getTotalNumberOfSeats() < MAX_SEATS) {
            ticketPrice = getTicketRegularPrice();
        } else {
            ticketPrice = isFrontRow(rowNumberToBook) ? getTicketRegularPrice() : getTicketDiscountPrice();
        }
        return ticketPrice;
    }

    /**
     * Books a seat and increments purchasedSeats var for statistics
     */
    public static void setBookedSeat(int rowNumberToBook, int seatNumberToBook) {
        cinemaHall[rowNumberToBook][seatNumberToBook] = BOOKED_SEAT;
        purchasedSeats++;
    }

    /**
     * Checks if given rowNumber is situated in front or back half of Cinema Hall
     */
    public static boolean isFrontRow(int rowNumberToBook){
        return rowNumberToBook > 0 && rowNumberToBook <= rowsNumber / 2;
    }

    /**
     * Calculates total number of seats
     */
    public static int getTotalNumberOfSeats() {
        return rowsNumber * seatsNumberPerRow;
    }

    /**
     * Calculates percentage of booked seats
     */
    public static double getBookedPercentage() {
        return getPurchasedSeats() * 100.0 / getTotalNumberOfSeats();
    }

    /**
     * Calculates total possible income for Cinema Hall of given size
     */
    public static int getTotalIncome(int rowsNumber, int seatsNumberPerRow) {
        int totalSeatsNumber = getTotalNumberOfSeats();
        int ticketRegularPrice = getTicketRegularPrice();
        int ticketDiscountPrice = getTicketDiscountPrice();
        int frontHalfRows = rowsNumber / 2;
        int backHalfRows = rowsNumber - frontHalfRows;

        if (totalSeatsNumber > 0 && totalSeatsNumber <= 60) {
            return totalSeatsNumber * ticketRegularPrice;
        } else if (totalSeatsNumber > 60) {
            return (frontHalfRows * seatsNumberPerRow * ticketRegularPrice) +
                    (backHalfRows * seatsNumberPerRow * ticketDiscountPrice);
        }
        return 0;
    }

    /**
     * Get regular price for ticket
     */
    public static int getTicketRegularPrice() {
        int ticketRegularPrice = 10;
        return ticketRegularPrice;
    }

    /**
     * Get discount price for ticket
     */
    public static int getTicketDiscountPrice() {
        int ticketDiscountPrice = 8;
        return ticketDiscountPrice;
    }

    /**
     * Get total number of purchased seats
     */
    public static int getPurchasedSeats() {
        return purchasedSeats;
    }

    /**
     * Get current income
     */
    public static int getCurrentIncome() {
        return currentIncome;
    }
}