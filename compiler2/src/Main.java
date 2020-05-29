import view.MainFrame;
import model.Analyzer;
import controller.Controller;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

//        MainFrame mainFrame = new MainFrame();
//        mainFrame.setVisible(true);
        String text = null;
        Controller controller =new Controller();
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
        System.out.println(text);
        controller.analysisLang(text);
        System.out.println("请输出文法");
        String str = sc.next();
        controller.analysisStrByRenJian(str);

    }
}
