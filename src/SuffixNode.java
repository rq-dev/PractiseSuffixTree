import java.util.HashMap;

public class SuffixNode {

    private Integer start;
    private Integer end;
    private SuffixNode suffixLink;
    private Boolean leaf;
    private Boolean[] type;
    private HashMap<Character, SuffixNode> children;

    public SuffixNode(Integer start, Integer end, Boolean leaf) {
        this.start = start;
        this.end = end;
        this.suffixLink = null;
        this.leaf = leaf;
        this.children = new HashMap<>();
        this.type = new Boolean[2];
        this.type[0] = Boolean.FALSE;
        this.type[1] = Boolean.FALSE;
    }

    public Boolean isTypeA() {
        return type[0];
    }

    public Boolean isTypeB() {
        return type[1];
    }

    public void setType(Boolean a, Boolean b) {
        this.type[0] = a;
        this.type[1] = b;
    }

    public Boolean[] getType(){
        return this.type;
    }

    public SuffixNode getChild(Character c) {
        return children.get(c);
    }

    public void addChild(Character c, SuffixNode n) {
        children.put(c, n);
    }

    public Boolean containsChild(Character c) {
        return children.containsKey(c);
    }

    public HashMap<Character, SuffixNode> getChildren() {
        return children;
    }

    public void setSuffixLink(SuffixNode n) {
        this.suffixLink = n;
    }

    public SuffixNode getSuffixLink() {
        return suffixLink;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getStart() {
        return start;
    }

    public Integer getEnd() {
        return end;
    }

    public Boolean isLeaf() {
        return leaf;
    }
}