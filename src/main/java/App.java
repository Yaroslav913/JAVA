import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;


public class App extends JFrame {
    private JTextField up;
    private JTextField step;
    private JTextField down;
    private JButton deleteButton;
    private JButton addButton;
    private JButton calculateButton;
    private JPanel rootPanel;
    private JTable table;
    private JButton deleteTableButton;
    private JButton restoreButton;
    private JButton saveTextButton;
    private JButton loadingTextButton;
    private JButton saveBinaryButton;
    private JButton loadingBinaryButton;
    private DefaultTableModel model;
    private List<RecIntegral> data = new ArrayList<>();

    public App() {
        setVisible(true);
        setSize(800, 600);
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
                try {
                    if ((Float.parseFloat(up) < 0.000001) || (Float.parseFloat(up) > 1000000) || (Float.parseFloat(down) < 0.000001) || (Float.parseFloat(down) > 1000000) || (Float.parseFloat(step) < 0.000001) || (Float.parseFloat(step) > 1000000)) {
                        throw new ExceptionInput("wrong input");
                    }
                } catch (ExceptionInput ex) {
                    App.this.up.setText("");
                    App.this.down.setText("");
                    App.this.step.setText("");
                    new ExceptionWarning();
                    return;
                }
                try {
                    if (Float.parseFloat(up) < Float.parseFloat(down)) {
                        throw new ExceptionInput("wrong input");
                    }
                } catch (ExceptionInput ex) {
                    App.this.up.setText("");
                    App.this.down.setText("");
                    App.this.step.setText("");
                    new ExceptionUpDown();
                    return;
                }
                model.addRow(new String[]{up, down, step, "0"});
                App.this.up.setText("");
                App.this.down.setText("");
                App.this.step.setText("");
                data.add(new RecIntegral(Arrays.stream(new String[]{up, down, step, "0"}).toList()));
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int temp = table.getSelectedRow();
                if (temp != -1) {
                    model.removeRow(temp);
                    data.remove(temp);
                }
            }
        });
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int temp = table.getSelectedRow();
                if (temp == -1) {
                    return;
                }
                Vector localData = model.getDataVector().get(temp);
                float max = Float.parseFloat((String) localData.get(0));
                float min = Float.parseFloat((String) localData.get(1));
                float step = Float.parseFloat((String) localData.get(2));
                double result = 0;
                for (float i = min; i < max - step; i += step) {
                    if (i > max) {
                        result += (Math.cos(i - step) + Math.cos(max)) / 2 * step;
                    } else {
                        result += (Math.cos(i) + Math.cos(i + step)) / 2 * step;
                    }
                }
                model.setValueAt(result, temp, 3);
                data.get(temp).setDataByIndex(3, String.valueOf(result));
            }
        });

        restoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int temp = table.getRowCount();
                for (int i = 0; i < temp; i++) {
                    model.removeRow(0);
                }
                for (RecIntegral element : data) {
                    model.addRow(element.getRecord().toArray());
                }
            }
        });

        deleteTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int temp = table.getRowCount();
                for (int i = 0; i < temp; i++) {
                    model.removeRow(0);
                }
            }
        });
        model.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    data.get(table.getSelectedRow()).setDataByIndex(table.getSelectedColumn(),
                            (String) model.getDataVector().get(table.getSelectedRow()).get(table.getSelectedColumn()));
                }
            }
        });
        saveBinaryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ObjectOutputStream out = null;
                int temp = table.getSelectedRow();
                if (temp == -1) {
                    return;
                }
                try {
                    out = new ObjectOutputStream(new BufferedOutputStream(
                            new FileOutputStream("BinaryStringNumber" + temp + ".txt")));
                    out.writeObject(data.get(temp));
                    out.close();
                } catch (IOException ignored) {
                }
            }
        });
        loadingBinaryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                fileChooser.showOpenDialog(null);
                ObjectInputStream in = null;
                RecIntegral restObj = null;
                int temp = table.getRowCount();
                if (temp != -1 ){
                    for (int i = 0; i < temp; i++) {
                        model.removeRow(0);
                    }}
                try {
                    in = new ObjectInputStream(new BufferedInputStream(
                            new FileInputStream(fileChooser.getSelectedFile().getAbsolutePath())));
                    restObj = (RecIntegral) in.readObject();
                    data.add(restObj);
                    model.addRow(restObj.getRecord().toArray());
                }
                catch (IOException | ClassNotFoundException ex ) {
                    ex.printStackTrace();
                }
            }
        });
        saveTextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ObjectOutputStream out = null;
                int temp = table.getSelectedRow();
                if (temp == -1) {
                    return;
                }
                try {
                    out = new ObjectOutputStream(new BufferedOutputStream(
                            new FileOutputStream("TextStringNumber" + temp + ".txt")));
                    out.writeObject(data.get(temp).toString());
                    out.close();
                } catch (IOException ignored) {
                }
            }
        });
        loadingTextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getDefaultDirectory());
                fileChooser.showOpenDialog(null);
                ObjectInputStream in = null;
                RecIntegral restObj = null;
                int temp = table.getRowCount();
                if (temp != -1 ){
                    for (int i = 0; i < temp; i++) {
                        model.removeRow(0);
                    }}
                try {
                    in = new ObjectInputStream(new BufferedInputStream(
                            new FileInputStream(fileChooser.getSelectedFile().getAbsolutePath())));
                    restObj = RecIntegral.fromString((String) in.readObject());
                    data.add(restObj);
                    model.addRow(restObj.getRecord().toArray());

                }
                catch (IOException | ClassNotFoundException ex ) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
        new App();
    }

    private void createUIComponents() {
        table = new JTable() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 3;
            }
        };
    }
}