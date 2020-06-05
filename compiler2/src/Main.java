import view.MainFrame;
import model.Analyzer;
import controller.Controller;

import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

//        MainFrame mainFrame = new MainFrame();
//        mainFrame.setVisible(true);
        String text = null;
        Controller controller =new Controller();
        File file = new File("E://YuFa//Compiler-master//compiler2//data.txt");

        if(! file.exists()){
            System.out.println("对不起，不包含指定路径的文件");
        }else{
            //根据指定路径的File对象创建FileReader对象
            try {
                FileReader fr = new FileReader(file);

                char[] data = new char[100];			//定义char数组

                int length = 0;

                while((length = fr.read(data))>0){			//循环读取文件中的数据
                    String str = new String(data,0,length);			//根据读取文件的内容创建String 对象
                    System.out.println(str);
                    controller.analysisLang(str);//输出读取内容
                }
                fr.close();								//关闭流



            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        Scanner sc  =new Scanner(System.in);

        text="E->TG\n" +
                "G->+TG\n" +
                "G->-TG\n" +
                "G->ε\n" +
                "T->FS\n" +
                "S->*FS\n" +
                "S->/FS\n" +
                "S->ε\n" +
                "F->(E)\n" +
                "F->i";
//        System.out.println(text);
//        controller.analysisLang(text);
//        System.out.println(str);
        System.out.println("请输出文法");
        String str2 = sc.next();
        controller.analysisStrByRenJian(str2);

    }
}
