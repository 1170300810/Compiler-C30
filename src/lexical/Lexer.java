package lexical;

import javax.swing.table.DefaultTableModel;
import java.io.File;

public class Lexer {

    private DefaultTableModel tokenListModel;
    private DefaultTableModel dfaListModel;
    private DefaultTableModel errorListModel;
    private StringBuilder result;

    private DFATableElement[] dfa;

    String[] keywords={"char","long","short","float","double","const","boolean","void","null","false","true","enum","int",
            "do","while","if","else","for","then","break","continue","class","static","final","extends","new","return","signed"
            ,"struct","union","unsigned","goto","switch","case","default","auto","extern","register","sizeof","typedef","volatile"
    };
    String[] tokens={"char","long","short","float","double","const","boolean","void","null","false","true","enum","int",
            "do","while","if","else","for","then","break","continue","class","static","final","extends","new","return","signed"
            ,"struct","union","unsigned","goto","switch","case","default","auto","extern","register","sizeof","typedef","volatile",
            ">",">=","<","<=","==","!=","|","&","||","&&","!","^","+","-","*","/","%","++","--","+=","-=","*=","/=",
            ",","=",";","[","]","(",")","{","}",".","\"","'"};

    /**
     * Create the application.
     * @throws Exception
     */
    public Lexer(String dfaFilePath, DefaultTableModel tokenListModel, DefaultTableModel errorListModel) throws Exception{
//        filepath = dfaFilePath;
        this.tokenListModel = tokenListModel;
        this.errorListModel = errorListModel;
        dfa = readDFATable.readDFAFromFile(new File(dfaFilePath));
        result = new StringBuilder();
//        initialize();
    }


    public void lexicalAnalysis(String input){
        while(tokenListModel.getRowCount()>0){
            tokenListModel.removeRow(0);
        }
        while(errorListModel.getRowCount()>0){
            errorListModel.removeRow(0);
        }
        char[] charArray = input.toCharArray();
        String currentString = "";
        int currentState = 0;
        int lastState = 0;
        for(int i=0;i<charArray.length;i++){
            if(charArray[i] == ' '){
                if(dfa[currentState].isFinish()){
                    if(isKeyword(currentString)){
                        tokenListModel.addRow(new Object[] {currentString,"关键字",tokenID(currentString),"   "});
                        result.append(currentString);
                    }else {
                        tokenListModel.addRow(new Object[] {
                                currentString,dfa[currentState].getType(),tokenID(currentString),
                                dfa[currentState].getType().equals("注释")||dfa[currentState].getType().equals("运算符")||dfa[currentState].getType().equals("界符")?"   ":currentString
                        });
                        if(dfa[currentState].getType().equals("运算符") || dfa[currentState].getType().equals("界符")){
                            result.append(currentString);
                        }else if(dfa[currentState].getType().equals("字符常量")){
                            result.append("character");
                        }else if (dfa[currentState].getType().equals("标识符")){
                            result.append("id");
                        }else{
                            result.append("digit");
                        }
                    }
                }
                currentString = "";
                currentState = 0;
//                lastState = 0;
                continue;
            }
            lastState = currentState;
            currentState = dfa[currentState].getNextState(charArray[i]);
            if(currentState == -1){
                if(dfa[lastState].isFinish()){
                    if(isKeyword(currentString)){
                        tokenListModel.addRow(new Object[] {currentString,"关键字",tokenID(currentString),"   "});
                        result.append(currentString);
                    }else {
                        tokenListModel.addRow(new Object[] {
                                currentString,dfa[lastState].getType(),tokenID(currentString),
                                dfa[lastState].getType().equals("注释")||dfa[lastState].getType().equals("运算符")||dfa[lastState].getType().equals("界符")?"   ":currentString
                        });
                    }
                    if(dfa[lastState].getType().equals("运算符") || dfa[lastState].getType().equals("界符")){
                        result.append(currentString);
                    }else if(dfa[lastState].getType().equals("字符常量")){
                        result.append("character");
                    }else if (dfa[lastState].getType().equals("标识符")){
                        result.append("id");
                    }else{
                        result.append("digit");
                    }
                }else {
                    errorListModel.addRow(new Object[] { currentString,"第"+i+"字符", "非法字符"});
                    tokenListModel.addRow(new Object[] { currentString,"非法字符","无","   "});
                }
                currentString = "";
                currentString = currentString + charArray[i];
                currentState = dfa[0].getNextState(charArray[i]);
                if(currentState == -1){//如果在0状态就扫描到非法字符
                    errorListModel.addRow(new Object[] { currentString,"第"+i+"字符", "非法字符"});
                    tokenListModel.addRow(new Object[] { currentString,"非法字符","无","   "});
                    currentString = "";
                    currentState = 0;
                }
                continue;
//                lastState = 0;
            }
            currentString = currentString + charArray[i];
        }
    }

    public boolean isKeyword(String word){
        for(int i=0;i<keywords.length;i++){
            if(keywords[i].equals(word)){
                return true;
            }
        }
        return false;
    }

    public int tokenID(String word){
        for(int i=0;i<tokens.length;i++){
            if(tokens[i].equals(word)){
                return i;
            }
        }
        return 100;//若token中未找到，按标识符处理
    }

    public String getTokenSeq(){
        return result.toString();
    }
}
