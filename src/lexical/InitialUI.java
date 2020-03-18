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

public class InitialUI {
    private JFrame frame;
    private JTextArea textArea;
    private JTable table;
    private DefaultTableModel tokenListModel;
    private DefaultTableModel dfaListModel;
    private DefaultTableModel errorListModel;
    private String filepath;
    private JScrollPane jScrollPane;

    public InitialUI() throws Exception {
        filepath = "1.xls";
        initialize();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    InitialUI window = new InitialUI();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    /**
     * Initialize the contents of the frame.
     * @throws Exception
     */
    private void initialize() throws Exception{
        frame = new JFrame("编译器前端");
        frame.setBounds(100, 100,1098,926);
        Dimension screensize   =   Toolkit.getDefaultToolkit().getScreenSize();
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
//        textArea.setBackground(new Color(220, 220, 220));
        textArea.setBackground(new Color(255, 255, 255));
        textArea.setBounds(14, 77, 285, 372);
        panel_1.add(textArea);
        textArea.setColumns(10);

        final JButton btnNewButton_1 = new JButton("选择文件");
        btnNewButton_1.setFont(new Font("宋体", Font.BOLD, 15));
        btnNewButton_1.setBackground(new Color(32, 178, 170));
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();             //设置选择器
                chooser.setMultiSelectionEnabled(true);             //设为多选
                int returnVal = chooser.showOpenDialog(btnNewButton_1);
                if (returnVal == JFileChooser.APPROVE_OPTION) {          //如果符合文件类型
                    String filepath = chooser.getSelectedFile().getAbsolutePath();      //获取绝对路径
                    System.out.println(filepath);
                    filepath=filepath.replaceAll("\\\\", "/");
                    File file = new File(filepath);
                    textArea.setText(txt2String(file));
                }
            }
        });
        btnNewButton_1.setBounds(42, 30, 107, 34);
        panel_1.add(btnNewButton_1);

        JButton btnNewButton_2 = new JButton("词法分析");
        btnNewButton_2.setFont(new Font("宋体", Font.BOLD, 15));
        btnNewButton_2.setBackground(new Color(32, 178, 170));
        btnNewButton_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String str= textArea.getText();
                String str2 = str.replaceAll("\r|\n","");  //去掉换行符空格
                try {
                    System.out.println("进入ActionEvent==========================================");
                    Lexer lexer = new Lexer("1.xls", tokenListModel, errorListModel);
                    lexer.lexicalAnalysis(str2);
                    System.out.println(lexer.getTokenSeq());
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });

        btnNewButton_2.setBounds(186, 30, 107, 34);
        panel_1.add(btnNewButton_2);

        JButton btnNewButton_3 = new JButton("语法规则");
        btnNewButton_3.setFont(new Font("宋体", Font.BOLD, 15));
        btnNewButton_3.setBackground(new Color(32, 178, 170));
        btnNewButton_3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //TODO
            }
        });
        btnNewButton_3.setBounds(330, 30, 107, 34);
        panel_1.add(btnNewButton_3);

        JButton btnNewButton_4 = new JButton("语法分析");
        btnNewButton_4.setFont(new Font("宋体", Font.BOLD, 15));
        btnNewButton_4.setBackground(new Color(32, 178, 170));
        btnNewButton_4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //TODO
            }
        });
        btnNewButton_4.setBounds(474, 30, 107, 34);
        panel_1.add(btnNewButton_4);

        JButton btnNewButton_5 = new JButton("语法分析");
        btnNewButton_5.setFont(new Font("宋体", Font.BOLD, 15));
        btnNewButton_5.setBackground(new Color(32, 178, 170));
        btnNewButton_5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //TODO
            }
        });
        btnNewButton_5.setBounds(618, 30, 107, 34);
        panel_1.add(btnNewButton_5);

        JButton btnNewButton_6 = new JButton("语义规则");
        btnNewButton_6.setFont(new Font("宋体", Font.BOLD, 15));
        btnNewButton_6.setBackground(new Color(32, 178, 170));
        btnNewButton_6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //TODO
            }
        });
        btnNewButton_6.setBounds(762, 30, 107, 34);
        panel_1.add(btnNewButton_6);

        JButton btnNewButton_7 = new JButton("语义分析");
        btnNewButton_7.setFont(new Font("宋体", Font.BOLD, 15));
        btnNewButton_7.setBackground(new Color(32, 178, 170));
        btnNewButton_7.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //TODO
            }
        });
        btnNewButton_7.setBounds(906, 30, 107, 34);
        panel_1.add(btnNewButton_7);

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
                        "","","","","","","","","","","","","","","","","","","",""
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
                    ,result[i][10],result[i][11],result[i][12],result[i][13],result[i][14],result[i][15],result[i][16],result[i][17], result[i][18], result[i][19]});

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

//        JLabel lblToken = new JLabel("TOKEN TABLE");
//        lblToken.setFont(new Font("宋体", Font.BOLD, 30));
//        lblToken.setBounds(462, 30, 254, 34);
//        panel_1.add(lblToken);
//
//        JLabel lblErrorTable = new JLabel("ERROR TABLE");
//        lblErrorTable.setFont(new Font("宋体", Font.BOLD, 30));
//        lblErrorTable.setBounds(811, 33, 203, 41);
//        panel_1.add(lblErrorTable);

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

}
