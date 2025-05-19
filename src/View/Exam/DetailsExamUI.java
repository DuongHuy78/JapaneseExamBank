package View.Exam;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import View.UI.UI;

public class DetailsExamUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextArea examContentArea;
    private JTextArea questionDetailsArea;
    private JTextField searchField;
    private JComboBox<String> comboBoxSection;
    private JButton btnSearch;
    private JButton btnBack;
    private JTextField audioSearchField;
    private JButton btnBrowseAudio;
    private JButton btnPlayAudio;
    private JButton btnStopAudio;
    private int examId;

    public DetailsExamUI(int examId) {
        this.examId = examId;
        setTitle("Chi tiết đề thi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 900, 650);
        
        contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        UI.createMenuBar(this);

        // Tạo panel hiển thị thông tin đề thi
        JPanel examInfoPanel = new JPanel(new BorderLayout(10, 10));
        examInfoPanel.setBorder(BorderFactory.createTitledBorder("Thông tin đề thi"));
        
        examContentArea = new JTextArea();
        examContentArea.setEditable(false);
        examContentArea.setFont(new Font("Arial", Font.PLAIN, 14));
        examContentArea.setLineWrap(true);
        examContentArea.setWrapStyleWord(true);
        JScrollPane examScrollPane = new JScrollPane(examContentArea);
        examScrollPane.setPreferredSize(new Dimension(0, 150));
        examInfoPanel.add(examScrollPane, BorderLayout.CENTER);

        // Tạo panel tìm kiếm
        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Tìm kiếm câu hỏi"));
        
        JPanel searchControlPanel = new JPanel(new BorderLayout(5, 0));
        searchField = new JTextField();
        searchField.setToolTipText("Nhập từ khóa tìm kiếm");
        
        JPanel searchOptionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        comboBoxSection = new JComboBox<>(new String[]{"Tất cả", "Grammar", "Vocabulary", "Reading", "Listening"});
        btnSearch = new JButton("Tìm kiếm");
        
        searchOptionsPanel.add(new JLabel("Phần:"));
        searchOptionsPanel.add(comboBoxSection);
        searchOptionsPanel.add(btnSearch);
        
        searchControlPanel.add(searchField, BorderLayout.CENTER);
        searchControlPanel.add(searchOptionsPanel, BorderLayout.EAST);
        searchPanel.add(searchControlPanel, BorderLayout.CENTER);

        // Tạo panel chứa chi tiết câu hỏi
        JPanel questionPanel = new JPanel(new BorderLayout(10, 10));
        questionPanel.setBorder(BorderFactory.createTitledBorder("Chi tiết câu hỏi"));
        
        questionDetailsArea = new JTextArea();
        questionDetailsArea.setEditable(false);
        questionDetailsArea.setFont(new Font("Arial", Font.PLAIN, 14));
        questionDetailsArea.setLineWrap(true);
        questionDetailsArea.setWrapStyleWord(true);
        JScrollPane questionScrollPane = new JScrollPane(questionDetailsArea);
        questionPanel.add(questionScrollPane, BorderLayout.CENTER);

        // Tạo panel phát audio
        JPanel audioPanel = new JPanel(new BorderLayout(5, 0));
        audioPanel.setBorder(BorderFactory.createTitledBorder("Phát file audio"));
        
        JPanel audioControlPanel = new JPanel(new BorderLayout(5, 0));
        audioSearchField = new JTextField();
        audioSearchField.setEditable(false);
        audioSearchField.setToolTipText("Đường dẫn file audio");
        
        JPanel audioButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        btnBrowseAudio = new JButton("Duyệt...");
        btnPlayAudio = new JButton("Phát");
        btnPlayAudio.setEnabled(false);
        
        audioButtonPanel.add(btnBrowseAudio);
        audioButtonPanel.add(btnPlayAudio);
        
        audioControlPanel.add(audioSearchField, BorderLayout.CENTER);
        audioControlPanel.add(audioButtonPanel, BorderLayout.EAST);
        
        btnStopAudio = new JButton("Dừng");
        btnStopAudio.setEnabled(false);

        audioButtonPanel.add(btnStopAudio);
        audioPanel.add(audioControlPanel, BorderLayout.CENTER);

        // Tạo panel chứa nút điều hướng
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnBack = new JButton("Quay lại");
        buttonPanel.add(btnBack);
        
        // Thêm các panel vào content pane
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(questionPanel, BorderLayout.CENTER);
        centerPanel.add(audioPanel, BorderLayout.SOUTH);

        contentPane.add(examInfoPanel, BorderLayout.NORTH);
        contentPane.add(centerPanel, BorderLayout.CENTER);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        // Khởi tạo controller
        new Controller.Exam.DetailExamController(this);
    }

    public JTextArea getExamContentArea() {
        return examContentArea;
    }

    public JTextArea getQuestionDetailsArea() {
        return questionDetailsArea;
    }

    public JTextField getSearchField() {
        return searchField;
    }

    public JComboBox<String> getComboBoxSection() {
        return comboBoxSection;
    }

    public JButton getBtnSearch() {
        return btnSearch;
    }

    public JButton getBtnBack() {
        return btnBack;
    }

    public JTextField getAudioSearchField() {
        return audioSearchField;
    }

    public JButton getBtnBrowseAudio() {
        return btnBrowseAudio;
    }

    public JButton getBtnPlayAudio() {
        return btnPlayAudio;
    }
    
    public JButton getBtnStopAudio() {
    	return btnStopAudio;
    }

    public int getExamId() {
        return examId;
    }
}