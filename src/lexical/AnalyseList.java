package lexical;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalyseList {
    // 成员变量,产生式集，终结符集，非终结符集
    private List<Production> productions;
    private List<String> terminals;
    private List<String> nonterminals;
    private Map<String, List<String>> firsts;
    private Map<String, List<String>> follows;

    public AnalyseList(){
        productions = new ArrayList<Production>();
        terminals = new ArrayList<String>();
        nonterminals = new ArrayList<String>();
        firsts = new HashMap<String, List<String>>();
        follows = new HashMap<String, List<String>>();

        setProductions();
        setNonTerminals();
        setTerminals();

        getFirst();
        getFollow();
        getSelect();

        Predict();

    }




    // 从文件中读取产生式
    public void setProductions(){
        try {
            File file = new File("grammar.txt");
            RandomAccessFile randomfile = new RandomAccessFile(file, "r");
            String line;
            String left;
            String right;
            Production production;
            while ((line=randomfile.readLine())!=null) {
                left = line.split("->")[0].trim();
                right = line.split("->")[1].trim();
                production = new Production(left, right.split(" "));
                productions.add(production);
            }
            randomfile.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    private void setNonTerminals() {
        for(Production production:productions){
            if(!nonterminals.contains(production.getLeft())){
                nonterminals.add(production.getLeft());
            }
        }
    }

    private void setTerminals() {
        String[] rights;
        for(Production production:productions){
            rights = production.getRights();
            for(int i=0;i<rights.length;i++){
                if(nonterminals.contains(rights[i]) || rights[i].equals("empty")){
                    continue;
                }else if(!terminals.contains(rights[i])){
                    terminals.add((rights[i]));
                }
            }
        }
    }

    private void getFirst() {
        List<String> first;
        //为每个终结符计算first集
        for(int i=0;i<terminals.size();i++){
            first = new ArrayList<>();
            first.add(terminals.get(i));
            firsts.put(terminals.get(i), first);
        }
        //为每个非终结符初始化first集
        for(int i=0;i<nonterminals.size();i++){
            first = new ArrayList<>();
            firsts.put(nonterminals.get(i), first);
        }
        boolean changed = false;
        while (true){
            changed = false;
            String left;
            String right;
            String[] rights;
            for(int i=0;i<productions.size();i++){
                left = productions.get(i).getLeft();
                rights = productions.get(i).getRights();
                for(int j=0;j<rights.length;j++){
                    right = rights[j];
                    if(!right.equals("empty")){
                        for(int k=0;k<firsts.get(right).size();k++){
                            if(!firsts.get(left).contains(firsts.get(right).get(k))){
                                firsts.get(left).add((firsts.get(right).get(k)));
                                changed = true;
                            }
                        }
                    }
                    if(canNull(right)){
                        continue;
                    }else {
                        break;
                    }
                }
            }
            if(changed == false){
                break;
            }
        }
    }

    private void getFollow() {
    }

    private void getSelect() {
    }

    private void Predict() {
    }

    private boolean canNull(String symbol){
        for(Production production:productions){
            if(production.getLeft().equals(symbol) && production.getRights()[0].equals("empty")){
                return true;
            }
        }
        return false;
    }
}
