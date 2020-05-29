package model;

import java.util.*;

/**
 * Created by Administrator
 * LL1 文法表达式 解析
 * 求 first follow 集合、分析表
 */
public class LL1 {
    /**
     * 分析预测表
     */


    /**
     * 人间修改
     */
    private HashMap<Character, Map<Character,String>> table;
    private String[][] analyzeTable;
    /**
     * LL1 文法表达式 存储集
     */
    private ArrayList<String> LL1Array;
    /**
     * 非终结符对应表达式 的集合
     */
    private HashMap<Character,ArrayList<String>> expressionMap;
    /**
     * 开始符
     */
    private Character s;
    /**
     * Vn非终结符集合
     */
    private TreeSet<Character> nvSet;
    /**
     * Vt终结符集合
     */
    private TreeSet<Character> ntSet;
    /**
     * First集合  非终结符 -> 终结符集合
     */
    private HashMap<Character, TreeSet<Character>> firstMap;
    /**
     * Follow集合  非终结符 -> 终结符集合
     */
    private HashMap<Character, TreeSet<Character>> followMap;
    /**
     * Select集合  非终结符  -> Map<表达式，终结符集合>
     */
    private HashMap<Character, HashMap<String, TreeSet<Character>>> selectMap;

    public String[][] getAnalyzeTable() {
        return analyzeTable;
    }

    public void setAnalyzeTable(String[][] analyzeTable) {
        this.analyzeTable = analyzeTable;
    }

    public ArrayList<String> getLL1Array() {
        return LL1Array;
    }

    public void setLL1Array(ArrayList<String> LL1Array) {
        this.LL1Array = LL1Array;
    }

    public HashMap<Character, ArrayList<String>> getExpressionMap() {
        return expressionMap;
    }

    public void setExpressionMap(HashMap<Character, ArrayList<String>> expressionMap) {
        this.expressionMap = expressionMap;
    }

    public Character getS() {
        return s;
    }

    public void setS(Character s) {
        this.s = s;
    }

    public TreeSet<Character> getNvSet() {
        return nvSet;
    }

    public void setNvSet(TreeSet<Character> nvSet) {
        this.nvSet = nvSet;
    }

    public TreeSet<Character> getNtSet() {
        return ntSet;
    }

    public void setNtSet(TreeSet<Character> ntSet) {
        this.ntSet = ntSet;
    }

    public HashMap<Character, TreeSet<Character>> getFirstMap() {
        return firstMap;
    }

    public void setFirstMap(HashMap<Character, TreeSet<Character>> firstMap) {
        this.firstMap = firstMap;
    }

    public HashMap<Character, TreeSet<Character>> getFollowMap() {
        return followMap;
    }

    public void setFollowMap(HashMap<Character, TreeSet<Character>> followMap) {
        this.followMap = followMap;
    }

    public HashMap<Character, HashMap<String, TreeSet<Character>>> getSelectMap() {
        return selectMap;
    }

    public void setSelectMap(HashMap<Character, HashMap<String, TreeSet<Character>>> selectMap) {
        this.selectMap = selectMap;
    }

    public HashMap<Character, Map<Character, String>> getTable() {
        return table;
    }

    public void setTable(HashMap<Character, Map<Character, String>> table) {
        this.table = table;
    }

