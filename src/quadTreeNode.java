import java.util.*;

public class quadTreeNode {
    int coorX, coorY, len;
    int size;
    boolean isSplit;
    quadTreeNode parent, NE, NW, SE, SW;
    TreeMap<Pair<Integer, Integer>, TreeSet<Point>> mPoints;  //{(x,y),{name1,name2,name3}}

    public quadTreeNode(int x, int y, int l, quadTreeNode par) {
        coorX = x;
        coorY = y;
        len = l;
        parent = par;
        NE = NW = SE = SW = null;
        isSplit = false;
        mPoints = new TreeMap<Pair<Integer, Integer>, TreeSet<Point>>();
        size = 0;
    }

    public void split() {
        isSplit = true;
        NW = new quadTreeNode(coorX, coorY, len / 2, this);
        NE = new quadTreeNode(coorX, coorY + len / 2, len / 2, this);
        SW = new quadTreeNode(coorX + len / 2, coorY, len / 2, this);
        SE = new quadTreeNode(coorX + len / 2, coorY + len / 2, len / 2, this);
        for (Map.Entry<Pair<Integer, Integer>, TreeSet<Point>> entry : mPoints.entrySet()) {
            if (NW.isIn(entry.getValue().first()) == true) {
                NW.mPoints.put(entry.getKey(), entry.getValue());
                NW.size += entry.getValue().size();
            } else if (NE.isIn(entry.getValue().first()) == true) {
                NE.mPoints.put(entry.getKey(), entry.getValue());
                NE.size += entry.getValue().size();
            } else if (SW.isIn(entry.getValue().first()) == true) {
                SW.mPoints.put(entry.getKey(), entry.getValue());
                SW.size += entry.getValue().size();
            } else if (SE.isIn(entry.getValue().first()) == true) {
                SE.mPoints.put(entry.getKey(), entry.getValue());
                SE.size += entry.getValue().size();
            }
        }
        mPoints.clear();
        size = 0;
    }

    public boolean merge() {
        for (Map.Entry<Pair<Integer, Integer>, TreeSet<Point>> entry : NW.mPoints.entrySet()) {
            mPoints.put(entry.getKey(), entry.getValue());
            size += entry.getValue().size();
        }
        for (Map.Entry<Pair<Integer, Integer>, TreeSet<Point>> entry : SW.mPoints.entrySet()) {
            mPoints.put(entry.getKey(), entry.getValue());
            size += entry.getValue().size();
        }
        for (Map.Entry<Pair<Integer, Integer>, TreeSet<Point>> entry : NE.mPoints.entrySet()) {
            mPoints.put(entry.getKey(), entry.getValue());
            size += entry.getValue().size();
        }
        for (Map.Entry<Pair<Integer, Integer>, TreeSet<Point>> entry : SE.mPoints.entrySet()) {
            mPoints.put(entry.getKey(), entry.getValue());
            size += entry.getValue().size();
        }
        boolean check = (size < 4 || mPoints.size() <= 1);
        if (check == false) {
            mPoints.clear();
            size = 0;
        } else {
            isSplit=false;
            NW = NE = SW = SE = null;
        }
        return check;
    }

    public boolean isIn(Point p) {
        return (p.X >= coorX && p.X <= coorX + len - 1) && (p.Y >= coorY && p.Y <= coorY + len - 1);
    }

}
