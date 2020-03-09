package lexical;

import java.util.HashMap;
import java.util.Map;

//DFA转换表的一行
public class DFATableElement {
    private int state;
    private Map<String[], Integer> transfer;
    private String type;
    private boolean isFinish;

    public DFATableElement(int state, String type, boolean isFinish) {
        this.state = state;
        this.type = type;
        this.isFinish = isFinish;
        this.transfer = new HashMap<>();
    }

    public int getState() {
        return state;
    }

    public String getType() {
        return type;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void addTransferElement(String[] input, int nextState){
        transfer.put(input, nextState);
    }

    public int getNextState(char input){
        for(String[] key:transfer.keySet()){
            for(String s:key){
                if(s.indexOf(input)!=-1){
                    return transfer.get(key);
                }
            }
        }
        return -1;
    }

    //debug用，打印出来的不规范
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(state + ":");
        for(String[] key:transfer.keySet()){
            for(String s:key){
                sb.append(" " + s);
            }
            sb.append(": " + transfer.get(key));
        }
        sb.append(" isFinish=" + isFinish + " type=" + type);
        return sb.toString();
    }
}
