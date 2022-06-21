import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class QuadTree {
    quadTreeNode Head;
    int size;
    //region search
    List<Point> intersectList;
    int Qnodes;

    public QuadTree() {
        Head = new quadTreeNode(0, 0, 1024, null);
        size = 1;
        intersectList = new ArrayList<Point>();
    }

    private void split(quadTreeNode Cur) {
        if (Cur.size < 4 || Cur.mPoints.size() <= 1 || Cur.isSplit == true)
            return;
        size += 4;
        Cur.split();
        split(Cur.NW);
        split(Cur.NE);
        split(Cur.SW);
        split(Cur.SE);
    }

    private void merge(quadTreeNode Cur) {
        if (Cur == null)
            return;
        boolean check = Cur.merge();
        if (check == true) {
            size -= 4;
            merge(Cur.parent);
        }
    }

    public void insert(Point New) {
        quadTreeNode Cur = Head;
        while (Cur.isSplit == true) {
            if (Cur.NE.isIn(New)) {
                Cur = Cur.NE;
            } else if (Cur.NW.isIn(New)) {
                Cur = Cur.NW;
            } else if (Cur.SE.isIn(New)) {
                Cur = Cur.SE;
            } else if (Cur.SW.isIn(New)) {
                Cur = Cur.SW;
            }
        }
        Pair<Integer, Integer> addingKey = new Pair<Integer, Integer>(New.X, New.Y);
        if (Cur.mPoints.containsKey(addingKey) == false)
            Cur.mPoints.put(addingKey, new TreeSet<Point>());
        Cur.mPoints.get(addingKey).add(New);
        Cur.size++;
        split(Cur);

    }

    private void dump(quadTreeNode Cur, int lvl) {
        for (int i = 0; i < lvl; i++) {
            System.out.print("  ");
        }
        System.out.print("Node at " + Cur.coorX + ", " + Cur.coorY + ", " + Cur.len + ":");
        if (Cur.isSplit == true) {
            System.out.println(" Internal");
            dump(Cur.NW, lvl + 1);
            dump(Cur.SW, lvl + 1);
            dump(Cur.NE, lvl + 1);
            dump(Cur.SE, lvl + 1);
        } else {
            if (Cur.mPoints.size() == 0) {
                System.out.println(" Empty");
            } else {
                System.out.println();
                for (Map.Entry<Pair<Integer, Integer>, TreeSet<Point>> entry : Cur.mPoints.entrySet()) {
                    for (Point it : entry.getValue()) {
                        for (int i = 0; i < lvl; i++) {
                            System.out.print("  ");
                        }
                        it.print();
                    }
                }

            }
        }
    }

    private void duplicates(quadTreeNode Cur) {
        if (Cur.isSplit == true) {
            duplicates(Cur.NW);
            duplicates(Cur.SW);
            duplicates(Cur.NE);
            duplicates(Cur.SE);
        } else {
            for (Map.Entry<Pair<Integer, Integer>, TreeSet<Point>> entry : Cur.mPoints.entrySet()) {
                if (entry.getValue().size() > 1)
                    System.out.println("(" + entry.getKey().first + ", " + entry.getKey().second + ")");
            }
        }

    }

    private boolean intersect(int x1, int y1, int x2, int y2,
                              int a, int b, int c, int d) {
        if (a > x2 || c < x1 || d < y1 || b > y2)
            return false;
        return true;
    }

    private Point delete(Point target) {
        quadTreeNode Cur = Head;
        while (Cur.isSplit == true) {
            if (Cur.NW.isIn(target))
                Cur = Cur.NW;
            else if (Cur.NE.isIn(target))
                Cur = Cur.NE;
            else if (Cur.SW.isIn(target))
                Cur = Cur.SW;
            else if (Cur.SE.isIn(target))
                Cur = Cur.SE;
            else
                return null;
        }
        Pair<Integer, Integer> removingKey = new Pair<Integer, Integer>(target.X, target.Y);
        if (Cur.mPoints.containsKey(removingKey) == false) {
            return null;
        }
        if (target.Name == "-1") {
            Point ret = Cur.mPoints.get(removingKey).first();
            Cur.mPoints.get(removingKey).remove(ret);
            if(Cur.mPoints.get(removingKey).size()==0){
                Cur.mPoints.remove(removingKey);
            }
            Cur.size--;
            merge(Cur.parent);
            return ret;
        } else {
            Cur.mPoints.get(removingKey).remove(target);
            if(Cur.mPoints.get(removingKey).size()==0){
                Cur.mPoints.remove(removingKey);
            }
            Cur.size--;
            merge(Cur.parent);
            return target;
        }
    }

    private void regionSearch(quadTreeNode Cur, int x1, int y1, int x2, int y2) {
        if (!intersect(x1, y1, x2, y2, Cur.coorX, Cur.coorY, Cur.coorX + Cur.len - 1, Cur.coorY + Cur.len - 1)) {
            return;
        }
        if (Cur.isSplit == true) {
            Qnodes++;
            regionSearch(Cur.NW, x1, y1, x2, y2);
            regionSearch(Cur.SW, x1, y1, x2, y2);
            regionSearch(Cur.NE, x1, y1, x2, y2);
            regionSearch(Cur.SE, x1, y1, x2, y2);
        } else {
            if (!Cur.mPoints.isEmpty()) {
                Qnodes++;
            }
            for (Map.Entry<Pair<Integer, Integer>, TreeSet<Point>> entry : Cur.mPoints.entrySet()) {
                if (!intersect(entry.getKey().first, entry.getKey().second, entry.getKey().first,
                        entry.getKey().second, x1, y1, x2, y2)) {
                    continue;
                }
                for (Point it : entry.getValue()) {
                    intersectList.add(it);
                }
            }
        }
    }

    public Point remove(int x, int y) {
        return delete(new Point("-1", x, y));
    }

    public Point remove(Point target) {
        if (target == null)
            return null;
        return delete(target);
    }

    public void dump() {
        System.out.println("QuadTree Dump:");
        dump(Head, 0);
        System.out.println("QuadTree Size: " + size + " QuadTree Nodes Printed.");
    }

    public void duplicates() {
        System.out.println("Duplicate Points:");
        duplicates(Head);
    }

    public void regionSearch(int x, int y, int w, int h) {
        intersectList.clear();
        Qnodes = 0;
        regionSearch(Head, x, y, x + w, y + h);
        for (Point it : intersectList) {
            System.out.print("Point found: ");
            it.print();
        }
        System.out.println(Qnodes + " QuadTree Nodes Visited");
    }

}
