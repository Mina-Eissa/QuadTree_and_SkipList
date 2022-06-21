public class Point implements Comparable<Point> {
    public String Name;
    public int X, Y;

    public Point() {
    }

    public Point(String name, int x, int y) {
        Name = name;
        X = x;
        Y = y;
    }

    public void print() {
        System.out.println("(" + Name + ", " + X + ", " + Y + ")");
    }

    public int compareTo(Point other) {
        if (Name.compareTo(other.Name) == 0) {
            if (X == other.X) {
                if (Y == other.Y) {
                    return 0;
                }
                return (Y < other.Y ? -1 : 1);
            }
            return (X < other.X ? -1 : 1);
        }
        return Name.compareTo(other.Name);
    }
}