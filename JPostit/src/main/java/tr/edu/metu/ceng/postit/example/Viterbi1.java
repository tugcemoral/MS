package tr.edu.metu.ceng.postit.example;

/*
 * Viterbi.java
 * Toy Viterbi Decorder
 *
 * by Yusuke Shinyama <yusuke at cs . nyu . edu>
 *
 *   Permission to use, copy, modify, distribute this software 
 *   for any purpose is hereby granted without fee, provided 
 *   that the above copyright notice appear in all copies and
 *   that both that copyright notice and this permission notice
 *   appear in supporting documentation.
 */

import java.awt.*;
import java.util.*;
import java.text.*;
import java.awt.event.*;
import java.applet.*;

class Symbol {
	public String name;

	public Symbol(String s) {
		name = s;
	}
}

class SymbolTable {
	Hashtable table;

	public SymbolTable() {
		table = new Hashtable();
	}

	public Symbol intern(String s) {
		s = s.toLowerCase();
		Object sym = table.get(s);
		if (sym == null) {
			sym = new Symbol(s);
			table.put(s, sym);
		}
		return (Symbol) sym;
	}
}

class SymbolList {
	Vector list;

	public SymbolList() {
		list = new Vector();
	}

	public int size() {
		return list.size();
	}

	public void set(int index, Symbol sym) {
		list.setElementAt(sym, index);
	}

	public void add(Symbol sym) {
		list.addElement(sym);
	}

	public Symbol get(int index) {
		return (Symbol) list.elementAt(index);
	}
}

class IntegerList {
	Vector list;

	public IntegerList() {
		list = new Vector();
	}

	public int size() {
		return list.size();
	}

	public void set(int index, int i) {
		list.setElementAt(new Integer(i), index);
	}

	public void add(int i) {
		list.addElement(new Integer(i));
	}

	public int get(int index) {
		return ((Integer) list.elementAt(index)).intValue();
	}
}

class ProbTable {
	Hashtable table;

	public ProbTable() {
		table = new Hashtable();
	}

	public void put(Object obj, double prob) {
		table.put(obj, new Double(prob));
	}

	public double get(Object obj) {
		Double prob = (Double) table.get(obj);
		if (prob == null) {
			return 0.0;
		}
		return prob.doubleValue();
	}

	// normalize probability
	public void normalize() {
		double total = 0.0;
		for (Enumeration e = table.elements(); e.hasMoreElements();) {
			total += ((Double) e.nextElement()).doubleValue();
		}
		if (total == 0.0) {
			return; // div by zero!
		}
		for (Enumeration e = table.keys(); e.hasMoreElements();) {
			Object k = e.nextElement();
			double prob = ((Double) table.get(k)).doubleValue();
			table.put(k, new Double(prob / total));
		}
	}
}

class State {
	public String name;
	ProbTable emits;
	ProbTable linksto;

	public State(String s) {
		name = s;
		emits = new ProbTable();
		linksto = new ProbTable();
	}

	public void normalize() {
		emits.normalize();
		linksto.normalize();
	}

	public void addSymbol(Symbol sym, double prob) {
		emits.put(sym, prob);
	}

	public double emitprob(Symbol sym) {
		return emits.get(sym);
	}

	public void addLink(State st, double prob) {
		linksto.put(st, prob);
	}

	public double transprob(State st) {
		return linksto.get(st);
	}
}

class StateTable {
	Hashtable table;

	public StateTable() {
		table = new Hashtable();
	}

	public State get(String s) {
		s = s.toUpperCase();
		State st = (State) table.get(s);
		if (st == null) {
			st = new State(s);
			table.put(s, st);
		}
		return st;
	}
}

class StateIDTable {
	Hashtable table;

	public StateIDTable() {
		table = new Hashtable();
	}

	public void put(State obj, int i) {
		table.put(obj, new Integer(i));
	}

	public int get(State obj) {
		Integer i = (Integer) table.get(obj);
		if (i == null) {
			return 0;
		}
		return i.intValue();
	}
}

