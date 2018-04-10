import java.util.LinkedList;
import java.util.List;

public class DataStructure implements Comparable <Object>
{
    List<String> items;
    int sup;
   
    public DataStructure(DataStructure a, DataStructure b)
    {
           items = new LinkedList<>();
           items.addAll(a.items);
           items.add(b.items.get(b.items.size() - 1));
           sup = 0;
    }
    public DataStructure(String S, Integer V) {
        items = new LinkedList<>();
        items.add(S);
        sup = V;
    }

    public String toString()
    {
        StringBuilder line = new StringBuilder();

        line.append(items.get(0));
        for (int i = 1; i < items.size(); i++)
            line.append(" " + items.get(i));

        return line.toString() +" (" + sup + ")";

    }

    @Override
    public int compareTo(Object object) {

        DataStructure itemset = (DataStructure) object;
        int i = 0;

        while( i < this.items.size())
        {
            if( !this.items.get(i).equals(itemset.items.get(i)) )
                return this.items.get(i).compareTo(itemset.items.get(i));
            i++;
        }
        return 0;
    }

   
}
