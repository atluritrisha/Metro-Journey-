import java.util.*;
import java.io.*;

public class GraphM {

    class Vertex {
        Map<String, Double> nbrs;

        public Vertex() {
            nbrs = new HashMap<>();
        }
    }

    private Map<String, Vertex> vtces;
    private List<String> stations;
    private List<String> lines;

    public GraphM() {
        vtces = new HashMap<>();
        stations = new ArrayList<>();
        lines = new ArrayList<>();
        lines.add("Blue Line");
        lines.add("Red Line");
        lines.add("Green Line");
        try {
            Scanner scanner = new Scanner(new File("edges.csv"));
            while (scanner.hasNextLine()) {
                String row = scanner.nextLine().trim();
                if (!row.isEmpty()) {
                    if (row.contains(",")) {
                        stations.add(row.split(",")[0]);
                    } else {
                        stations.add(row);
                    }
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public int numVertices() {
        return vtces.size();
    }

    public boolean containsVertex(String vName) {
        return vtces.containsKey(vName);
    }

    public void addVertex(String vName) {
        vtces.put(vName, new Vertex());
    }

    public void removeVertex(String vName) {
        Vertex vtx = vtces.get(vName);
        for (String key : vtx.nbrs.keySet()) {
            Vertex nbrVtx = vtces.get(key);
            nbrVtx.nbrs.remove(vName);
        }
        vtces.remove(vName);
    }

    public int numEdges() {
        int count = 0;
        for (String key : vtces.keySet()) {
            Vertex vtx = vtces.get(key);
            count += vtx.nbrs.size();
        }
        return count / 2;
    }

    public boolean containsEdge(String vName1, String vName2) {
        return vtces.containsKey(vName1) && vtces.containsKey(vName2) && vtces.get(vName1).nbrs.containsKey(vName2);
    }

    public void addEdge(String vName1, String vName2, double value) {
        if (!vtces.containsKey(vName1) || !vtces.containsKey(vName2) || vtces.get(vName1).nbrs.containsKey(vName2)) {
            return;
        }
        vtces.get(vName1).nbrs.put(vName2, value);
        vtces.get(vName2).nbrs.put(vName1, value);
    }

    public void removeEdge(String vName1, String vName2) {
        if (!containsEdge(vName1, vName2)) {
            return;
        }
        vtces.get(vName1).nbrs.remove(vName2);
        vtces.get(vName2).nbrs.remove(vName1);
    }

    public void displayMap() {
        System.out.println("\n----------------Hyderabad Metro Map----------------\n");
        System.out.println("\tEdges\t\t\t   Distance(KM)\n");
        for (String key : vtces.keySet()) {
            String strn = key + " =>\n";
            Vertex vtx = vtces.get(key);
            for (String nbr : vtx.nbrs.keySet()) {
                strn += "\t" + nbr;
                if (nbr.length() < 24) {
                    strn += "\t";
                }
                if (nbr.length() < 16) {
                    strn += "\t";
                }
                if (nbr.length() < 8) {
                    strn += "\t";
                }
                strn += vtx.nbrs.get(nbr) + "\n";
            }
            System.out.println(strn);
        }
        System.out.println("---------------------------------------------------\n");
    }

    public void displayStations() {
        System.out.println("\n----------List of stations----------\n");
        int i = 1;
        int j = 0;
        String prev = lines.get(j);
        for (String key : stations) {
            String[] resArr = key.split("~");
            if (!resArr[1].contains(String.valueOf(prev.charAt(0)))) {
                j++;
                prev = lines.get(j);
                System.out.println("\n------------" + prev + "-------------\n");
            }
            System.out.println("    " + i + ". " + key);
            i++;
        }
        System.out.println("\n------------------------------------\n");
    }

    public boolean hasPath(String vName1, String vName2, Map<String, Boolean> processed) {
        if (containsEdge(vName1, vName2)) {
            return true;
        }
        processed.put(vName1, true);
        Vertex vtx = vtces.get(vName1);
        for (String nbr : vtx.nbrs.keySet()) {
            if (!processed.containsKey(nbr) && hasPath(nbr, vName2, processed)) {
                return true;
            }
        }
        return false;
    }

    class DijkstraPair implements Comparable<DijkstraPair> {
        String vName;
        String psf;
        double cost;

        public DijkstraPair() {
            vName = "";
            psf = "";
            cost = 0;
        }

        public int compareTo(DijkstraPair other) {
            return Double.compare(this.cost, other.cost);
        }
    }

    class Pair {
        String vName;
        String psf;
        double minDis;
        double minTime;

        public Pair() {
            vName = "";
            psf = "";
            minDis = 0;
            minTime = 0;
        }
    }

    public String getMinimumDistance(String src, String dst) {
        double mini = Double.POSITIVE_INFINITY;
        String ans = "";
        Map<String, Boolean> processed = new HashMap<>();
        Queue<Pair> queue = new LinkedList<>();
        Pair sp = new Pair();
        sp.vName = src;
        sp.psf = src + "  ";
        sp.minDis = 0;
        sp.minTime = 0;
        queue.add(sp);
        while (!queue.isEmpty()) {
            Pair rp = queue.poll();
            if (processed.containsKey(rp.vName)) {
                continue;
            }
            processed.put(rp.vName, true);
            if (rp.vName.equals(dst)) {
                double temp = rp.minDis;
                if (temp < mini) {
                    ans = rp.psf;
                    mini = temp;
                }
                continue;
            }
            Vertex rpvtx = vtces.get(rp.vName);
            List<String> nbrs = new ArrayList<>(rpvtx.nbrs.keySet());
            for (String nbr : nbrs) {
                if (!processed.containsKey(nbr)) {
                    Pair np = new Pair();
                    np.vName = nbr;
                    np.psf = rp.psf + nbr + "  ";
                    np.minDis = rp.minDis + rpvtx.nbrs.get(nbr);
                    queue.add(np);
                }
            }
        }
        ans += mini;
        return ans;
    }

    public List<String> getInterchanges(String s) {
        List<String> arr = new ArrayList<>();
        String[] res = s.split("  ");
        arr.add(res[0]);
        int count = 0;
        for (int i = 1; i < res.length - 1; i++) {
            int index = res[i].indexOf('~');
            String station = res[i].substring(index + 1);
            if (station.length() == 2) {
                String prev = res[i - 1].substring(res[i - 1].indexOf('~') + 1);
                String next = res[i + 1].substring(res[i + 1].indexOf('~') + 1);
                if (prev.equals(next)) {
                    arr.add(res[i]);
                } else {
                    arr.add(res[i] + " ==> " + res[i + 1]);
                    i++;
                    count++;
                }
            } else {
                arr.add(res[i]);
            }
        }
        arr.add(String.valueOf(count));
        arr.add(res[res.length - 1]);
        return arr;
    }

    public int cost(double d) {
        if (0 <= d && d < 2) {
            return 10;
        } else if (2 <= d && d < 4) {
            return 15;
        } else if (4 <= d && d < 6) {
            return 25;
        } else if (6 <= d && d < 8) {
            return 30;
        } else if (8 <= d && d < 10) {
            return 35;
        } else if (10 <= d && d < 14) {
            return 40;
        } else if (14 <= d && d < 18) {
            return 45;
        } else if (18 <= d && d < 22) {
            return 50;
        } else if (22 <= d && d < 26) {
            return 55;
        } else {
            return 60;
        }
    }

    public String time(double dis) {
        double ts = dis * 109.0;
        double tm = ts / 60.0;
        double th = tm / 60.0;
        double temp = tm;
        tm = tm - th * 60.0;
        ts = ts - temp * 60.0;
        String t = "";
        if (th > 0.0) {
            if (th == 1.0) {
                t += "1 Hr ";
            } else {
                t += Math.round(th) + " Hrs ";
            }
        }
        if (tm > 0.0) {
            if (tm == 1.0) {
                t += "1 Min ";
            } else {
                t += Math.round(tm) + " Mins ";
            }
        }
        if (ts > 0.0) {
            if (ts == 1.0) {
                t += "1 Sec ";
            } else {
                t += Math.round(ts) + " Secs ";
            }
        }
        if (t.isEmpty()) {
            t = "0 Secs";
        }
        return t;
    }

    public void createMetroMap() {
        try {
            Scanner scanner = new Scanner(new File("stations.csv"));
            while (scanner.hasNextLine()) {
                addVertex(scanner.nextLine().trim());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Scanner scanner = new Scanner(new File("edges.csv"));
            String prev = scanner.nextLine().trim();
            boolean flag = false;
            while (scanner.hasNextLine()) {
                String row = scanner.nextLine().trim();
                if (row.isEmpty()) {
                    flag = true;
                    continue;
                }
                if (flag) {
                    prev = row;
                    flag = false;
                    continue;
                }
                String[] lis = row.split(",");
                String cur = lis[0];
                double val = Double.parseDouble(lis[1]);
                addEdge(prev, cur, val);
                prev = cur;
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String stationsInput() {
        displayStations();
        System.out.println("\n1. To Enter Serial no. of Stations\n2. To Enter Name of Stations\n");
        Scanner scanner = new Scanner(System.in);
        String st1 = "";
        String st2 = "";
        while (true) {
            System.out.print("Enter your choice : ");
            int ch = scanner.nextInt();
            scanner.nextLine();
            if (ch == 1) {
                System.out.print("\nEnter the Source Station      : ");
                st1 = stations.get(scanner.nextInt() - 1);
                scanner.nextLine();
                System.out.print("Enter the Destination Station : ");
                st2 = stations.get(scanner.nextInt() - 1);
                scanner.nextLine();
                System.out.println("\n------------- Trip Details -------------");
                System.out.println("\n    Source      : " + st1.split("~")[0]);
                System.out.println("    Destination : " + st2.split("~")[0]);
                break;
            } else if (ch == 2) {
                System.out.print("\nEnter the Source Station      : ");
                st1 = scanner.nextLine();
                System.out.print("Enter the Destination Station : ");
                st2 = scanner.nextLine();
                break;
            } else {
                System.out.println("\nInvalid Choice\n");
            }
        }
        scanner.close();
        return st1 + " " + st2;
    }
}