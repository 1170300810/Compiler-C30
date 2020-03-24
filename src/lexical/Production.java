package lexical;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


// 产生式类
public class Production{
    String left;
    String[] right;
    // 初始化select集
    ArrayList<String> select = new ArrayList<String>();
    public Production(String left, String[] right){
        this.left = left;
        this.right = right;
    }

    public String[] getRights(){
        return right;
    }

    public String getLeft(){
        return left;
    }
}