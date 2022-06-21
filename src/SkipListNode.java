public class SkipListNode{
    public Point Node;
    public SkipListNode[] forward;
    public SkipListNode(Point node,int level){
        Node = node;
        forward=new SkipListNode[level+1];
        for (int i = 0; i < level; i++) {
            forward[i] = null;
        }
    }
}