    public LL1(){
        super();
        LL1Array = new ArrayList<String>();
        nvSet = new TreeSet<Character>();
        ntSet = new TreeSet<Character>();
        firstMap = new HashMap<Character, TreeSet<Character>>();
        followMap = new HashMap<Character, TreeSet<Character>>();
        selectMap = new HashMap<>();
    }
    //消除左递归
    public ArrayList<String> direct_left_recursion(ArrayList<String> array,TreeSet<Character> nvSet){
        ArrayList<String> temp1 = new ArrayList<>();  //保存消除了左递归的新的List
        ArrayList<Node> analy = new ArrayList<>();
        List<Character> ch = new ArrayList<>();  //保存已经使用了的字母
        TreeSet<Character> NvSet = nvSet;          //继承参数的内容（非终结符集）
        for (int i = 0; i < array.size(); i++) {
            Node aa = new Node(array.get(i).charAt(0),array.get(i).substring(3));
            analy.add(aa);
        }
        for(int i=0;i<analy.size();i++){   //遍历所有产生式
            if(analy.get(i).left == analy.get(i).right.charAt(0)){   //当发现左部等于右部第一个符号
                ch.add(analy.get(i).left);                           //对该左部字母进行保存
                for(int j=0;j<analy.size();j++){                     //寻找该字母的另一个产生式，以便寻找P->βP'的β
                    if((analy.get(j).left == analy.get(i).left)){
                        if (analy.get(j).left != analy.get(j).right.charAt(0)){
                            for(char k='G';k<='Z';k++){                 //遍历字母集，寻找可以替代的字母
                                int flag =0;
                                Iterator it = NvSet.iterator();
                                while (it.hasNext()){
                                    char t =(char)it.next();
                                    if(k == t){
                                        flag = 1;                       //当该字母在原来的非终结符集有了，置flag=1
                                    }
                                }
                                if(flag == 0) {                       //如果该字母没有使用过，就按照消除左递归的方法设定新的产生式
                                    String str1 = analy.get(i).left + "->" + analy.get(j).right.charAt(0) + k;
                                    String str2 = k + "->" + analy.get(i).right.substring(1) + k;
                                    String str3 = k + "->" + 'ε';
                                    NvSet.add(k);          //将该字母加入非终结符集合
                                    temp1.add(str1);
                                    temp1.add(str2);
                                    temp1.add(str3);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        for (int i =0;i<analy.size();i++){     //对于原来的产生式，将没有左递归的产生式加入新的List
            int flag =0;
            Iterator it = ch.iterator();
            while (it.hasNext()){                //如果原来产生式的左部在ch链表中，则证明该符号具有左递归，置flag=1
                char t =(char)it.next();
                if(analy.get(i).left == t){
                    flag = 1;
                }
            }
            if(flag == 0){                      //如果没有出现，加入新链表
                String str1 = analy.get(i).left+"->"+analy.get(i).right;
                temp1.add(str1);
            }
        }
        return temp1;
    }
    /**
     * 初始化相关数据
     */
    public void init(){
        initNvNt();
        initExpressionMaps();
        initFirst();
        initFollow();
        initSelect();
        initTable();


        HashMap<Character, TreeSet<Character>> first = firstMap;
        Iterator iter = first.entrySet().iterator();
        System.out.println("-------first集合---------------");
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Object val = entry.getValue();
            System.out.println(key.toString() + "---" + val.toString());
        }
        first = followMap;
        iter = first.entrySet().iterator();
        System.out.println("--------follow集合--------------");
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Object val = entry.getValue();
            System.out.println(key.toString() + "---" + val.toString());
        }
        HashMap<Character, HashMap<String,TreeSet<Character>>> select = selectMap;
        iter = select.entrySet().iterator();
        System.out.println("--------select集合--------------");
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Object val = entry.getValue();
            System.out.println(key.toString() + "---" + val.toString());
        }
        Set<Character> tempSet = table.keySet();
        System.out.println("--------预测分析表--------------");
        TreeSet<Character> tempNt = ntSet;
        tempNt.add('#');
        for(Character item:tempNt){
            System.out.print("\t"+item);
        }
        System.out.println();
        for(Character nv:tempSet){
            System.out.print(nv);
           for(Character nt:tempNt){
                System.out.print("\t"+table.get(nv).get(nt));
           }
            System.out.println();
        }
        System.out.println("----------------------");


    }

    /**
     * 获取非终结符集与终结符集
     */
    public void initNvNt() {
        for (String gsItem : LL1Array){
            String[] nvNtItem = gsItem.split("->");
            String charItemStr = nvNtItem[0];
            char charItem = charItemStr.charAt(0);
            // nv在左边
            nvSet.add(charItem);
        }
        for (String gsItem : LL1Array) {
            String[] nvNtItem = gsItem.split("->");
            // nt在右边
            String nvItemStr = nvNtItem[1];
            // 遍历每一个字
            for (int i = 0; i < nvItemStr.length(); i++) {
                char charItem = nvItemStr.charAt(i);
                if (!nvSet.contains(charItem)) {
                    ntSet.add(charItem);
                }
            }
        }
    }

    /**
     * 初始化表达式集合
     */
    public void initExpressionMaps() {
        expressionMap = new HashMap<Character, ArrayList<String>>();
        for (String gsItem : LL1Array) {
            String[] nvNtItem = gsItem.split("->");
            String charItemStr = nvNtItem[0];
            String charItemRightStr = nvNtItem[1];//表达式的右部，结果
            char charItem = charItemStr.charAt(0);//表达式的左部，非终结符 字符
            if (!expressionMap.containsKey(charItem)) {
                ArrayList<String> expArr = new ArrayList<String>();
                expArr.add(charItemRightStr);
                expressionMap.put(charItem, expArr);
            } else {
                ArrayList<String> expArr = expressionMap.get(charItem);
                expArr.add(charItemRightStr);
                expressionMap.put(charItem, expArr);
            }
        }
    }

    /**
     * 获取 first 集合
     * 实现方法：使用一个linkedList存储Nv，当某个Nv1的first集合为空时，需要这个集合的那个Nv2加到尾部，
     *          等Nv1计算完成后，在计算Nv2的first集合
     *
     * 待优化--------》递归函数
     */
    private void initFirst() {
        //所有非终结符遍历 ，求其 first 集合
        Iterator<Character> iterator = nvSet.iterator();
        LinkedList<Character> saveNvUnable = new LinkedList<Character>();
        while (iterator.hasNext()){
            saveNvUnable.add(iterator.next());
        }

        while (!saveNvUnable.isEmpty()) {
            Character charItem = saveNvUnable.pop();//非终结符
            ArrayList<String> arrayList = expressionMap.get(charItem);//同一个非终结符的 所有表达式
            here:
            for (String str :arrayList) {
                char firstChar = str.charAt(0);//第一个字符
                TreeSet<Character> itemSet = firstMap.get(charItem);//非终结符 对应的first集合
                if (itemSet == null) {
                    itemSet = new TreeSet<>();
                }
                //////////////////////
                if(ntSet.contains(firstChar)){//第一个符号 终结符 or ε
                    itemSet.add(firstChar);
                    firstMap.put(charItem, itemSet);
                }else if(nvSet.contains(firstChar)){//第一个符号 非终结符
                    // TODO:遍历整个表达式
                    boolean toNext;//是否继续读下一个字符 true->是 , false->否
                    int place = 0;//遍历式子的位置
                    do {
                        if(ntSet.contains(firstChar) && firstChar!='ε'){//如果在遍历式子时 扫到了一个终结符
                            itemSet.add(firstChar);
                            firstMap.put(charItem, itemSet);
                            break;
                        }
                        TreeSet<Character> firstNv = firstMap.get(firstChar);
                        toNext = false;
                        if (firstNv == null) {
                            saveNvUnable.add(charItem);
                            break here;// 跳出 for (String str :arrayList) 循环
                            // TODO: 这里有问题，当前这个非终结符的first集合可能没有求完
                        } else {// 他的first集合不为空,遍历扫描 把不是空符号的字符存过去
                            Iterator<Character> it = firstNv.iterator();
                            while (it.hasNext()) {
                                Character member = it.next();
                                if (member == 'ε') {
                                    //有空字符 判断是否是最后一个字符 不是就要扫描下一个字符
                                    //是就要把ε加进去 然后结束
                                    if(place+1 == str.length()){
                                        itemSet.add(member);
                                        firstMap.put(charItem, itemSet);
                                        break;
                                    }
                                    toNext = true;
                                    firstChar = str.charAt(++place);
                                }
                                else {
                                    itemSet.add(member);
                                    firstMap.put(charItem, itemSet);
                                }
                            }
                        }
                    }while (toNext);
                }
                ///////////////////////
            }

        }
    }



    /**
     * 获取 follow 集合
     */
    private void initFollow(){
        HashMap<Character,TreeSet<Character>> relationMap = new HashMap<>();
        //存储follow集合之间的属于关系;  TreeSet中存储指向第一个Character 的所有Nv
        for (Character tempKey : nvSet) {
            TreeSet<Character> tempSet = new TreeSet<Character>();
            followMap.put(tempKey, tempSet);
            TreeSet<Character> tempRelation = new TreeSet<Character>();
            relationMap.put(tempKey,tempRelation);
        }
        TreeSet<Character> startSet = followMap.get(s);
        startSet.add('#');
        followMap.put(s,startSet);
        //先将first集合转到follow集合中
        Iterator<Character> iterator = nvSet.iterator();
        while (iterator.hasNext()){
            char charItem = iterator.next();
            ArrayList<String> arrayList = expressionMap.get(charItem);//同一个非终结符的 所有表达式
            for (String str :arrayList) {
                if(str.length() >= 2){
                    char lastChar =  str.charAt(str.length()-1);
                    char last2Char =  str.charAt(str.length()-2);
                    if(nvSet.contains(last2Char)) {//判断倒数第二个是非终结字符
                        TreeSet<Character> FollowSet = followMap.get(last2Char);
                        if(ntSet.contains(lastChar)){//如果倒数第一个是终结符
                            FollowSet.add(lastChar);
                            followMap.put(last2Char, FollowSet);
                        }else {
                            TreeSet<Character> firstSet = firstMap.get(lastChar);
                            Iterator<Character> it = firstSet.iterator();
                            while (it.hasNext()) {
                                Character member = it.next();
                                if (member != 'ε') {
                                    FollowSet.add(member);
                                    followMap.put(last2Char, FollowSet);
                                }
                            }
                        }
                    }
                }
            }
        }
        //再将表达式 左部follow 转到 右部
        /**
         * 生成relationMap 用于存储follow集合之间的属于， 其中 key对应的Character的follow集合要属于到
         *                                                 value对应的TreeSet中所有的Character的follow集合中
         */
        iterator = nvSet.iterator();
        while (iterator.hasNext()){
            char charItem = iterator.next();
            ArrayList<String> arrayList = expressionMap.get(charItem);//同一个非终结符的 所有表达式
            for (String str :arrayList) {
                char lastChar =  str.charAt(str.length()-1);
                if(nvSet.contains(lastChar) && lastChar!=charItem){//最后一个为非终结符 and A与B不相同
                    TreeSet<Character> tempSet = relationMap.get(charItem);
                    tempSet.add(lastChar);
                    relationMap.put(charItem,tempSet);
                }
                // TODO:  下面的代码有问题：A->aBb 其中b代表的不一定只是一个文法符号，可能是一个符号串
                // TODO:                 下面的代码只处理了b是一个文法符号的情况
                if(str.length()>=2 && nvSet.contains(lastChar)){
                    char last2Char =  str.charAt(str.length()-2);
                    if(last2Char != charItem) {
                        TreeSet<Character> tempSet = firstMap.get(lastChar);
                        if (tempSet.contains('ε') && nvSet.contains(last2Char)) {
                            TreeSet<Character> temp2 = relationMap.get(charItem);
                            temp2.add(last2Char);
                            relationMap.put(charItem, temp2);
                        }
                    }
                }
            }
        }

        System.out.println("---A->B---relationMap---");
        Iterator iter = relationMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Object val = entry.getValue();
            System.out.println(key.toString() + "---" + val.toString());
        }

        for (Character Nv :nvSet) {
            TreeSet<Character> relationSet = relationMap.get(Nv);
            if (!relationSet.isEmpty()){
                for (Character value :relationSet) {
                    TreeSet<Character> A = followMap.get(Nv);
                    TreeSet<Character> B = followMap.get(value);
                    if(B.addAll(A)){//有不同则添加 并返回true
                        LinkedList<Character> road = new LinkedList<>();
                        road.add(Nv);
                        reCallFunction(relationMap,road,value);
                    }
                }
            }
        }
    }

    private void reCallFunction(HashMap<Character, TreeSet<Character>> relationMap, LinkedList<Character> road, Character value) {
        /**eg：relationMap内容 如A->E E->A结束；A->E E->T T->...
         P---[]
         A---[E]
         B---[T]
         C---[]
         T---[B, F]
         E---[A, T]
         F---[C, P]
         *  递归将follow集合的 值传递下去。使用road链表记录下穿过程,防止死循环
         */
        TreeSet<Character> relationSet = relationMap.get(value);
        if (!relationSet.isEmpty()){
            for (Character _2ndLevelValue:relationSet){
                    TreeSet<Character> A = followMap.get(value);
                    TreeSet<Character> B = followMap.get(_2ndLevelValue);
                    if(B.addAll(A) && !road.contains(_2ndLevelValue)){
                        road.add(_2ndLevelValue);
                        reCallFunction(relationMap,road,_2ndLevelValue);
                    }
            }
        }
    }


    /**
     * 获取Select集合
     * 对应于 预测分析表中内容
     * selectMap  <Character, HashMap<String, TreeSet<Character>>>
     *             非终结符  ->  Map<表达式，终结符集合>
     */
    public void initSelect() {
        // 遍历每一个表达式
        Set<Character> keySet = expressionMap.keySet();//非终结符
        for (Character selectKey : keySet) {
            ArrayList<String> arrayList = expressionMap.get(selectKey);
            HashMap<String, TreeSet<Character>> selectItemMap = new HashMap<String, TreeSet<Character>>();
            for (String selectExp : arrayList) {// 每一个表达式
                /**
                 * 存放select结果的集合
                 */
                TreeSet<Character> selectSet = new TreeSet<Character>();
                // set里存放的数据分3种情况,由selectExp决定
                // 1.A->ε,=follow(A)
                if (selectExp.charAt(0) == 'ε') {
                    selectSet = followMap.get(selectKey);
                    selectItemMap.put(selectExp, selectSet);
                }
                // 2.Nt开始,=Nt
                // <br>终结符开始
                else if (ntSet.contains(selectExp.charAt(0))) {
                    selectSet.add(selectExp.charAt(0));
                    selectSet.remove('ε');
                    selectItemMap.put(selectExp, selectSet);
                }
                // 3.Nv开始，=first(Nv)
                if (nvSet.contains(selectExp.charAt(0))) {
                    selectSet = firstMap.get(selectKey);
                    //添加完非终结符的first之后，判断集合里面是否还有ε
                    if(selectSet.contains('ε')){
                        //如果含有'ε'，还要添加当前非终结符的follow集合
                        for(Character item: followMap.get(selectKey)){
                            if(!selectSet.contains(item)){
                                selectSet.add(item);
                            }
                        }
                    }
                    selectSet.remove('ε');
                    selectItemMap.put(selectExp, selectSet);
                }
                selectMap.put(selectKey, selectItemMap);
            }
        }
    }



    /**
     * 查找产生式
     *
     * @param selectMap
     * @param peek
     *            当前Nv ，栈顶
     * @param charAt
     *            当前字符  句子第一位字符
     * @return
     */
    public static String findUseExp(HashMap<Character, HashMap<String, TreeSet<Character>>> selectMap, Character peek,
                                    char charAt) {
        try {
            HashMap<String, TreeSet<Character>> hashMap = selectMap.get(peek);//非终结符 对应的表达式集合
            Set<String> keySet = hashMap.keySet();//表达式集合
            for (String useExp : keySet) {
                TreeSet<Character> treeSet = hashMap.get(useExp);//表达式 对应的 终结符集
                if (treeSet.contains(charAt)) {
                    return useExp;
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    /***
     * 人间修改
     * HashMap<Character, HashMap<String, TreeSet<Character>>>
     */
    public void initTable(){
        table = new HashMap<Character, Map<Character, String>>();
        TreeSet<Character> tempNt = ntSet;
        tempNt.add('#');
        //获取非终结符集合
        Set<Character> vN = selectMap.keySet();
        HashMap<String, TreeSet<Character>> tempHash;
        //遍历非终结符
        for(Character vn:vN){
            TreeSet<Character> tempSet = new TreeSet<>();
            tempHash = selectMap.get(vn);
            //遍历表达式
            for(String key:tempHash.keySet()){
                for(Character item:tempHash.get(key)){
                    if(!tempSet.contains(item)){
                        tempSet.add(item);
                    }
                    else{
                        System.out.println("该文法不是LL(1)文法");
                        return;
                    }
                }

            }

        }

        for(Character vn:vN){
            Map<Character,String> rs = new HashMap<>();
            tempHash = selectMap.get(vn);
            //遍历终结符
            //HashMap<Character, HashMap<String, TreeSet<Character>>>
            //private HashMap<Character, Map<Character,String>> table;
            for(Character vt:tempNt){
                //遍历表达式
                for(String expression:tempHash.keySet()){
                    if (tempHash.get(expression).contains(vt)){
                        rs.put(vt,expression);
                    }
                }

            }
            table.put(vn,rs);
        }

    }

}
