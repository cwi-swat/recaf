package generated;

import javax.swing.*;
import recaf.demo.swul.SWUL;

public class TestSWUL {

   static SWUL<JPanel> alg = new SWUL<JPanel>();

     JPanel  example1() {
  return (JPanel )alg.Method(alg.Panel(alg.Seq(alg.Label(alg.Text(() -> "Welcome!")), alg.Panel(alg.Border(alg.Seq(alg.Center(alg.Label(alg.Text(() -> "Hello world!"))), alg.South(alg.Panel(alg.Grid(java.util.Arrays.asList(alg.Row(() -> 2, alg.Seq(alg.Button(alg.Seq(alg.Text(() -> "cancel"), alg.Action(alg.ExpStat(() -> { System.out.println("Cancel"); return null; })))), alg.Button(alg.Text(() -> "ok"))))))))))))));
}

     JPanel  gridBagTest() {
  return (JPanel )alg.Method(alg.Panel(alg.Border(alg.Center(alg.Panel(alg.GridBag(java.util.Arrays.asList(alg.Row(() -> 4, alg.Seq(alg.Cell(alg.Button(alg.Text(() -> "Button1"))), alg.Cell(() -> 3, alg.Button(alg.Text(() -> "Button2"))))), alg.Row(() -> 4, alg.Seq(alg.Cell(() -> 2, alg.Button(alg.Text(() -> "Button3"))), alg.Seq(alg.None(), alg.Cell(() -> 1, () -> 3, alg.Button(alg.Text(() -> "Button4")))))), alg.Row(() -> 4, alg.Seq(alg.None(), alg.Seq(alg.Cell(alg.Button(alg.Text(() -> "Button5"))), alg.Cell(alg.Button(alg.Text(() -> "Button6")))))), alg.Row(() -> 4, alg.Seq(alg.Cell(() -> 2,() -> 2, alg.Button(alg.Text(() -> "Button7"))), alg.Cell(alg.Button(alg.Text(() -> "Button8"))))), alg.Row(() -> 4, alg.Seq(alg.None(), alg.Seq(alg.None(), alg.Cell(() -> 2, alg.Button(alg.Text(() -> "Button9")))))))))))));
}

     JPanel  widgetORama() {
  return (JPanel )alg.Method(alg.Decl(() -> new ButtonGroup(), (recaf.core.Ref<ButtonGroup > radiogroup1) -> {return alg.Decl(() -> new ButtonGroup(), (recaf.core.Ref<ButtonGroup > someGroup) -> {return alg.Panel(alg.Border(alg.Seq(alg.Border(() -> BorderFactory.createRaisedBevelBorder()), alg.Seq(alg.North(alg.ScrollPane(alg.Panel(alg.Grid(java.util.Arrays.asList(alg.Row(() -> 2, alg.Seq(alg.TextArea(alg.Seq(alg.Rows(() -> 10), alg.Seq(alg.Columns(() -> 10), alg.Text(() -> "A 10 by 10 textarea")))), alg.Tree(alg.Empty())))))))), alg.Center(alg.Panel(alg.Grid(java.util.Arrays.asList(alg.Row(() -> 3, alg.Seq(alg.Label(alg.Text(() -> "This an JLabel")), alg.Seq(alg.Button(alg.Text(() -> "This is a JButton")), alg.Slider(alg.Empty())))), alg.Row(() -> 3, alg.Seq(alg.RadioButton(alg.Seq(alg.Text(() -> "JRadioButton in a group"), alg.Seq(alg.Group(() -> radiogroup1.value), alg.Selected(() -> true)))), alg.Seq(alg.TextField(alg.Text(() -> "a JTextField")), alg.ProgressBar(alg.Value(() -> 23))))), alg.Row(() -> 3, alg.Seq(alg.CheckBox(alg.Text(() -> "a JCheckBox in a group")), alg.Seq(alg.ComboBox(alg.Empty()), alg.ToggleButton(alg.Seq(alg.Text(() -> "a JToggleButton"), alg.Group(() -> someGroup.value)))))), alg.Row(() -> 3, alg.Seq(alg.RadioButton(alg.Seq(alg.Text(() -> "another JRadioButton"), alg.Group(() -> radiogroup1.value))), alg.Seq(alg.Label(alg.Empty()), alg.ToggleButton(alg.Seq(alg.Text(() -> "another JToggleButton"), alg.Seq(alg.Group(() -> someGroup.value), alg.Selected(() -> true)))))))))))))));});}));
}


   public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Welcome!");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JPanel panel = new JPanel();
            TestSWUL swul = new TestSWUL();
            panel.add(swul.example1());
            panel.add(swul.gridBagTest());
            panel.add(swul.widgetORama());
            frame.setContentPane(panel);
            frame.pack();
            frame.setVisible(true);
        });
    }
}