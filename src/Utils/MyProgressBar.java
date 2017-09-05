package Utils;

import javax.swing.*;
import java.awt.*;

/**
 * Created by mohab 2 on 21/07/2017.
 */
public class MyProgressBar {
    public static final JProgressBar jProgressBar= new JProgressBar();
    public static final JLabel status = new JLabel("Connecting...");
    public static JFrame frame;
    public MyProgressBar() {
        frame = new JFrame("جاري التنفيذ");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new FlowLayout());
        frame.setLocationRelativeTo(null);
        frame.add(status);
        frame.add("jProgressBar", jProgressBar);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dim.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dim.getHeight() - frame.getHeight()) / 2);

        frame.setLocation(x, y);
        frame.pack();
        frame.setAlwaysOnTop(true);
        frame.setVisible(true);

    }

    public static void disposeTheBar(){
        frame.setVisible(false);
        frame.dispose();
    }
}