class StateList {
	Vector list;

	public StateList() {
		list = new Vector();
	}

	public int size() {
		return list.size();
	}

	public void set(int index, State st) {
		list.setElementAt(st, index);
	}

	public void add(State st) {
		list.addElement(st);
	}

	public State get(int index) {
		return (State) list.elementAt(index);
	}
}

class HMMCanvas extends Canvas {
	static final int grid_x = 60;
	static final int grid_y = 40;
	static final int offset_x = 70;
	static final int offset_y = 30;
	static final int offset_y2 = 10;
	static final int offset_y3 = 65;
	static final int col_x = 40;
	static final int col_y = 10;
	static final int state_r = 10;
	static final Color state_fill = Color.white;
	static final Color state_fill_maximum = Color.yellow;
	static final Color state_fill_best = Color.red;
	static final Color state_boundery = Color.black;
	static final Color link_normal = Color.green;
	static final Color link_processed = Color.blue;
	static final Color link_maximum = Color.red;

	HMMDecoder hmm;

	public HMMCanvas() {
		setBackground(Color.white);
		setSize(400, 300);
	}

	public void setHMM(HMMDecoder h) {
		hmm = h;
	}

	private void drawState(Graphics g, int x, int y, Color c) {
		x = x * grid_x + offset_x;
		y = y * grid_y + offset_y;
		g.setColor(c);
		g.fillOval(x - state_r, y - state_r, state_r * 2, state_r * 2);
		g.setColor(state_boundery);
		g.drawOval(x - state_r, y - state_r, state_r * 2, state_r * 2);
	}

	private void drawLink(Graphics g, int x, int y0, int y1, Color c) {
		int x0 = grid_x * x + offset_x;
		int x1 = grid_x * (x + 1) + offset_x;
		y0 = y0 * grid_y + offset_y;
		y1 = y1 * grid_y + offset_y;
		g.setColor(c);
		g.drawLine(x0, y0, x1, y1);
	}

	private void drawCenterString(Graphics g, String s, int x, int y) {
		x = x - g.getFontMetrics().stringWidth(s) / 2;
		g.setColor(Color.black);
		g.drawString(s, x, y + 5);
	}

	private void drawRightString(Graphics g, String s, int x, int y) {
		x = x - g.getFontMetrics().stringWidth(s);
		g.setColor(Color.black);
		g.drawString(s, x, y + 5);
	}

	public void paint(Graphics g) {
		if (hmm == null) {
			return;
		}
		DecimalFormat form = new DecimalFormat("0.0000");
		int nsymbols = hmm.symbols.size();
		int nstates = hmm.states.size();
		// complete graph.
		for (int i = 0; i < nsymbols; i++) {
			int offset_ymax = offset_y2 + nstates * grid_y;
			if (i < nsymbols - 1) {
				for (int y1 = 0; y1 < nstates; y1++) {
					for (int y0 = 0; y0 < nstates; y0++) {
						Color c = link_normal;
						if (hmm.stage == i + 1 && hmm.i0 == y0 && hmm.i1 == y1) {
							c = link_processed;
						}
						if (hmm.matrix_prevstate[i + 1][y1] == y0) {
							c = link_maximum;
						}
						drawLink(g, i, y0, y1, c);
						if (c == link_maximum && 0 < i) {
							double transprob = hmm.states.get(y0).transprob(
									hmm.states.get(y1));
							drawCenterString(g, form.format(transprob),
									offset_x + i * grid_x + grid_x / 2,
									offset_ymax);
							offset_ymax = offset_ymax + 16;
						}
					}
				}
			}
			// state circles.
			for (int y = 0; y < nstates; y++) {
				Color c = state_fill;
				if (hmm.matrix_prevstate[i][y] != -1) {
					c = state_fill_maximum;
				}
				if (hmm.sequence.size() == nsymbols
						&& hmm.sequence.get(nsymbols - 1 - i) == y) {
					c = state_fill_best;
				}
				drawState(g, i, y, c);
			}
		}
		// max probability.
		for (int i = 0; i < nsymbols; i++) {
			for (int y1 = 0; y1 < nstates; y1++) {
				if (hmm.matrix_prevstate[i][y1] != -1) {
					drawCenterString(g, form.format(hmm.matrix_maxprob[i][y1]),
							offset_x + i * grid_x, offset_y + y1 * grid_y);
				}
			}
		}

		// captions (symbols atop)
		for (int i = 0; i < nsymbols; i++) {
			drawCenterString(g, hmm.symbols.get(i).name, offset_x + i * grid_x,
					col_y);
		}
		// captions (states in left)
		for (int y = 0; y < nstates; y++) {
			drawRightString(g, hmm.states.get(y).name, col_x, offset_y + y
					* grid_y);
		}

		// status bar
		g.setColor(Color.black);
		g.drawString(hmm.status, col_x, offset_y3 + nstates * grid_y);
		g.drawString(hmm.status2, col_x, offset_y3 + nstates * grid_y + 16);
	}
}

