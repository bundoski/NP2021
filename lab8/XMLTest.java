package lab8;

import java.sql.Array;
import java.util.*;

abstract class XMLComponent {

    String tag;
    List<String> keys;
    Map<String, String> attrs;

    public XMLComponent(String tag) {
        this.tag = tag;
        attrs = new HashMap<>();
        keys = new ArrayList<>();
    }

    abstract protected void print(int indentation);

    public void addAttribute(String key, String value) {
        attrs.putIfAbsent(key, value);
        keys.add(key);
    }

    public void display() {
        this.print(0);
    }
}

class XMLLeaf extends XMLComponent {

    String value;

    public XMLLeaf(String tag, String value) {
        super(tag);
        this.value = value;
    }

    @Override //<student type="redoven">
    protected void print(int indentation) {
        StringBuilder components = new StringBuilder();
        if(attrs.size()!=0) {
            for (String key : keys) {
                components
                        .append(" ")
                        .append(key)
                        .append("=\"")
                        .append(attrs.get(key))
                        .append("\"");
            }
        }
        for(int i=0; i<indentation; i++) System.out.print("\t");
        System.out.printf("<%s%s>%s</%s>\n", tag, components.toString(), value, tag);
    }
}

class XMLComposite extends XMLComponent {

    List<XMLComponent> componentList;

    public XMLComposite(String tag) {
        super(tag);
        componentList = new ArrayList<>();
    }

    @Override
    protected void print(int indentation) {
        StringBuilder components = new StringBuilder();
        if(attrs.size()!=0) {
            for (String key : keys) {
                components
                        .append(" ")
                        .append(key)
                        .append("=\"")
                        .append(attrs.get(key))
                        .append("\"");
            }
        }
        for(int i=0; i<indentation; i++)
            System.out.print("\t");
        System.out.printf("<%s%s>\n", tag, components.toString());
        componentList.forEach(comp -> comp.print(indentation+1));
        for(int i=0; i<indentation; i++)
            System.out.print("\t");
        System.out.printf("</%s>\n", tag);
    }

    public void addComponent(XMLComponent component) {
        componentList.add(component);
    }
}

public class XMLTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int testCase = sc.nextInt();
        XMLComponent component = new XMLLeaf("student", "Trajce Trajkovski");
        component.addAttribute("type", "redoven");
        component.addAttribute("program", "KNI");

        XMLComposite composite = new XMLComposite("name");
        composite.addComponent(new XMLLeaf("first-name", "trajce"));
        composite.addComponent(new XMLLeaf("last-name", "trajkovski"));
        composite.addAttribute("type", "redoven");
        component.addAttribute("program", "KNI");

        if (testCase == 1) {
            //TODO Print the component object
            component.display();
        } else if (testCase == 2) {
            //TODO print the composite object
            composite.display();
        } else if (testCase == 3) {
            XMLComposite main = new XMLComposite("level1");
            main.addAttribute("level", "1");
            XMLComposite lvl2 = new XMLComposite("level2");
            lvl2.addAttribute("level", "2");
            XMLComposite lvl3 = new XMLComposite("level3");
            lvl3.addAttribute("level", "3");
            lvl3.addComponent(component);
            lvl2.addComponent(lvl3);
            lvl2.addComponent(composite);
            lvl2.addComponent(new XMLLeaf("something", "blabla"));
            main.addComponent(lvl2);
            main.addComponent(new XMLLeaf("course", "napredno programiranje"));

            //TODO print the main object
            main.display();
        }
    }
}
