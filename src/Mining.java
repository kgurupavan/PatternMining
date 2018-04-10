import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class Mining {

 public static void main(String[] args) {
  // TODO Auto-generated method stub
  //Read Transaction Database
  try {
   String[] input = args[0].split(",");
   if (!args[0].contains(",")) {
    System.out.println("Incorrect format of arguments\nExample: 5,2,'C:\\Project\\transactionDB','output.txt'\n");
   } else {
	if(input.length==4)
	{
    System.out.println("Started mining frequent itemsets.....");
    Long startTime = System.currentTimeMillis()/1000;
    int min_sup = Integer.parseInt(input[0]);
    int K = Integer.parseInt(input[1]);
    // System.out.println(input[2]);
    String file = input[2].trim();
    String output=input[3].toString();
   
    
    int count = 1;
    List < String > initial = Files.readAllLines(Paths.get(file));
    //read all lines from transactionDB
    Map < String, BitSet > transactionDB = new HashMap < > ();
    
    for (String line: initial) {
     String[] str = line.split(" ");
     //eliminate items less than minimum number of items.
     if (str.length >= K) {
      for (String transact: str) {
       BitSet bitSet = new BitSet();
       if (!transactionDB.containsKey(transact)) {
        bitSet.set(count);
        transactionDB.put(transact, bitSet);
       }
       bitSet = transactionDB.get(transact);
       bitSet.set(count);
      }
      count++;
     }
    }

    TreeSet < DataStructure > init_List = new TreeSet < > ();
    for (Map.Entry < String, BitSet > entry: transactionDB.entrySet()) {
     if (entry.getValue().cardinality() >= min_sup) {
      init_List.add(new DataStructure(entry.getKey(), entry.getValue().cardinality()));
     }
    };
    
    // List for maintaining frequent item transactions for output writing    
    List < TreeSet < DataStructure >> endList = new LinkedList < TreeSet < DataStructure >> ();
    endList.add(init_List);
    
    final TreeSet < DataStructure > [] recursive_List = new TreeSet[]{endList.get(0)};

    //loop starts from 2nd scan to final scan        
    for (int loop = 2; !recursive_List[0].isEmpty(); loop++) {

     TreeSet < DataStructure > list = new TreeSet < > ();
     DataStructure frequent_item = null;
     Iterator < DataStructure > itr = recursive_List[0].iterator();
     while (itr.hasNext()) {
      DataStructure a = itr.next();
      Iterator < DataStructure > tailSetIterator = recursive_List[0].tailSet(a).iterator();
      while (tailSetIterator.hasNext()) {
       DataStructure b = tailSetIterator.next();
       String left;
       String right;
       int check = 0;
       int inside = 0;
       int i = 0;
       for (i = 0; i < a.items.size() - 1; i++) {
        left = a.items.get(i);
        right = b.items.get(i);

        if (!left.equals(right)) {
         inside = 1;
         break;
        }
       }

       left = a.items.get(i);
       right = b.items.get(i);

       if (inside != 1 && left.compareTo(right) < 0) {
        frequent_item = new DataStructure(a, b);
        BitSet bitSet = new BitSet(count);
        bitSet.set(0, count);
        
        for(int k=0; k < frequent_item.items.size();k++)
        {
    	   bitSet.and(transactionDB.get(frequent_item.items.get(k)));
        }
       
        frequent_item.sup = bitSet.cardinality();
        
        if (frequent_item.sup >= min_sup) {
         for (int x = 0; x < frequent_item.items.size(); x++) 
         {
           String item_del = frequent_item.items.remove(x);

           if (!recursive_List[0].contains(frequent_item)) 
           {
            check = 1;
            break;
            }

          frequent_item.items.add(x, item_del);
          }
         if (check != 1) {
          list.add(frequent_item);
         }
        }
       }

      }


     }
     endList.add(list);
     recursive_List[0] = list;
    }
    System.out.println("End of process.");
    FileWriter out = new FileWriter(new File(output));
    int track=0;
    
    for(int i=0;i<endList.size();i++) {
    	
    		Iterator<DataStructure> iter=endList.get(i).iterator();
    		while(iter.hasNext()) {
    		DataStructure x=iter.next(); 
    		if(x.items.size()>=K)
    		{	out.write(x.toString() + "\n");
    			track++;  }
    		}
    }
    System.out.println(track+" transactions captured"); 
    out.close();
    Long endTime = System.currentTimeMillis()/1000;
    System.out.println("Elapsed time - " + (endTime - startTime));
   }
	else {
		   System.out.println("Insufficient number of arguments\nExample: 5,2,'C:\\Project\\transactionDB','output.txt'\n");
	   }
   }
   

  } catch (IOException e) {
   e.printStackTrace();
  }

 }

}