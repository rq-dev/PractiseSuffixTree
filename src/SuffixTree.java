import java.util.*;
import java.util.Stack;

public class SuffixTree {
    private SuffixNode activeNode;
    private SuffixNode lastNewNode;
    private Integer activeEdge;
    private Integer activeLength;
    private Integer remaining;
    private Integer splitIndex;
    private Integer globalEnd;
    private SuffixNode root;
    private String string;
    private Integer maxHeight;
    private Integer substringStartIndex;

    public SuffixTree(String string1, String string2) {
        this.splitIndex = string1.length();
        this.string = string1 + "#" + string2 + "$";
        this.activeEdge = -1;
        this.activeLength = 0;
        this.remaining = 0;
        this.root = new SuffixNode(-1, -1, Boolean.FALSE);
        this.activeNode = this.root;
        this.globalEnd = -1;
        buildTree();
    }

    private SuffixNode createNode(Integer start, Integer end, Boolean leaf) {
        SuffixNode newNode = new SuffixNode(start, end, leaf);
        newNode.setSuffixLink(this.root);
        return newNode;
    }

    public void buildTree() {

        for(int i = 0; i < string.length(); i++) {
            this.globalEnd++;
            this.remaining = this.remaining + 1;
            this.lastNewNode = null;
            Boolean showstopper = Boolean.FALSE;

            while(this.remaining > 0) {
                showstopper = Boolean.FALSE;
                if(this.activeLength == 0) {
                    this.activeEdge = i;
                }

                if(!this.activeNode.containsChild(this.string.charAt(this.activeEdge))) {
                    SuffixNode nn = createNode(i, i, Boolean.TRUE);
                    this.activeNode.addChild(this.string.charAt(this.activeEdge), nn);

                    if(this.lastNewNode != null) {
                        this.lastNewNode.setSuffixLink(activeNode);
                        this.lastNewNode = null;
                    }

                } else {
                    SuffixNode nn = activeNode.getChild(this.string.charAt(this.activeEdge));

                    if(this.activeLength >= getLength(nn)) {
                        this.activeEdge = this.activeEdge + getLength(nn);
                        this.activeLength = this.activeLength - getLength(nn);
                        this.activeNode = nn;
                        showstopper = Boolean.TRUE;

                    } else {
                        if(this.string.charAt(i) == this.string.charAt(nn.getStart() + this.activeLength)) {

                            if(this.lastNewNode != null && activeNode != this.root) {
                                this.lastNewNode.setSuffixLink(this.activeNode);
                                this.lastNewNode = null;
                            }
                            this.activeLength = this.activeLength + 1;
                            break;
                        }

                        SuffixNode newNode = createNode(i, i, Boolean.TRUE);
                        SuffixNode preSplit = createNode(nn.getStart(), nn.getStart() + this.activeLength - 1, Boolean.FALSE);
                        this.activeNode.addChild(this.string.charAt(this.activeEdge), preSplit);
                        nn.setStart(nn.getStart() + this.activeLength);
                        preSplit.addChild(this.string.charAt(nn.getStart()), nn);
                        preSplit.addChild(this.string.charAt(i), newNode);

                        if(this.lastNewNode != null) {
                            this.lastNewNode.setSuffixLink(preSplit);
                        }

                        this.lastNewNode = preSplit;
                    }
                }

                if(!showstopper) {

                    this.remaining = this.remaining - 1;

                    if(this.activeNode == this.root && this.activeLength > 0) {
                        this.activeLength = this.activeLength - 1;
                        this.activeEdge = this.activeEdge + 1;
                    } else if(this.activeNode != this.root) {
                        this.activeNode = this.activeNode.getSuffixLink();
                    }
                }
            }
        }
    }

    public Integer getEnd(SuffixNode n){
        if(n.isLeaf()) return this.globalEnd;
        else return n.getEnd();
    }

    public Integer getLength(SuffixNode n) {
        return getEnd(n) - n.getStart() + 1;
    }

    private void markNodes(SuffixNode n) {
        ArrayList<SuffixNode> vertexes = new ArrayList<>();;

        Stack<SuffixNode> task = new Stack<>();
        task.push(n);
        while (!task.isEmpty()) {

            SuffixNode i = task.peek();
            task.pop();

            if (i.isLeaf()) {
                if (i.getStart() <= this.splitIndex) {
                    i.setType(Boolean.TRUE, Boolean.FALSE);
                } else {
                    i.setType(Boolean.FALSE, Boolean.TRUE);
                }
                continue;

            } else {

                vertexes.add(i);
                for (Character c : i.getChildren().keySet()) {
                    task.push(i.getChild(c));
                }

                for (int k = 0; k < vertexes.size(); k++) {
                    Boolean[] ret = new Boolean[2];
                    ret[0] = Boolean.FALSE;
                    ret[1] = Boolean.FALSE;
                    for (Character c : vertexes.get(k).getChildren().keySet()) {
                        if(vertexes.get(k).getChild(c).getType()[0]) ret[0] = Boolean.TRUE;
                        if(vertexes.get(k).getChild(c).getType()[1]) ret[1] = Boolean.TRUE;
                    }
                    vertexes.get(k).setType(ret[0], ret[1]);
                }

                continue;
            }
        }
        for (int k = 0; k < vertexes.size(); k++) {
            Boolean[] types = new Boolean[2];
            types[0] = Boolean.FALSE;
            types[1] = Boolean.FALSE;
            for (Character c : vertexes.get(k).getChildren().keySet()) {
                if(vertexes.get(k).getChild(c).getType()[0]) types[0] = Boolean.TRUE;
                if(vertexes.get(k).getChild(c).getType()[1]) types[1] = Boolean.TRUE;
            }
            vertexes.get(k).setType(types[0], types[1]);
        }
    }

    public String getLongestCommonSubstring() {
        this.maxHeight = 0;
        this.substringStartIndex = 0;
        markNodes(this.root);
        findLCS(this.root);

        return this.string.substring(this.substringStartIndex, this.substringStartIndex + this.maxHeight);
    }

    private void findLCS(SuffixNode n) {

        Stack<Struct> task = new Stack<>();
        task.push(new Struct(0, n));
        while (!task.isEmpty()) {

            Struct i = task.peek();
            task.pop();
            if (i.getN() == null) {
                continue;
            }

            if (!i.getN().isTypeA() || !i.getN().isTypeB()) {
                continue;
            } else {
                for (Character c : i.getN().getChildren().keySet()) {
                    SuffixNode nn = i.getN().getChild(c);
                    if (nn.isTypeA() && nn.isTypeB()) {
                        if (this.maxHeight < i.getH() + getLength(nn)) {
                            this.maxHeight = i.getH() + getLength(nn);
                            this.substringStartIndex = getEnd(nn) - i.getH() - getLength(nn) + 1;
                        }
                        task.push(new Struct(i.getH() + getLength(nn), nn));
                    }
                }
            }
            continue;
        }
    }
}