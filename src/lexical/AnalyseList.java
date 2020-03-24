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
        productions = new ArrayList<>();
        terminals = new ArrayList<>();
        nonterminals = new ArrayList<>();
        firsts = new HashMap<>();
        follows = new HashMap<>();

        setProductions();
        setNonTerminals();
        setTerminals();

        getFirst();
        getFollow();
        getSelect();

        Predict();

    }

    public static void main(String[] args) {
        AnalyseList analyseList = new AnalyseList();
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
        //为所有非终结符初始化follow集
        List<String> follow;
        for(int i=0;i<nonterminals.size();i++){
            follow = new ArrayList<>();
            follows.put(nonterminals.get(i), follow);
        }
        follows.get("Program").add("$");//初始符号的follow集包含$
        boolean changed = false;
        boolean lastIsNonternimal = false;//标志产生式最后一个文法符号是否为非终结符
        while(true){
            changed = false;
            for(int i=0;i<productions.size();i++){
                String left = null;
                String right = null;
                String[] rights;
                rights = productions.get(i).getRights();
                for(int j=0;j<rights.length;j++){
                    right = rights[j];

                    if(nonterminals.contains(right)){
                        lastIsNonternimal = true;
                        for(int k=j+1;k<rights.length;k++){
                            for(int l=0;l<firsts.get(rights[k]).size();l++){
                                //将后一个元素的first集加入到前一个元素的follow集中
                                if(!follows.get(right).contains(firsts.get(rights[k]).get(l))){
                                    follows.get(right).add(firsts.get(rights[k]).get(l));
                                    changed = true;
                                }
                            }
                            if (canNull(rights[k])){
                                continue;
                            }else {
                                lastIsNonternimal = true;
                            }
                        }
//                        if(lastIsNonternimal)
                    }
                }
                if(nonterminals.contains(right)){
                    left = productions.get(i).getLeft();
                    for (int p=0;p<follows.get(left).size();p++){
                        if (!follows.get(right).contains(follows.get(left).get(p))){
                            follows.get(right).add(follows.get(left).get(p));
                            changed = true;
                        }
                    }
                }
            }
            if(changed == true){
                break;
            }
        }
    }

    private void getSelect() {
        String left;
        String right;
        String[] rights;
        List<String> follow = new ArrayList<>();
        List<String> first = new ArrayList<>();

        for (int i=0;i<productions.size();i++){
            left = productions.get(i).getLeft();
            rights = productions.get(i).getRights();
            if(rights[0].equals("empty")){
                follow = follows.get(left);
                for(int j=0;j<follow.size();j++){
                    if(!productions.get(i).select.contains(follow.get(j))){
                        productions.get(i).select.add(follow.get(j));
                    }
                }
            }else {
                boolean allEmpty = true;//标志产生式右部是否都可空
                for (int j=0;j<rights.length;j++){
                    right = rights[j];
                    first = firsts.get(right);
                    for(int k=0;k<first.size();k++){
                        if(!productions.get(i).select.contains(first.get(k))){
                            productions.get(i).select.add(first.get(k));
                        }
                    }
                    if(canNull(right)){
                        continue;
                    }else {
                        allEmpty = false;
                        break;
                    }
                }
                if(allEmpty){
                    follow = follows.get(left);
                    for(int j=0;j<follow.size();j++){
                        if(!productions.get(i).select.contains(follow.get(j))){
                            productions.get(i).select.add(follow.get(j));
                        }
                    }
                }
            }
        }
    }

    private void Predict() {
        Production production;
        String line;
        String[] rights;
        try {
            File file = new File("predictldy.txt");
            RandomAccessFile randomfile = new RandomAccessFile(file,"rw");
            for (int i = 0; i < productions.size(); i++) {
                production = productions.get(i);
                for (int j = 0; j < production.select.size(); j++) {
                    line = production.getLeft()+"#"+production.select.get(j)+" ->";
                    rights = production.getRights();
                    for (int v = 0; v < rights.length; v++) {
                        line = line+" "+rights[v];
                    }
                    line = line+"\n";
                    // 写入文件
                    randomfile.writeBytes(line);
                }
            }
            randomfile.close();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
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