class HMMDecoder {
	StateList states;
	int state_start;
	int state_end;

	public IntegerList sequence;
	public double[][] matrix_maxprob;
	public int[][] matrix_prevstate;
	public SymbolList symbols;
	public double probmax;
	public int stage, i0, i1;
	public boolean laststage;
	public String status, status2;

	public HMMDecoder() {
		status = "Not initialized.";
		status2 = "";
		states = new StateList();
	}

	public void addStartState(State st) {
		state_start = states.size(); // get current index
		states.add(st);
	}

	public void addNormalState(State st) {
		states.add(st);
	}

	public void addEndState(State st) {
		state_end = states.size(); // get current index
		states.add(st);
	}

	// for debugging.
	public void showmatrix() {
		for (int i = 0; i < symbols.size(); i++) {
			for (int j = 0; j < states.size(); j++) {
				System.out.print(matrix_maxprob[i][j] + " "
						+ matrix_prevstate[i][j] + ", ");
			}
			System.out.println();
		}
	}

	// initialize for decoding
	public void initialize(SymbolList syms) {
		// symbols[syms.length] should be END
		symbols = syms;
		matrix_maxprob = new double[symbols.size()][states.size()];
		matrix_prevstate = new int[symbols.size()][states.size()];
		for (int i = 0; i < symbols.size(); i++) {
			for (int j = 0; j < states.size(); j++) {
				matrix_prevstate[i][j] = -1;
			}
		}

		State start = states.get(state_start);
		for (int i = 0; i < states.size(); i++) {
			matrix_maxprob[0][i] = start.transprob(states.get(i));
			matrix_prevstate[0][i] = 0;
		}

		stage = 0;
		i0 = -1;
		i1 = -1;
		sequence = new IntegerList();
		status = "Ok, let's get started...";
		status2 = "";
	}

	// forward procedure
	public boolean proceed_decoding() {
		status2 = "";
		// already end?
		if (symbols.size() <= stage) {
			return false;
		}
		// not started?
		if (stage == 0) {
			stage = 1;
			i0 = 0;
			i1 = 0;
			matrix_maxprob[stage][i1] = 0.0;
		} else {
			i0++;
			if (states.size() <= i0) {
				// i0 should be reinitialized.
				i0 = 0;
				i1++;
				if (states.size() <= i1) {
					// i1 should be reinitialized.
					// goto next stage.
					stage++;
					if (symbols.size() <= stage) {
						// done.
						status = "Decoding finished.";
						return false;
					}
					laststage = (stage == symbols.size() - 1);
					i1 = 0;
				}
				matrix_maxprob[stage][i1] = 0.0;
			}
		}

		// sym1: next symbol
		Symbol sym1 = symbols.get(stage);
		State s0 = states.get(i0);
		State s1 = states.get(i1);

		// precond: 1 <= stage.
		double prob = matrix_maxprob[stage - 1][i0];
		DecimalFormat form = new DecimalFormat("0.0000");
		status = "Prob:" + form.format(prob);

		if (1 < stage) {
			// skip first stage.
			double transprob = s0.transprob(s1);
			prob = prob * transprob;
			status = status + " x " + form.format(transprob);
		}

		double emitprob = s1.emitprob(sym1);
		prob = prob * emitprob;
		status = status + " x " + form.format(emitprob) + "(" + s1.name + ":"
				+ sym1.name + ")";

		status = status + " = " + form.format(prob);
		// System.out.println("stage: "+stage+", i0:"+i0+", i1:"+i1+", prob:"+prob);

		if (matrix_maxprob[stage][i1] < prob) {
			matrix_maxprob[stage][i1] = prob;
			matrix_prevstate[stage][i1] = i0;
			status2 = "(new maximum found)";
		}

		return true;
	}

