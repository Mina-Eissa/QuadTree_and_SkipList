import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Point2 {
    static public boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }
    static public boolean validPoint(Point P) {
        return isAlpha(P.Name.charAt(0)) && P.X >= 0 && P.X < 1024 && P.Y >= 0 && P.Y < 1024;
    }

    static public void main(String[] args) {
        String fileName = args[0];
        Scanner Input = null;
        try {
            Input = new Scanner(new File(fileName));//"C:\\Users\\x\\Desktop\\3rd_year_2021\\Algorithm\\quad-tree\\src\\P3test3.txt"));
            SkipList Skip = new SkipList();
            QuadTree Tree = new QuadTree();
            while (Input.hasNext()) {
                String command, pointName;
                int pointX, pointY, w, h;
                command = Input.next();
                command.toLowerCase();
                switch (command) {
                    case "insert":
                        pointName = Input.next();
                        pointX = Input.nextInt();
                        pointY = Input.nextInt();
                        Point New = new Point(pointName, pointX, pointY);
                        if (validPoint(New)) {
                            if (Skip.find(New) == null) {
                                Skip.insert(New);
                                Tree.insert(New);
                                System.out.print("Point inserted: ");
                                New.print();
                            }
                        } else {
                            System.out.print("Point REJECTED: ");
                            New.print();
                        }
                        break;
                    case "remove":
                        pointName = Input.next();
                        if (isNumber(pointName) && Input.hasNextInt()) {
                            pointX = Integer.parseInt(pointName);
                            pointY = Input.nextInt();
                            Point del = new Point();
                            del = Tree.remove(pointX, pointY);
                            if (del == null) {
                                System.out.println("point Not Removed: (" + pointX + ", " + pointY + ")");
                            } else {
                                //skip remove update
                                System.out.print("Point ");
                                System.out.print("(" + del.Name + ", " + del.X + ", " + del.Y + ")");
                                System.out.println(" Removed");
                                Skip.remove(del);
                            }
                        } else {
                            Point del = new Point();
                            del = Skip.remove(pointName);
                            if (del == null) {
                                System.out.println("Point Not Removed: " + pointName);
                            } else {
                                //Tree remove update
                                System.out.print("Point ");
                                System.out.print("(" + del.Name + ", " + del.X + ", " + del.Y + ")");
                                System.out.println(" Removed");
                                Tree.remove(del);
                            }
                        }
                        break;
                    case "dump":
                        Skip.dump();
                        Tree.dump();
                        break;
                    case "duplicates":
                        Tree.duplicates();
                        break;
                    case "search":
                        pointName = Input.next();
                        Point target = new Point(pointName, -1, -1);
                        target = Skip.find(target);
                        if (target != null) {
                            Skip.search(target);
                        } else {
                            System.out.println("Point not found: " + pointName);
                        }
                        break;
                    case "regionsearch":
                        pointX = Input.nextInt();
                        pointY = Input.nextInt();
                        w = Input.nextInt();
                        h = Input.nextInt();
                        if (w < 0 || h < 0) {
                            System.out.println("Rectangle Rejected: (" + pointX + ", " + pointY + ", " + w + ", " + h + ")");
                        } else {
                            System.out.println("Points Intersecting Region: (" + pointX + ", " + pointY + ", " + w + ", " + h + ")");
                            Tree.regionSearch(pointX, pointY, w, h);
                        }
                        break;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static boolean isNumber(String test) {

        try {
            Integer.parseInt(test);
            return true;
        } catch (Exception e) {
            return false;
        }

    }
}