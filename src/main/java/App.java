import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.html.parser.Parser;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;


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
        model.addColumn("down");
        model.addColumn("step");
        model.addColumn("result");

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String up = App.this.up.getText();
                String down = App.this.down.getText();
                String step = App.this.step.getText();
                model.addRow(new String[]{up,down,step,"0"});
                App.this.up.setText("");
                App.this.down.setText("");
                App.this.step.setText("");
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int temp = table.getSelectedRow();
                if (temp != -1){
                model.removeRow(temp);
                }
            }
        });
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int temp = table.getSelectedRow();
                if (temp == -1){
                    return;
                }
                Vector data = model.getDataVector().get(temp);
                float max = Float.parseFloat((String) data.get(0));
                float min = Float.parseFloat((String) data.get(1));
                float step = Float.parseFloat((String) data.get(2));
                double result = 0;
                for (float i = min;i < max-step;i += step){
                    if ( i>max ){
                        result += (Math.cos(i-step) + Math.cos(max))/2*step;
                    }
                    else{
                        result += (Math.cos(i) + Math.cos(i+step))/2*step;
                }
                model.setValueAt(result,temp,3);
            }
        });

    }

    public static void main (String[] args){
        new App();
    }

    private void createUIComponents() {
        table = new JTable(){
            @Override
            public boolean isCellEditable(int row,int column){
                return column != 3;
            }
        };
    }
}