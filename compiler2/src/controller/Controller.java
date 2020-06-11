package controller;

import model.Analyzer;
import model.LL1;
import model.Node;
import view.MainFrame;

import javax.swing.table.DefaultTableModel;
import java.util.*;

/**
 * Created by Administrator
 *  界面模块控制器
 */
public class Controller {

    private MainFrame mainFrame = null;
    private Analyzer analyzer = null;
    private LL1 lang = null;

    //去除界面化注释
//    public Controller(MainFrame frame){
//        mainFrame = frame;
//
//    }

    public void extact_left_factor(String text)
    {

    }
    public boolean determine_type_recursion(ArrayList<String> array)
    {
        ArrayList<Node>  analy=new ArrayList<>();
        for(int i =0 ;i<array.size();i++)
        {
            Node node =new Node(array.get(i).charAt(0),array.get(i).substring(3));
            analy.add(node);
        }
        for (int i=0;i<analy.size();i++)
        {
            if(analy.get(i).left == analy.get(i).right.charAt(0))
            {
                return  true;
            }
        }
      return  false;
    }
    public void indirect_left_recursion(String text)
    {

    }
//    //消除左递归
//    public List<String> direct_left_recursion(ArrayList<String> array,TreeSet<Character> nvSet){
//        List<String> temp1 = new ArrayList<>();  //保存消除了左递归的新的List
//        ArrayList<Node> analy = new ArrayList<>();
//        List<Character> ch = new ArrayList<>();  //保存已经使用了的字母
//        TreeSet<Character> NvSet = nvSet;          //继承参数的内容（非终结符集）
//        for (int i = 0; i < array.size(); i++) {
//            Node aa = new Node(array.get(i).charAt(0),array.get(i).substring(3));
//            analy.add(aa);
//        }
//        for(int i=0;i<analy.size();i++){   //遍历所有产生式
//            if(analy.get(i).left == analy.get(i).right.charAt(0)){   //当发现左部等于右部第一个符号
//                ch.add(analy.get(i).left);                           //对该左部字母进行保存
//                for(int j=0;j<analy.size();j++){                     //寻找该字母的另一个产生式，以便寻找P->βP'的β
//                    if((analy.get(j).left == analy.get(i).left)){
//                        if (analy.get(j).left != analy.get(j).right.charAt(0)){
//                            for(char k='A';k<='Z';k++){                 //遍历字母集，寻找可以替代的字母
//                                int flag =0;
//                                Iterator it = NvSet.iterator();
//                                while (it.hasNext()){
//                                    char t =(char)it.next();
//                                    if(k == t){
//                                        flag = 1;                       //当该字母在原来的非终结符集有了，置flag=1
//                                    }
//                                }
//                                if(flag == 0) {                       //如果该字母没有使用过，就按照消除左递归的方法设定新的产生式
//                                    String str1 = analy.get(i).left + "->" + analy.get(j).right.charAt(0) + k;
//                                    String str2 = k + "->" + analy.get(i).right.substring(1) + k;
//                                    String str3 = k + "->" + '$';
//                                    NvSet.add(k);          //将该字母加入非终结符集合
//                                    temp1.add(str1);
//                                    temp1.add(str2);
//                                    temp1.add(str3);
//                                    break;
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        for (int i =0;i<analy.size();i++){     //对于原来的产生式，将没有左递归的产生式加入新的List
//            int flag =0;
//            Iterator it = ch.iterator();
//            while (it.hasNext()){                //如果原来产生式的左部在ch链表中，则证明该符号具有左递归，置flag=1
//                char t =(char)it.next();
//                if(analy.get(i).left == t){
//                    flag = 1;
//                }
//            }
//            if(flag == 0){                      //如果没有出现，加入新链表
//                String str1 = analy.get(i).left+"->"+analy.get(i).right;
//                temp1.add(str1);
//            }
//        }
//        return temp1;
//    }
    /**
     * 开始分析文法
     * @param text 传入的文法string
     */
    public void analysisLang(String text){

        lang = new LL1();
        ArrayList<String> array = new ArrayList<String>();
     //   TreeSet nvSet = new TreeSet<Character>();
        setLL1(array,text);
        //    ArrayList<String> array1=lang.direct_left_recursion(array,nvSet);
        lang.setLL1Array(array);
        lang.initNvNt();
        TreeSet nvSet = lang.getNvSet();
        ArrayList<String> array1=lang.direct_left_recursion(array,nvSet);
        lang.setLL1Array(array1);//文法表达式集合
        lang.setS('E');//开始符
        lang.init();
//      人间修改，取消图形化界面的形成
//        setFirstTable();
//        setFollowTable();
//        setAnalysisTable();
    }

    /**
     * 开始分析句子
     * @param text
     */
    public void analysisStr(String text){

        analyzer = new Analyzer(this);
        analyzer.setStartChar('E');
        analyzer.setLang(lang);
        analyzer.setStr(text);
        LinkedList<Object[]> stack = analyzer.analyze();
        DefaultTableModel stackModel = mainFrame.getStackModel();

        if (stack == null) {
            // TODO: 2017/10/17 错误
        }else{
            // TODO: 2017/10/17 输出结果
            int row = 0;
            for (Object[] value :stack) {
                for (int i = 0; i < 5; i++) {
                    if (value[i] != null){
                        stackModel.setValueAt(value[i].toString(),row,i);
                    }
                }
                row++;
            }
        }

        System.out.println("program finished ");
    }
    /***
     * 人间修改分析
     * 2020.5.21
     */
    public void analysisStrByRenJian(String text){
        analyzer = new Analyzer(this);
        analyzer.setStartChar('E');
        analyzer.setLang(lang);
        analyzer.setStr(text);
        analyzer.overAnalyze();
    }

    /**
     * 设置界面分析表
     */
    private void setAnalysisTable() {
        if (lang != null) {
            HashMap<Character,HashMap<String,TreeSet<Character>>> selectMap =  lang.getSelectMap();
            DefaultTableModel selectModel = new DefaultTableModel();

            TreeSet<Character> ntSet = lang.getNtSet();//终结符集合
            ntSet.remove('ε');
            selectModel.addColumn("");//第一列
            int column = 1;
            for (Character nt :ntSet) {
                selectModel.addColumn(nt);
                column++;
            }
            selectModel.addColumn('#');//最后一列
            column++;

            Set<Character> keySet = selectMap.keySet();//非终结符集合
            int row = 0;
            for (Character key :keySet) {//非终结符
                selectModel.addRow(new Object[column]);
                selectModel.setValueAt(key,row,0);
                HashMap<String,TreeSet<Character>>value =  selectMap.get(key);
                Set<String> strSet = value.keySet();//该非终结符的 所有表达式
                for (String str : strSet) {
                    TreeSet<Character> ntValue = value.get(str);//表达式对应的终结符列
                    for (Character nt :ntValue) {
                        if(nt == '#'){
                            selectModel.setValueAt(key+"->"+str,row,column-1);
                        }else{
                            for (int i = 1; i < column - 1; i++) {
                                String tempNt = selectModel.getColumnName(i);
                                if(tempNt.equals(nt.toString())){//对应的列
                                    selectModel.setValueAt(key+"->"+str,row,i);
                                }
                            }
                        }
                    }
                }
                row++;
            }
            mainFrame.updateAnalysis(selectModel);//更新到界面
        }
    }

    private void setFollowTable() {
        if (lang != null) {
            HashMap<Character,TreeSet<Character>> followMap =  lang.getFollowMap();
            DefaultTableModel followModel = mainFrame.getFollowModel();
            Set<Character> keySet = followMap.keySet();
            int row = 0;
            for (Character key : keySet) {
                followModel.setValueAt(key,row,0);
                followModel.setValueAt(followMap.get(key),row,1);
                row++;
            }
        }
    }

    private void setFirstTable(){
        if (lang != null) {
            HashMap<Character,TreeSet<Character>> firstMap =  lang.getFirstMap();
            DefaultTableModel firstModel = mainFrame.getFirstModel();
            Set<Character> keySet = firstMap.keySet();
            int row = 0;
            for (Character key : keySet) {
                firstModel.setValueAt(key,row,0);
                firstModel.setValueAt(firstMap.get(key),row,1);
                row++;
            }
        }
    }



    /**
     * 设置文法的 表达式数据
     * @param array
     * @param text 读取到的文法文本（文本格式：每行一个表达式，回车换行）
     */
    private static void setLL1(ArrayList<String> array, String text) {
        String[] item = text.split("\\s+");
        for (String str :item) {
            array.add(str);
        }
    }
}
