import java.util.*;


public class ClosestPair {

    public static class Point {
        double x, y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double distanceTo(Point other) {
            double dx = x - other.x;
            double dy = y - other.y;
            return Math.sqrt(dx * dx + dy * dy);
        }
    }

    public static double findClosestPair(Point[] points) {
        int n = points.length;

        // Sort points by x-coordinate
        Arrays.sort(points, (p1, p2) -> Double.compare(p1.x, p2.x));

        // Find closest pair recursively
        return findClosestPairRecursive(points, 0, n - 1);
    }

    private static double findClosestPairRecursive(Point[] points, int left, int right) {
        if (right - left <= 3) {
            return bruteForceClosestPair(points, left, right);
        }

        int mid = (left + right) / 2;
        double dl = findClosestPairRecursive(points, left, mid);
        double dr = findClosestPairRecursive(points, mid + 1, right);
        double d = Math.min(dl, dr);

        // Find points that are close to the dividing line
        List<Point> strip = new ArrayList<>();
        for (int i = left; i <= right; i++) {
            if (Math.abs(points[i].x - points[mid].x) < d) {
                strip.add(points[i]);
            }
        }

        // Sort points in the strip by y-coordinate
        Collections.sort(strip, (p1, p2) -> Double.compare(p1.y, p2.y));

        // Find closest pair in the strip
        for (int i = 0; i < strip.size() - 1; i++) {
            for (int j = i + 1; j < strip.size() && (strip.get(j).y - strip.get(i).y) < d; j++) {
                d = Math.min(d, strip.get(i).distanceTo(strip.get(j)));
            }
        }

        return d;
    }

    private static double bruteForceClosestPair(Point[] points, int left, int right) {
        double minDistance = Double.MAX_VALUE;
        for (int i = left; i <= right; i++) {
            for (int j = i + 1; j <= right; j++) {
                minDistance = Math.min(minDistance, points[i].distanceTo(points[j]));
            }
        }
        return minDistance;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter the number of points: ");
        int n = sc.nextInt();

        Point[] points = new Point[n];
        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            double x = rand.nextDouble() * 1000;
            double y = rand.nextDouble() * 1000;
            points[i] = new Point(x, y);
        }

        long startTime = System.nanoTime();
        double closestDistance = findClosestPair(points);
        long endTime = System.nanoTime();

        long elapsedTime = endTime - startTime;
        System.out.println("Closest distance: " + closestDistance);
        System.out.println("Elapsed time: " + elapsedTime + " nanoseconds");
    }
}