import java.util.*;

public class SkipList {
    public SkipListNode head;
    public int level, size;
    static public Random ran = new Random();

    public SkipList() {
        head = new SkipListNode(null, 1);
        level = -1;
        size = 0;
    }

    private int randomLevel() {
        int lev;
        for (lev = 0; Math.abs(ran.nextInt()) % 2 == 0; lev++) { // ran is random generator
            ; // Do nothing
        }
        return lev;
    }

    public void insert(Point New) {
        int newLevel = randomLevel(); // New node's level
        if (newLevel > level) { // If new node is deeper
            adjustHead(newLevel); // adjust the header
        }
        // Track end of level
        SkipListNode[] update = new SkipListNode[level + 1];
        SkipListNode x = head; // Start at header node
        for (int i = level; i >= 0; i--) { // Find insert position
            while ((x.forward[i] != null) && (x.forward[i].Node.compareTo(New) < 0)) {
                x = x.forward[i];
            }
            update[i] = x; // Track end at level i
        }
        x = new SkipListNode(New, newLevel);
        for (int i = 0; i <= newLevel; i++) { // Splice into list
            x.forward[i] = update[i].forward[i]; // Who x points to
            update[i].forward[i] = x; // Who points to x
        }
        size++; // Increment dictionary size
    }

    private void adjustHead(int newLevel) {
        SkipListNode temp = head;
        head = new SkipListNode(head.Node, newLevel);
        for (int i = 0; i <= level; i++) {
            head.forward[i] = temp.forward[i];
        }
        level = newLevel;
    }

    public Point find(Point target) {
        SkipListNode x = head; // Dummy header node
        for (int i = level; i >= 0; i--) { // For each level...
            while ((x.forward[i] != null) && (x.forward[i].Node.compareTo(target) < 0)) { // go forward
                x = x.forward[i]; // Go one last step
            }
        }
        x = x.forward[0]; // Move to actual record, if it exists
        if ((x != null) && target.X == -1 && target.Y == -1 && x.Node.Name.compareTo(target.Name) == 0) {// search only by name
            return x.Node;
        } else if ((x != null) && (x.Node.compareTo(target) == 0)) { // search by all point
            return x.Node;
        } // Got it
        else {
            return null;
        } // Its not there
    }

    private Point delete(Point target) {
        SkipListNode[] update = new SkipListNode[level + 1];
        SkipListNode x = head; // Start at header node
        for (int i = level; i >= 0; i--) { // Find insert position
            while ((x.forward[i] != null) && (x.forward[i].Node.compareTo(target) < 0)) {
                x = x.forward[i];
            }
            update[i] = x; // Track end at level i
        }
        x = x.forward[0];
        if ((x != null) && target.X == -1 && target.Y == -1 && x.Node.Name.compareTo(target.Name) == 0) {// any point
            for (int i = x.forward.length - 1; i >= 0; i--) {
                update[i].forward[i] = x.forward[i];
            }
            size--;
            return x.Node;
        } else if ((x != null) && (x.Node.compareTo(target) == 0)) { // Exist and delete it
            for (int i = x.forward.length - 1; i >= 0; i--) {
                update[i].forward[i] = x.forward[i];
            }
            size--;
            return x.Node;
        } else {
            return null;
        } // Its not there
    }

    public void dump() {
        System.out.println("SkipList Dump:");
        List<SkipListNode> Return = new ArrayList<SkipListNode>();
        SkipListNode x = head;
        do {
            Return.add(x);
            x = x.forward[0];
        } while (x != null);
        dump(Return);
    }

    public void search(Point target) {
        SkipListNode x = head; // Dummy header node
        for (int i = level; i >= 0; i--) { // For each level...
            while ((x.forward[i] != null) && (x.forward[i].Node.compareTo(target) < 0)) { // go forward
                x = x.forward[i]; // Go one last step
            }
        }
        x = x.forward[0]; // Move to actual record, if it exists
        List<Point> Return = new ArrayList<Point>();
        while ((x != null) && x.Node.Name.compareTo(target.Name) == 0) {// search only by name
            Return.add(x.Node);
            x = x.forward[0];
        }
        printSearch(Return, target.Name);
    }

    public Point remove(String name) {
        return delete(new Point(name, -1, -1));
    }

    public Point remove(Point target) {
        if (target == null)
            return null;
        return delete(target);
    }

    private void dump(List<SkipListNode> ans) {
        for (int i = 0; i < ans.size(); i++) {

            System.out.print("level: " + ans.get(i).forward.length + " Value: ");
            if (ans.get(i).Node == null)
                System.out.println("null");
            else {
                ans.get(i).Node.print();
            }
        }
        System.out.print("The SkipList's Size is: ");
        System.out.println(ans.size() - 1);
    }

    private void printSearch(List<Point> ser, String name) {
        if (ser.size() == 0)
            System.out.println("Point not found: " + name);
        else {
            for (int i = 0; i < ser.size(); i++) {
                System.out.print("Point found: ");
                ser.get(i).print();
            }
        }
    }
}
