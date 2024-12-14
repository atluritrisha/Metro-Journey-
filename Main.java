import java.util.*;

public class Main {
    public static void main(String[] args) {
        GraphM g = new GraphM();
        g.createMetroMap();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n1. Display the Metro Map\n2. Display the Stations\n3. Compute the Trip Details\n4. Exit");
            System.out.print("\nEnter your choice : ");
            int ch = scanner.nextInt();
            scanner.nextLine();
            if (ch == 1) {
                g.displayMap();
            } else if (ch == 2) {
                g.displayStations();
            } else if (ch == 3) {
                String[] st = g.stationsInput().split(" ");
                List<String> arr = g.getInterchanges(g.getMinimumDistance(st[0], st[1]));
                int leng = arr.size();
                double dis = Double.parseDouble(arr.get(leng - 1));
                System.out.println("\n    Distance : " + dis + " KM");
                System.out.println("    Time     : " + g.time(dis));
                System.out.println("    Cost     : Rs. " + g.cost(dis));
                System.out.println("\n    Number of Interchanges : " + arr.get(leng - 2));
                System.out.println("\n---------------- Route ----------------\n");
                System.out.println("    Start  ==>  " + arr.get(0));
                for (int i = 1; i < leng - 3; i++) {
                    System.out.println("    " + arr.get(i));
                }
                System.out.println("    " + arr.get(leng - 3) + "  ==>  End");
                System.out.println("\n---------------------------------------\n");
            } else if (ch == 4) {
                System.out.println();
                break;
            } else {
                System.out.println("\nInvalid Choice");
            }
            System.out.print("Press Enter to Continue...");
            scanner.nextLine();
        }
        scanner.close();
    }
}