	// backward proc
	public void backward() {
		int probmaxstate = state_end;
		sequence.add(probmaxstate);
		for (int i = symbols.size() - 1; 0 < i; i--) {
			probmaxstate = matrix_prevstate[i][probmaxstate];
			if (probmaxstate == -1) {
				status2 = "Decoding failed.";
				return;
			}
			sequence.add(probmaxstate);
			// System.out.println("stage: "+i+", state:"+probmaxstate);
		}
	}
}

public class Viterbi1 extends Applet implements ActionListener, Runnable {
	SymbolTable symtab;
	StateTable sttab;
	HMMDecoder myhmm = null;
	HMMCanvas canvas;
	Panel p;
	TextArea hmmdesc;
	TextField sentence;
	Button bstart, bskip;
	static final String initialHMM = "start: go(cow,1.0)\n"
			+ "cow: emit(moo,0.9) emit(hello,0.1) go(cow,0.5) go(duck,0.3) go(end,0.2)\n"
			+ "duck: emit(quack,0.6) emit(hello,0.4) go(duck,0.5) go(cow,0.3) go(end,0.2)\n";

	final int sleepmillisec = 100; // 0.1s

	// setup hmm
	// success:true.
	boolean setupHMM(String s) {
		myhmm = new HMMDecoder();
		symtab = new SymbolTable();
		sttab = new StateTable();

		State start = sttab.get("start");
		State end = sttab.get("end");
		myhmm.addStartState(start);

		boolean success = true;
		StringTokenizer lines = new StringTokenizer(s, "\n");
		while (lines.hasMoreTokens()) {
			// foreach line.
			String line = lines.nextToken();
			int i = line.indexOf(':');
			if (i == -1)
				break;
			State st0 = sttab.get(line.substring(0, i).trim());
			if (st0 != start && st0 != end) {
				myhmm.addNormalState(st0);
			}
			// System.out.println(st0.name+":"+line.substring(i+1));

			StringTokenizer tokenz = new StringTokenizer(line.substring(i + 1),
					", ");
			while (tokenz.hasMoreTokens()) {
				// foreach token.
				String t = tokenz.nextToken().toLowerCase();
				if (t.startsWith("go(")) {
					State st1 = sttab.get(t.substring(3).trim());
					// fetch another token.
					if (!tokenz.hasMoreTokens()) {
						success = false; // err. nomoretoken
						break;
					}
					String n = tokenz.nextToken().replace(')', ' ');
					double prob;
					try {
						prob = Double.valueOf(n).doubleValue();
					} catch (NumberFormatException e) {
						success = false; // err.
						prob = 0.0;
					}
					st0.addLink(st1, prob);
					// System.out.println("go:"+st1.name+","+prob);
				} else if (t.startsWith("emit(")) {
					Symbol sym = symtab.intern(t.substring(5).trim());
					// fetch another token.
					if (!tokenz.hasMoreTokens()) {
						success = false; // err. nomoretoken
						break;
					}
					String n = tokenz.nextToken().replace(')', ' ');
					double prob;
					try {
						prob = Double.valueOf(n).doubleValue();
					} catch (NumberFormatException e) {
						success = false; // err.
						prob = 0.0;
					}
					st0.addSymbol(sym, prob);
					// System.out.println("emit:"+sym.name+","+prob);
				} else {
					// illegal syntax, just ignore
					break;
				}
			}

			st0.normalize(); // normalize probability
		}

		end.addSymbol(symtab.intern("end"), 1.0);
		myhmm.addEndState(end);

		return success;
	}

