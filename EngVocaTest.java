import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;

public class EngVocaTest extends JFrame implements ActionListener {

	JTextArea result = new JTextArea(); // 결과 출력 부분

	final String FN = "WordPad.txt";
	public JTextField Eng; // 영어 입력 부분
	public JTextField Kor; // 한글 입력 부분
	private static String ReadEng = null;
	private static String ReadKor = null;

	public void scroll() {
		result.setCaretPosition(result.getDocument().getLength());
	}

	public EngVocaTest() {
		JPanel PUT = new JPanel(); // 입력 부분
		JPanel GLOBAL = new JPanel(); // 전체 배치
		JPanel NOTE = new JPanel(); // PUT과 scroll 배치
		JPanel BUTTON = new JPanel(); // 버튼
		
		setTitle("영단어");

		Eng = new JTextField(10);
		Kor = new JTextField(10); // 단어글자수 수정가능

		JButton Start = new JButton("테스트 시작"); // 버튼 생성
		JButton Check = new JButton("확 인");
		JButton Add = new JButton("단어 추가");
		JButton Delete = new JButton("삭 제");
		JButton Find = new JButton("검 색");
		JButton Reset = new JButton("초기화");
		JButton End = new JButton("종 료");

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
		PUT.add(new JLabel("영단어"));
		PUT.add(Eng);
		PUT.add(new JLabel("한글"));
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
				result.append("단어를 입력해주세요.\n");
			} else {
				pw = new PrintWriter(new FileOutputStream(FN, append));
				pw.println(eng);
				pw.println(kor);
				result.append(" 단어 : " + eng + " 뜻: " + kor + " (이)가 입력되었습니다. \n");
				pw.close();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			result.append(" 파일이 없습니다. \n");
		}
		scroll();
	}

	public int random() throws IOException { // 랜덤으로 불러올 단어 인덱스 생성
		int cnt = 0;
		int index = 0;
		
			BufferedReader randomText = new BufferedReader(new FileReader(FN));
			while (randomText.readLine() != null) {
				cnt++; // 단어 개수 확인
			}
			if (cnt == 0) {
				throw new IOException();
			}
			index = 2 + new Random().nextInt(cnt); // 인덱스 결정
			randomText.close();
		

		return index;

	}

	public void read() { // 문제 읽기
		int i;
		String ex;
		int r;
		BufferedReader ReadText = null;
		
		try {
			r = random();

			ReadText = new BufferedReader(new FileReader(FN));
			for (i = 0; i < r; i++) {
				ex = ReadText.readLine();
				if (r % 2 == 1) { // r이 짝수
					if (i % 2 == 0 & i < r - 1) {
						ReadEng = ex;
					} else if (i % 2 == 1)
						ReadKor = ex;
				} else { // r이 홀수
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
			result.append("파일이 없습니다.\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			result.append("오류\n");
		}

		scroll();
	}

	public void comp() { // 입력=사전 단어 비교
		try {
			if (Kor.getText().contentEquals(ReadKor))
				result.append(" 정답입니다. \n");
			else
				result.append(" 틀렸습니다. " + ReadEng + " 의 정답은 : " + ReadKor + "입니다.\n");
			Eng.setText("");
		} catch (NullPointerException e) {
			result.append(" 답을 입력해주세요.\n");
		}
		scroll();
	}

	public void delete() { // 단어 삭제
		try {
			new PrintWriter(new FileOutputStream(FN));
			result.append("단어가 삭제되었습니다.\n");
			Eng.setText("");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			result.append(" 파일이 없습니다. \n");
		}
		scroll();
	}

	public void find() { // 총 단어 및 개수 출력
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
					break; // 더 이상 단어가 없으면
				result.append(" 영단어 : " + eng + "  " + "뜻 : " + kor + "\n");
				cnt++;
				scroll();
			}
			result.append("                                                  \n");
			if (cnt == 0)
				result.append(" 총 영단어 수 : " + cnt + "\n등록된 단어가 없습니다. 단어를 입력하세요.  \n");
			else
				result.append(" 총 영단어 수 : " + cnt + "\n");
			FindText.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			result.append(" 등록된 단어가 없습니다.\n");
		}
		scroll();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String In = e.getActionCommand();

		if (In.equals("단어 추가")) {
			addWord();
			Kor.setText("");
			Eng.setText("");
		} else if (In.equals("테스트 시작")) {
			result.append("\n첫번째 문제입니다.\n");
			read();
			Kor.setText("");
		} else if (In.equals("삭 제")) {
			delete();
			Kor.setText("");
			Eng.setText("");
		} else if (In.equals("검 색")) {
			find();
			Kor.setText("");
			Eng.setText("");
		} else if (In.equals("초기화")) {
			startText();
			Eng.setText("");
			Kor.setText("");
		} else if (In.equals("종 료")) {
			setVisible(false);
			System.exit(0);
		} else if (In.equals("확 인")) {
			comp();
			Kor.setText("");
			Eng.setText("");
			read();
		} else
			result.append("해당 버튼이 없습니다. \n");
	}

	public void startText() {
		result.setText("/////////////////  테스트 시작  ///////////////////\n ");
		result.append("테스트 시작 -> 등록된 단어로 문제가 출제됩니다.  \n");
		result.append("테스트 시작시 버튼을 두번 눌러주세요 !!! \n");
		result.append("단어 추가 -> 영어와 의미을 입력합니다. \n");
		result.append("삭 제 -> 등록된 단어를 삭제합니다. \n");
		result.append("검 색 -> 등록된 모든 단어를 출력합니다. \n");
		result.append("초기화 -> 현재작업을 초기화합니다. \n");
		result.append("종 료  -> 프로그램을 종료합니다. \n");
		result.append("확 인 -> 정답을 확인합니다. \n");
	}
	
	public static void main(String[] args) {
		new EngVocaTest();
	}
}