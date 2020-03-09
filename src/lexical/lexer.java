package lexical;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class lexer {
    private JFrame frame;
    private JTextArea textArea;
    private JTable table;
    private DefaultTableModel tokenListModel;
    private DefaultTableModel dfaListModel;
    private DefaultTableModel errorListModel;
    private String filepath;
    private JScrollPane jScrollPane;

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
     * Launch the application.
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    lexer window = new lexer("1.xls");
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     * @throws Exception
     */
    public lexer(String dfaFilePath) throws Exception{
        filepath = dfaFilePath;
        dfa = readDFATable.readDFAFromFile(new File(dfaFilePath));
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     * @throws Exception
     */
    private void initialize() throws Exception{
        frame = new JFrame();
        frame.setBounds(100, 100,1098,926);
        Dimension   screensize   =   Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize=frame.getSize();
        frame.setLocation((screensize.width-frameSize.width)/2,(screensize.height-frameSize.height)/2);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(32, 178, 170));
        panel.setBounds(0, 0, 1080, 879);
        frame.getContentPane().add(panel);
        panel.setLayout(null);

        JPanel panel_1 = new JPanel();
        panel_1.setBackground(new Color(102, 204, 204));
        panel_1.setBounds(23, 13, 1043, 853);
        panel.add(panel_1);
        panel_1.setLayout(null);

        jScrollPane = new JScrollPane();

        textArea = new JTextArea();
        textArea.setBackground(new Color(220, 220, 220));

        textArea.setBounds(14, 77, 285, 372);
        panel_1.add(textArea);
        textArea.setColumns(10);

        final JButton btnNewButton_2 = new JButton("选择文件");
        btnNewButton_2.setFont(new Font("宋体", Font.BOLD, 15));
        btnNewButton_2.setBackground(new Color(32, 178, 170));
        btnNewButton_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();             //设置选择器
                chooser.setMultiSelectionEnabled(true);             //设为多选
                int returnVal = chooser.showOpenDialog(btnNewButton_2);
                if (returnVal == JFileChooser.APPROVE_OPTION) {          //如果符合文件类型
                    String filepath = chooser.getSelectedFile().getAbsolutePath();      //获取绝对路径
                    System.out.println(filepath);
                    filepath=filepath.replaceAll("\\\\", "/");
                    File file = new File(filepath);
                    textArea.setText(txt2String(file));
                }
            }
        });
        btnNewButton_2.setBounds(42, 30, 107, 34);
        panel_1.add(btnNewButton_2);

        JButton btnNewButton_1 = new JButton("\u6D4B\u8BD5");
        btnNewButton_1.setFont(new Font("宋体", Font.BOLD, 15));
        btnNewButton_1.setBackground(new Color(32, 178, 170));
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String str= textArea.getText();
                String str2 = str.replaceAll("\r|\n","");  //去掉换行符空格
                try {
                    System.out.println("进入ActionEvent==========================================");
                    lexicalAnalysis(str2);
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
        btnNewButton_1.setBounds(186, 30, 92, 34);
        panel_1.add(btnNewButton_1);

        //种别码表格
        tokenListModel = new DefaultTableModel(
                new Object[][] {},
                new String[] {
                        "字符串","类别","种别码","value"
                }
        );

        JTable tokenListTb = new JTable();
        tokenListTb.setBackground(new Color(224, 255, 255));
        tokenListTb.setFillsViewportHeight(true);
        tokenListTb.setModel(tokenListModel);

        RowSorter<DefaultTableModel> sorter = new TableRowSorter<DefaultTableModel>(tokenListModel);
        tokenListTb.setRowSorter(sorter);
        JScrollPane tokenSP = new JScrollPane();
        tokenSP.setViewportView(tokenListTb);
        tokenSP.setBounds(313, 77, 461, 372);
        panel_1.add(tokenSP);

        //DFA表格
        dfaListModel = new DefaultTableModel(
                new Object[][] {},
                new String[] {
                        "","","","","","","","","","","","","","","","",""
                }
        );
        String[][] result=readDFATable.getData(new File(filepath), 0);
        for(int i=1;i<result.length;i++) {
            for(int j=1;j<result[i].length-2;j++) {
                result[i][j]=result[i][j].replaceAll("-1", " ");
            }
        }
        for(int i=0;i<result.length;i++) {
            dfaListModel.addRow(new Object[] { result[i][0],result[i][1],result[i][2],result[i][3],result[i][4],result[i][5],result[i][6],result[i][7],result[i][8],result[i][9]
                    ,result[i][10],result[i][11],result[i][12],result[i][13],result[i][14],result[i][15],result[i][16],result[i][17]});

        }
        RowSorter<DefaultTableModel> sorter1 = new TableRowSorter<DefaultTableModel>(dfaListModel);
//出错表格
        errorListModel = new DefaultTableModel(
                new Object[][] {},
                new String[] {
                        "出错符号","出错地方","出错原因"
                }
        );

        JTable errorListTb = new JTable();
        errorListTb.setBackground(new Color(224, 255, 255));
        errorListTb.setFillsViewportHeight(true);
        errorListTb.setModel(errorListModel);

        //errorListTbModel.addRow(new Object[] { "出错", "类别","出错原因" ,"value"});

        RowSorter<DefaultTableModel> sorter2 = new TableRowSorter<DefaultTableModel>(errorListModel);
        errorListTb.setRowSorter(sorter2);
        JScrollPane errorSP = new JScrollPane();
        errorSP.setViewportView(errorListTb);
        errorSP.setBounds(788, 77, 241, 372);
        panel_1.add(errorSP);

        JLabel lblToken = new JLabel("TOKEN TABLE");
        lblToken.setFont(new Font("宋体", Font.BOLD, 30));
        lblToken.setBounds(462, 30, 254, 34);
        panel_1.add(lblToken);

        JLabel lblErrorTable = new JLabel("ERROR TABLE");
        lblErrorTable.setFont(new Font("宋体", Font.BOLD, 30));
        lblErrorTable.setBounds(811, 33, 203, 41);
        panel_1.add(lblErrorTable);

        JPanel panel_2 = new JPanel();
        panel_2.setBackground(new Color(255, 204, 204));
        panel_2.setBounds(0, 462, 1043, 404);
        panel_1.add(panel_2);
        panel_2.setLayout(null);

        JTable dfaListTb = new JTable();
        dfaListTb.setBackground(new Color(250, 240, 230));
        dfaListTb.setFillsViewportHeight(true);
        dfaListTb.setModel(dfaListModel);
//        dfaListTb.setRowSorter(sorter1);
        JScrollPane dfaSP = new JScrollPane();
        dfaSP.setViewportView(dfaListTb);
        dfaSP.setBounds(14, 70, 1015, 321);
        panel_2.add(dfaSP);

        JLabel lblDfa = new JLabel("DFA\u8F6C\u6362\u8868");
        lblDfa.setBounds(408, 10, 285, 47);
        panel_2.add(lblDfa);
        lblDfa.setFont(new Font("宋体", Font.BOLD, 42));
    }

    //读文件
    public static String txt2String(File file){
        StringBuilder result = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                result.append(s+System.lineSeparator());
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        result.append(' ');
        return result.toString();
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
                    }else {
                        tokenListModel.addRow(new Object[] {
                                currentString,dfa[currentState].getType(),tokenID(currentString),
                                dfa[currentState].getType().equals("注释")||dfa[currentState].getType().equals("运算符")||dfa[currentState].getType().equals("界符")?"   ":currentString
                        });
                    }
                }else {
                    errorListModel.addRow(new Object[] { currentString,"第"+i+"字符", "非法字符"});
                    tokenListModel.addRow(new Object[] { currentString,"非法字符","无","   "});
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
                    }else {
                        tokenListModel.addRow(new Object[] {
                                currentString,dfa[lastState].getType(),tokenID(currentString),
                                dfa[lastState].getType().equals("注释")||dfa[lastState].getType().equals("运算符")||dfa[lastState].getType().equals("界符")?"   ":currentString
                        });
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
}
