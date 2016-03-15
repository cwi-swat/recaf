package recaf.demo.swul;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayDeque;
import java.util.List;
import java.util.function.Supplier;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JTree;
import javax.swing.border.Border;

import recaf.core.alg.JavaMethodAlg;
import recaf.core.direct.IExec;
import recaf.core.direct.StmtJavaDirect;

public class SWUL<R extends JComponent> implements StmtJavaDirect<R>, JavaMethodAlg<R, IExec> {
	private ArrayDeque<JComponent> componentStack = new ArrayDeque<>();
	private ArrayDeque<LayoutManager> layoutStack = new ArrayDeque<>();
	private ArrayDeque<Object> layoutAttrs = new ArrayDeque<>();
	
	private JComponent result;
	
	@Override
	public R Method(IExec body) {
		try {
			body.exec(null);
			return (R) result;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
	
	private void push(JComponent comp) {
		if (componentStack.isEmpty()) {
			this.result = comp;
		}
		componentStack.push(comp);
	}
	
	private void pushLayout(LayoutManager l) {
		layoutStack.push(l);
	}
	
	private void popLayout() {
		layoutStack.pop();
	}
	
	private JComponent pop() {
		return componentStack.pop();
	}
	
	private void add(JComponent c) {
		if (componentStack.isEmpty()) {
			// root component
			return; 
		}
		
		JComponent parent = componentStack.peek();
		if (layoutStack.peek() instanceof BorderLayout) {
//			System.out.println("ADDING with layout: " + c);
			if (parent instanceof JScrollPane) {
				((JScrollPane)parent).setViewportView(c);
			}
			else {
				parent.add(c, layoutAttrs.peek());
			}
		}
		else {
			parent.add(c);
		}
	}
	
	
	public IExec Action(IExec body) {
		return l -> {
			JButton button = (JButton)componentStack.peek();
			button.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						body.exec(null);
					} catch (Throwable e1) {
						throw new RuntimeException(e1);
					}
				}
			});
		};
	}
	
	private void widget(JComponent c, IExec body) throws Throwable {
		push(c);
		body.exec(null);
		add(pop());
	}
	
	public IExec Button(IExec body) {
		return l -> { widget(new JButton(), body); };
	}
	
	public IExec Panel(IExec body) {
		return l -> { widget(new JPanel(), body); };
	}
	
	public IExec ScrollPane(IExec body) {
		return l -> { widget(new JScrollPane(), body); };
	}
	
	public IExec Label(IExec body) {
		return l -> { widget(new JLabel(), body); }; 
	}

	public IExec CheckBox(IExec body) {
		return l -> { widget(new JCheckBox(), body); };
	}

	public IExec ComboBox(IExec body) {
		return l -> { widget(new JComboBox(), body); };
	}

	public IExec RadioButton(IExec body) {
		return l -> { widget(new JRadioButton(), body); };
	}
	
	public IExec Slider(IExec body) {
		return l -> { widget(new JSlider(), body); };
	}
	
	public IExec TextField(IExec body) {
		return l -> { widget(new JTextField(), body); };
	}
	
	public IExec TextArea(IExec body) {
		return l -> { widget(new JTextArea(), body); };
	}
	
	public IExec ProgressBar(IExec body) {
		return l -> { widget(new JProgressBar(), body); };
	}
	
	public IExec ToggleButton(IExec body) {
		return l -> { widget(new JToggleButton(), body); };
	}

	public IExec Tree(IExec body) {
		return l -> { widget(new JTree(), body); };
	}




	
	
	// Border layout
	public IExec Border(IExec body) {
		return l -> {
			BorderLayout layout = new BorderLayout();
			componentStack.peek().setLayout(layout);
			pushLayout(layout);
			body.exec(null);
			popLayout();
		};
	}
	
	private void borderAttr(String attr, IExec body) throws Throwable {
		layoutAttrs.push(attr);
		body.exec(null);
		layoutAttrs.pop();
	}
	
	public IExec Center(IExec body) {
		return l -> { borderAttr(BorderLayout.CENTER, body); };
	}
	
	public IExec North(IExec body) {
		return l -> { borderAttr(BorderLayout.NORTH, body); };
	}
	
	public IExec South(IExec body) {
		return l -> { borderAttr(BorderLayout.SOUTH, body); };
	}

	public IExec East(IExec body) {
		return l -> { borderAttr(BorderLayout.EAST, body); };
	}
	
	public IExec West(IExec body) {
		return l -> { borderAttr(BorderLayout.WEST, body); };
	}

	public class Row {
		private int cols;
		private IExec body;

		public Row(Integer integer, IExec body) {
			this.cols = cols;
			this.body = body;
		}
	}
	
	// Gridlayout
	public IExec Grid(List<Row> rows) {
		return l -> {
			GridLayout layout = new GridLayout(rows.size(), rows.get(0).cols);
			componentStack.peek().setLayout(layout);
			pushLayout(layout);
			for (Row row: rows) {
				row.body.exec(null);
			}
			popLayout();
		};
	}
	
	public Row Row(Supplier<Integer> cols, IExec body) {
		return new Row(cols.get(), body);
	}
	
	// Properties
	
	
	public IExec Value(Supplier<Object> v) {
		return l -> {
			JComponent comp = componentStack.peek();
			Object value = v.get();
			if (comp instanceof JProgressBar) {
				((JProgressBar)comp).setValue((int)value);
			}
			else {
				throw new RuntimeException("Cannot set text of " + comp);
			}
		};
	}
	
	public IExec Rows(Supplier<Integer> rows) {
		return l -> {
			((JTextArea)componentStack.peek()).setRows(rows.get());
		};
	}
	
	public IExec Columns(Supplier<Integer> cols) {
		return l -> {
			((JTextArea)componentStack.peek()).setColumns(cols.get());
		};
	}
	
	public IExec Selected(Supplier<Boolean> b) {
		return l -> {
			JComponent comp = componentStack.peek();
			Boolean val = b.get();
			if (comp instanceof JCheckBox) {
				((JCheckBox)comp).setSelected(val);
			}
			else if (comp instanceof JRadioButton) {
				((JRadioButton)comp).setSelected(val);
			}
			else if (comp instanceof JToggleButton) {
				((JToggleButton)comp).setSelected(val);
			}
			else {
				throw new RuntimeException("Cannot set selected of " + comp);
			}
		};
	}
	
	public IExec Text(Supplier<String> l) {
		return l0 -> {
			// TODO: deal with all kinds that support text.
			JComponent comp = componentStack.peek();
			String txt = l.get();
			if (comp instanceof JLabel) {
				((JLabel)comp).setText(txt);
			}
			else if (comp instanceof JButton) {
				((JButton)comp).setText(txt);
			}
			else if (comp instanceof JRadioButton) {
				((JRadioButton)comp).setText(txt);
			}
			else if (comp instanceof JCheckBox) {
				((JCheckBox)comp).setText(txt);
			}
			else if (comp instanceof JTextField) {
				((JTextField)comp).setText(txt);
			}
			else if (comp instanceof JTextArea) {
				((JTextArea)comp).setText(txt);
			}
			else if (comp instanceof JToggleButton) {
				((JToggleButton)comp).setText(txt);
			}
			else {
				throw new RuntimeException("Cannot set text of " + comp);
			}
		};
	}
	
	public IExec Border(Supplier<Border> b) {
		return l -> componentStack.peek().setBorder(b.get());
	}
	
	public IExec Group(Supplier<ButtonGroup> group) {
		return l -> {
			group.get().add((AbstractButton) componentStack.peek());
		};
	}
	
}
