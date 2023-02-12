import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class App extends JFrame {
    private JTextField up;
    private JTextField step;
    private JTextField down;
    private JButton deleteButton;
    private JButton addButton;
    private JButton calculateButton;
    private JPanel rootPanel;
    private JTable table;
    private DefaultTableModel model;
    public App(){
        setVisible(true);
        setSize(800,600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(rootPanel);
        model = (DefaultTableModel) table.getModel();
        model.addColumn("up");
        model.addColumn("step");
        model.addColumn("down");
        model.addColumn("result");

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String up = App.this.up.getText();
                String step = App.this.step.getText();
                String down = App.this.down.getText();
                model.addRow(new String[]{up,step,down,"0"});

            }
        });
    }
    public static void main (String[] args){
        new App();
    }
}