package View.Question;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;
import java.awt.Color;

import View.UI.UI;

public class QuestionUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtQuestionId;
    private JTextArea txtAreaContent;
    private JComboBox<String> comboBoxSection;
    private JTextField txtAudioPath;
    private JButton btnBrowseAudio;
    private JTextField txtImagePath;
    private JButton btnBrowseImage;

    private JTextField txtAnswerA;
    private JTextField txtAnswerB;
    private JTextField txtAnswerC;
    private JTextField txtAnswerD;

    private JRadioButton radioAnswerA;
    private JRadioButton radioAnswerB;
    private JRadioButton radioAnswerC;
    private JRadioButton radioAnswerD;
    private ButtonGroup answersGroup;

    private JButton btnSave;
    private JButton btnCancel;
    private JButton btnClear;

    public static void run() {
        try {
            QuestionUI frame = new QuestionUI();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public QuestionUI() {
        initializeUI();
        new Controller.Question.QuestionController(this);
    }
    
    private void initializeUI() {
        setTitle("Quản lý Câu hỏi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 750, 700);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPane.setBackground(new Color(0xf9f9f9));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 20));
        
        // Add MenuBar
        UI.createMenuBar(this);

        JLabel lblTitle = new JLabel("Thêm/Sửa Câu hỏi");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setHorizontalAlignment(JLabel.CENTER);
        lblTitle.setForeground(new Color(0x2c3e50));
        contentPane.add(lblTitle, BorderLayout.NORTH);

        JPanel formPanel = createFormPanel();
        contentPane.add(formPanel, BorderLayout.CENTER);
    }
    
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0,0,0,50), 1),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        
        GridBagLayout gbl_formPanel = new GridBagLayout();
        gbl_formPanel.columnWidths = new int[]{0, 0, 0};
        gbl_formPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
        gbl_formPanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
        gbl_formPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
        formPanel.setLayout(gbl_formPanel);

        // Content question
        addContentQuestion(formPanel);
        
        // Section
        addSectionDropdown(formPanel);
        
        // Audio file
        addAudioFileSection(formPanel);
        
        // Image file
        addImageFileSection(formPanel);
        
        // Answers
        addAnswersSection(formPanel);
        
        // Action buttons
        addActionButtons(formPanel);
        
        return formPanel;
    }
    
    private void addContentQuestion(JPanel formPanel) {
        JLabel lblContent = new JLabel("Nội dung câu hỏi:");
        lblContent.setFont(new Font("Arial", Font.BOLD, 14));
        GridBagConstraints gbc_lblContent = new GridBagConstraints();
        gbc_lblContent.anchor = GridBagConstraints.WEST;
        gbc_lblContent.insets = new Insets(0, 0, 5, 5);
        gbc_lblContent.gridx = 0;
        gbc_lblContent.gridy = 0;
        formPanel.add(lblContent, gbc_lblContent);

        txtAreaContent = new JTextArea();
        txtAreaContent.setRows(5);
        txtAreaContent.setFont(new Font("Arial", Font.PLAIN, 14));
        txtAreaContent.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        JScrollPane scrollPaneContent = new JScrollPane(txtAreaContent);
        GridBagConstraints gbc_scrollPaneContent = new GridBagConstraints();
        gbc_scrollPaneContent.insets = new Insets(0, 0, 18, 0);
        gbc_scrollPaneContent.fill = GridBagConstraints.BOTH;
        gbc_scrollPaneContent.gridx = 1;
        gbc_scrollPaneContent.gridy = 0;
        formPanel.add(scrollPaneContent, gbc_scrollPaneContent);
    }
    
    private void addSectionDropdown(JPanel formPanel) {
        JLabel lblSection = new JLabel("Phần (Section):");
        lblSection.setFont(new Font("Arial", Font.BOLD, 14));
        GridBagConstraints gbc_lblSection = new GridBagConstraints();
        gbc_lblSection.anchor = GridBagConstraints.WEST;
        gbc_lblSection.insets = new Insets(0, 0, 5, 5);
        gbc_lblSection.gridx = 0;
        gbc_lblSection.gridy = 1;
        formPanel.add(lblSection, gbc_lblSection);

        comboBoxSection = new JComboBox<>();
        comboBoxSection.setFont(new Font("Arial", Font.PLAIN, 14));
        comboBoxSection.addItem("-- Chọn loại câu hỏi --");
        comboBoxSection.addItem("Grammar");
        comboBoxSection.addItem("Vocabulary");
        comboBoxSection.addItem("Reading");
        comboBoxSection.addItem("Listening");
        GridBagConstraints gbc_comboBoxSection = new GridBagConstraints();
        gbc_comboBoxSection.insets = new Insets(0, 0, 18, 0);
        gbc_comboBoxSection.fill = GridBagConstraints.HORIZONTAL;
        gbc_comboBoxSection.gridx = 1;
        gbc_comboBoxSection.gridy = 1;
        formPanel.add(comboBoxSection, gbc_comboBoxSection);
    }
    
    private void addAudioFileSection(JPanel formPanel) {
        JLabel lblAudioPath = new JLabel("File âm thanh:");
        lblAudioPath.setFont(new Font("Arial", Font.BOLD, 14));
        GridBagConstraints gbc_lblAudioPath = new GridBagConstraints();
        gbc_lblAudioPath.anchor = GridBagConstraints.WEST;
        gbc_lblAudioPath.insets = new Insets(0, 0, 5, 5);
        gbc_lblAudioPath.gridx = 0;
        gbc_lblAudioPath.gridy = 2;
        formPanel.add(lblAudioPath, gbc_lblAudioPath);

        JPanel audioPanel = new JPanel(new BorderLayout(5,0));
        audioPanel.setOpaque(false);
        txtAudioPath = new JTextField();
        txtAudioPath.setFont(new Font("Arial", Font.PLAIN, 14));
        txtAudioPath.setEditable(false);
        audioPanel.add(txtAudioPath, BorderLayout.CENTER);
        btnBrowseAudio = new JButton("Browse...");
        audioPanel.add(btnBrowseAudio, BorderLayout.EAST);
        GridBagConstraints gbc_audioPanel = new GridBagConstraints();
        gbc_audioPanel.insets = new Insets(0, 0, 18, 0);
        gbc_audioPanel.fill = GridBagConstraints.HORIZONTAL;
        gbc_audioPanel.gridx = 1;
        gbc_audioPanel.gridy = 2;
        formPanel.add(audioPanel, gbc_audioPanel);
    }
    
    private void addImageFileSection(JPanel formPanel) {
        JLabel lblImagePath = new JLabel("File hình ảnh:");
        lblImagePath.setFont(new Font("Arial", Font.BOLD, 14));
        GridBagConstraints gbc_lblImagePath = new GridBagConstraints();
        gbc_lblImagePath.anchor = GridBagConstraints.WEST;
        gbc_lblImagePath.insets = new Insets(0, 0, 5, 5);
        gbc_lblImagePath.gridx = 0;
        gbc_lblImagePath.gridy = 3;
        formPanel.add(lblImagePath, gbc_lblImagePath);

        JPanel imagePanel = new JPanel(new BorderLayout(5,0));
        imagePanel.setOpaque(false);
        txtImagePath = new JTextField();
        txtImagePath.setFont(new Font("Arial", Font.PLAIN, 14));
        txtImagePath.setEditable(false);
        imagePanel.add(txtImagePath, BorderLayout.CENTER);
        btnBrowseImage = new JButton("Browse...");
        imagePanel.add(btnBrowseImage, BorderLayout.EAST);
        GridBagConstraints gbc_imagePanel = new GridBagConstraints();
        gbc_imagePanel.insets = new Insets(0, 0, 18, 0);
        gbc_imagePanel.fill = GridBagConstraints.HORIZONTAL;
        gbc_imagePanel.gridx = 1;
        gbc_imagePanel.gridy = 3;
        formPanel.add(imagePanel, gbc_imagePanel);
    }
    
    private void addAnswersSection(JPanel formPanel) {
        JPanel answersPanel = new JPanel();
        answersPanel.setOpaque(false);
        answersPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Các đáp án",
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new Font("Arial", Font.BOLD, 14),
            new Color(0x555555)
        ));
        GridBagConstraints gbc_answersPanel = new GridBagConstraints();
        gbc_answersPanel.insets = new Insets(0, 0, 18, 0);
        gbc_answersPanel.gridwidth = 2;
        gbc_answersPanel.fill = GridBagConstraints.BOTH;
        gbc_answersPanel.gridx = 0;
        gbc_answersPanel.gridy = 4;
        formPanel.add(answersPanel, gbc_answersPanel);
        answersPanel.setLayout(new GridLayout(0, 1, 0, 10));

        answersGroup = new ButtonGroup();

        answersPanel.add(createAnswerItemPanel("A.", radioAnswerA = new JRadioButton(), txtAnswerA = new JTextField()));
        answersPanel.add(createAnswerItemPanel("B.", radioAnswerB = new JRadioButton(), txtAnswerB = new JTextField()));
        answersPanel.add(createAnswerItemPanel("C.", radioAnswerC = new JRadioButton(), txtAnswerC = new JTextField()));
        answersPanel.add(createAnswerItemPanel("D.", radioAnswerD = new JRadioButton(), txtAnswerD = new JTextField()));
    }
    
    private void addActionButtons(JPanel formPanel) {
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionsPanel.setOpaque(false);
        btnCancel = new JButton("Hủy");
        btnCancel.setFont(new Font("Arial", Font.PLAIN, 16));
        btnCancel.setBackground(new Color(0x6c757d));
        btnCancel.setForeground(Color.WHITE);

        btnSave = new JButton("Lưu Câu hỏi");
        btnSave.setFont(new Font("Arial", Font.PLAIN, 16));
        btnSave.setBackground(new Color(0x28a745));
        btnSave.setForeground(Color.WHITE);
        
        btnClear = new JButton("Xóa câu hỏi");
        btnClear.setForeground(Color.WHITE);
        btnClear.setFont(new Font("Arial", Font.PLAIN, 16));
        btnClear.setBackground(new Color(108, 117, 125));
        actionsPanel.add(btnClear);

        actionsPanel.add(btnCancel);
        actionsPanel.add(btnSave);

        GridBagConstraints gbc_actionsPanel = new GridBagConstraints();
        gbc_actionsPanel.gridwidth = 2;
        gbc_actionsPanel.fill = GridBagConstraints.HORIZONTAL;
        gbc_actionsPanel.gridx = 0;
        gbc_actionsPanel.gridy = 5;
        formPanel.add(actionsPanel, gbc_actionsPanel);
    }

    private JPanel createAnswerItemPanel(String label, JRadioButton radioButton, JTextField textField) {
        JPanel itemPanel = new JPanel(new BorderLayout(8, 0));
        itemPanel.setOpaque(false);
        
        radioButton.setFont(new Font("Arial", Font.PLAIN, 14));
        radioButton.setOpaque(false);
        answersGroup.add(radioButton);
        
        JLabel ansLabel = new JLabel(label);
        ansLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JPanel radioLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        radioLabelPanel.setOpaque(false);
        radioLabelPanel.add(radioButton);
        radioLabelPanel.add(ansLabel);

        itemPanel.add(radioLabelPanel, BorderLayout.WEST);
        
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        itemPanel.add(textField, BorderLayout.CENTER);
        
        return itemPanel;
    }

    public JTextField getTxtQuestionId() {
        return txtQuestionId;
    }
    
    public JTextArea getTxtAreaContent() {
        return txtAreaContent;
    }
    
    public JComboBox<String> getComboBoxSection() {
        return comboBoxSection;
    }
    
    public JTextField getTxtAudioPath() {
        return txtAudioPath;
    }
    
    public JButton getBtnBrowseAudio() {
        return btnBrowseAudio;
    }
    
    public JTextField getTxtImagePath() {
        return txtImagePath;
    }
    
    public JButton getBtnBrowseImage() {
        return btnBrowseImage;
    }
    
    public JTextField getTxtAnswerA() {
        return txtAnswerA;
    }
    
    public JTextField getTxtAnswerB() {
        return txtAnswerB;
    }
    
    public JTextField getTxtAnswerC() {
        return txtAnswerC;
    }
    
    public JTextField getTxtAnswerD() {
        return txtAnswerD;
    }
    
    public JRadioButton getRadioAnswerA() {
        return radioAnswerA;
    }
    
    public JRadioButton getRadioAnswerB() {
        return radioAnswerB;
    }
    
    public JRadioButton getRadioAnswerC() {
        return radioAnswerC;
    }
    
    public JRadioButton getRadioAnswerD() {
        return radioAnswerD;
    }
    
    public JButton getBtnSave() {
        return btnSave;
    }
    
    public JButton getBtnCancel() {
        return btnCancel;
    }

    public JButton getBtnClear() {
        return btnClear;
    }
}