	// success:true.
	boolean setup() {
		if (!setupHMM(hmmdesc.getText()))
			return false;

		// initialize words
		SymbolList words = new SymbolList();
		StringTokenizer tokenz = new StringTokenizer(sentence.getText());
		words.add(symtab.intern("start"));
		while (tokenz.hasMoreTokens()) {
			words.add(symtab.intern(tokenz.nextToken()));
		}
		words.add(symtab.intern("end"));
		myhmm.initialize(words);
		canvas.setHMM(myhmm);
		return true;
	}

	public void init() {
		canvas = new HMMCanvas();

		setLayout(new BorderLayout());
		p = new Panel();
		sentence = new TextField("moo hello quack", 20);
		bstart = new Button("  Start  ");
		bskip = new Button("Auto");
		bstart.addActionListener(this);
		bskip.addActionListener(this);
		p.add(sentence);
		p.add(bstart);
		p.add(bskip);
		hmmdesc = new TextArea(initialHMM, 4, 20);
		add("North", canvas);
		add("Center", p);
		add("South", hmmdesc);

	}

	void setup_fallback() {
		// adjustable
		State cow = sttab.get("cow");
		State duck = sttab.get("duck");
		State end = sttab.get("end");

		cow.addLink(cow, 0.5);
		cow.addLink(duck, 0.3);
		cow.addLink(end, 0.2);
		duck.addLink(cow, 0.3);
		duck.addLink(duck, 0.5);
		duck.addLink(end, 0.2);

		cow.addSymbol(symtab.intern("moo"), 0.9);
		cow.addSymbol(symtab.intern("hello"), 0.1);
		duck.addSymbol(symtab.intern("quack"), 0.6);
		duck.addSymbol(symtab.intern("hello"), 0.4);
	}

	public void destroy() {
		remove(p);
		remove(canvas);
	}

	public void processEvent(AWTEvent e) {
		if (e.getID() == Event.WINDOW_DESTROY) {
			System.exit(0);
		}
	}

	public void run() {
		if (myhmm != null) {
			while (myhmm.proceed_decoding()) {
				canvas.repaint();
				try {
					Thread.sleep(sleepmillisec);
				} catch (InterruptedException e) {
					;
				}
			}
			myhmm.backward();
			canvas.repaint();
			bstart.setLabel("  Start  ");
			bstart.setEnabled(true);
			bskip.setEnabled(true);
			myhmm = null;
		}
	}

	public void actionPerformed(ActionEvent ev) {
		String label = ev.getActionCommand();

		if (label.equalsIgnoreCase("  start  ")) {
			if (!setup()) {
				// error
				return;
			}
			bstart.setLabel("Proceed");
			canvas.repaint();
		} else if (label.equalsIgnoreCase("proceed")) {
			// next
			if (!myhmm.proceed_decoding()) {
				myhmm.backward();
				bstart.setLabel("  Start  ");
				myhmm = null;
			}
			canvas.repaint();
		} else if (label.equalsIgnoreCase("auto")) {
			// skip
			if (myhmm == null) {
				if (!setup()) {
					// error
					return;
				}
			}
			bstart.setEnabled(false);
			bskip.setEnabled(false);
			Thread me = new Thread(this);
			me.setPriority(Thread.MIN_PRIORITY);
			// start animation.
			me.start();
		}
	}

	public static void main(String args[]) {
		Frame f = new Frame("Viterbi");
		Viterbi1 v = new Viterbi1();
		f.add("Center", v);
		f.setSize(400, 400);
		f.show();
		v.init();
		v.start();
	}

	public String getAppletInfo() {
		return "A Sample Viterbi Decoder Applet";
	}
}