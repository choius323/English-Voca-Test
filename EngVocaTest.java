import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;

public class EngVocaTest extends JFrame implements ActionListener {

	JTextArea result = new JTextArea(); // ��� ��� �κ�

	final String FN = "WordPad.txt";
	public JTextField Eng; // ���� �Է� �κ�
	public JTextField Kor; // �ѱ� �Է� �κ�
	private static String ReadEng = null;
	private static String ReadKor = null;

	public void scroll() {
		result.setCaretPosition(result.getDocument().getLength());
	}

	public EngVocaTest() {
		JPanel PUT = new JPanel(); // �Է� �κ�
		JPanel GLOBAL = new JPanel(); // ��ü ��ġ
		JPanel NOTE = new JPanel(); // PUT�� scroll ��ġ
		JPanel BUTTON = new JPanel(); // ��ư
		
		setTitle("���ܾ�");

		Eng = new JTextField(10);
		Kor = new JTextField(10); // �ܾ���ڼ� ��������

		JButton Start = new JButton("�׽�Ʈ ����"); // ��ư ����
		JButton Check = new JButton("Ȯ ��");
		JButton Add = new JButton("�ܾ� �߰�");
		JButton Delete = new JButton("�� ��");
		JButton Find = new JButton("�� ��");
		JButton Reset = new JButton("�ʱ�ȭ");
		JButton End = new JButton("�� ��");

		startText();

		BUTTON.setLayout(new GridLayout(7, 1, 5, 5));
		BUTTON.add(Start);
		BUTTON.add(Add);
		BUTTON.add(Delete);
		BUTTON.add(Find);
		BUTTON.add(Reset);
		BUTTON.add(End);
		BUTTON.add(Check);

		PUT.setLayout(new FlowLayout());
		PUT.add(new JLabel("���ܾ�"));
		PUT.add(Eng);
		PUT.add(new JLabel("�ѱ�"));
		PUT.add(Kor);
		PUT.add(new JLabel("                  "));

		NOTE.setLayout(new BorderLayout(0, 5));
		NOTE.add("Center", new JScrollPane(result));
		NOTE.add("South", PUT);

		GLOBAL.setLayout(new BorderLayout(0, 5));
		GLOBAL.add("Center", NOTE);
		GLOBAL.add("East", BUTTON);

		getContentPane().setLayout(null);
		GLOBAL.setBounds(10, 20, 430, 330);
		getContentPane().add(GLOBAL);
		setResizable(false);
		setSize(470, 400);
		setVisible(true);

		Start.addActionListener(this);
		Add.addActionListener(this);
		Delete.addActionListener(this);
		Find.addActionListener(this);
		Reset.addActionListener(this);
		End.addActionListener(this);
		Check.addActionListener(this);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void addWord() {
		PrintWriter pw;
		String eng = Eng.getText();
		String kor = Kor.getText();
		boolean append = true;

		try {
			if (eng.trim().compareTo("") == 0 || kor.trim().compareTo("") == 0) {
				result.append("�ܾ �Է����ּ���.\n");
			} else {
				pw = new PrintWriter(new FileOutputStream(FN, append));
				pw.println(eng);
				pw.println(kor);
				result.append(" �ܾ� : " + eng + " ��: " + kor + " (��)�� �ԷµǾ����ϴ�. \n");
				pw.close();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			result.append(" ������ �����ϴ�. \n");
		}
		scroll();
	}

	public int random() throws IOException { // �������� �ҷ��� �ܾ� �ε��� ����
		int cnt = 0;
		int index = 0;
		
			BufferedReader randomText = new BufferedReader(new FileReader(FN));
			while (randomText.readLine() != null) {
				cnt++; // �ܾ� ���� Ȯ��
			}
			if (cnt == 0) {
				throw new IOException();
			}
			index = 2 + new Random().nextInt(cnt); // �ε��� ����
			randomText.close();
		

		return index;

	}

	public void read() { // ���� �б�
		int i;
		String ex;
		int r;
		BufferedReader ReadText = null;
		
		try {
			r = random();

			ReadText = new BufferedReader(new FileReader(FN));
			for (i = 0; i < r; i++) {
				ex = ReadText.readLine();
				if (r % 2 == 1) { // r�� ¦��
					if (i % 2 == 0 & i < r - 1) {
						ReadEng = ex;
					} else if (i % 2 == 1)
						ReadKor = ex;
				} else { // r�� Ȧ��
					if (i % 2 == 0)
						ReadEng = ex;
					else
						ReadKor = ex;
				}
			}
			System.out.println(r + "\t" + ReadEng + "\t" + ReadKor);
			ReadText.close();
			Eng.setText(ReadEng);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			result.append("������ �����ϴ�.\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			result.append("����\n");
		}

		scroll();
	}

	public void comp() { // �Է�=���� �ܾ� ��
		try {
			if (Kor.getText().contentEquals(ReadKor))
				result.append(" �����Դϴ�. \n");
			else
				result.append(" Ʋ�Ƚ��ϴ�. " + ReadEng + " �� ������ : " + ReadKor + "�Դϴ�.\n");
			Eng.setText("");
		} catch (NullPointerException e) {
			result.append(" ���� �Է����ּ���.\n");
		}
		scroll();
	}

	public void delete() { // �ܾ� ����
		try {
			new PrintWriter(new FileOutputStream(FN));
			result.append("�ܾ �����Ǿ����ϴ�.\n");
			Eng.setText("");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			result.append(" ������ �����ϴ�. \n");
		}
		scroll();
	}

	public void find() { // �� �ܾ� �� ���� ���
		String eng;
		String kor;
		int cnt = 0;
		try {
			BufferedReader FindText = new BufferedReader(new FileReader(FN));
			result.append("\n\n");
			while (true) {
				kor = FindText.readLine();
				eng = FindText.readLine();
				if (kor == null || eng == null)
					break; // �� �̻� �ܾ ������
				result.append(" ���ܾ� : " + eng + "  " + "�� : " + kor + "\n");
				cnt++;
				scroll();
			}
			result.append("                                                  \n");
			if (cnt == 0)
				result.append(" �� ���ܾ� �� : " + cnt + "\n��ϵ� �ܾ �����ϴ�. �ܾ �Է��ϼ���.  \n");
			else
				result.append(" �� ���ܾ� �� : " + cnt + "\n");
			FindText.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			result.append(" ��ϵ� �ܾ �����ϴ�.\n");
		}
		scroll();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String In = e.getActionCommand();

		if (In.equals("�ܾ� �߰�")) {
			addWord();
			Kor.setText("");
			Eng.setText("");
		} else if (In.equals("�׽�Ʈ ����")) {
			result.append("\nù��° �����Դϴ�.\n");
			read();
			Kor.setText("");
		} else if (In.equals("�� ��")) {
			delete();
			Kor.setText("");
			Eng.setText("");
		} else if (In.equals("�� ��")) {
			find();
			Kor.setText("");
			Eng.setText("");
		} else if (In.equals("�ʱ�ȭ")) {
			startText();
			Eng.setText("");
			Kor.setText("");
		} else if (In.equals("�� ��")) {
			setVisible(false);
			System.exit(0);
		} else if (In.equals("Ȯ ��")) {
			comp();
			Kor.setText("");
			Eng.setText("");
			read();
		} else
			result.append("�ش� ��ư�� �����ϴ�. \n");
	}

	public void startText() {
		result.setText("/////////////////  �׽�Ʈ ����  ///////////////////\n ");
		result.append("�׽�Ʈ ���� -> ��ϵ� �ܾ�� ������ �����˴ϴ�.  \n");
		result.append("�׽�Ʈ ���۽� ��ư�� �ι� �����ּ��� !!! \n");
		result.append("�ܾ� �߰� -> ����� �ǹ��� �Է��մϴ�. \n");
		result.append("�� �� -> ��ϵ� �ܾ �����մϴ�. \n");
		result.append("�� �� -> ��ϵ� ��� �ܾ ����մϴ�. \n");
		result.append("�ʱ�ȭ -> �����۾��� �ʱ�ȭ�մϴ�. \n");
		result.append("�� ��  -> ���α׷��� �����մϴ�. \n");
		result.append("Ȯ �� -> ������ Ȯ���մϴ�. \n");
	}
	
	public static void main(String[] args) {
		new EngVocaTest();
	}
}