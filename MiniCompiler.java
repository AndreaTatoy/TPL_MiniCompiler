import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MiniCompiler extends JFrame {
    private JTextArea codeTextArea;
    private JTextArea resultTextArea;
    private JButton openFileButton;
    private JButton lexicalAnalysisButton;
    private JButton syntaxAnalysisButton;
    private JButton semanticAnalysisButton;
    private JButton clearButton;

    public MiniCompiler() {
        codeTextArea = new JTextArea(10, 40);
        resultTextArea = new JTextArea(5, 40);
        resultTextArea.setEditable(false);
        codeTextArea.setEditable(false);

        codeTextArea.setMargin(new Insets(10, 10, 10, 10)); 
        resultTextArea.setMargin(new Insets(10, 10, 10, 10));

        openFileButton = new JButton("Open File");
        lexicalAnalysisButton = new JButton("Lexical Analysis");
        syntaxAnalysisButton = new JButton("Syntax Analysis");
        semanticAnalysisButton = new JButton("Semantic Analysis");
        clearButton = new JButton("Clear");

        lexicalAnalysisButton.setEnabled(false);
        syntaxAnalysisButton.setEnabled(false);
        semanticAnalysisButton.setEnabled(false);

        //<---------------------------- Interface ---------------------------->
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        buttonPanel.setBackground(new Color(152, 193, 217));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 20, 5, 20);
        gbc.weightx = 1;
        gbc.weighty = 1;

        Dimension buttonSize = new Dimension(180, 50);
        openFileButton.setPreferredSize(buttonSize);
        lexicalAnalysisButton.setPreferredSize(buttonSize);
        syntaxAnalysisButton.setPreferredSize(buttonSize);
        semanticAnalysisButton.setPreferredSize(buttonSize);
        clearButton.setPreferredSize(buttonSize);

        setButtonStyles(openFileButton);
        setButtonStyles(lexicalAnalysisButton);
        setButtonStyles(syntaxAnalysisButton);
        setButtonStyles(semanticAnalysisButton);
        setButtonStyles(clearButton);

        buttonPanel.add(openFileButton, gbc); 
        buttonPanel.add(lexicalAnalysisButton, gbc);
        buttonPanel.add(syntaxAnalysisButton, gbc);
        buttonPanel.add(semanticAnalysisButton, gbc);
        buttonPanel.add(clearButton, gbc);

        mainPanel.add(buttonPanel, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(new Color(230, 252, 252));
        resultTextArea.setBackground(new Color(224, 251, 252));
        codeTextArea.setBackground(new Color(230, 252, 252));
        
        JScrollPane resultScrollPane = new JScrollPane(resultTextArea);
        rightPanel.add(resultScrollPane, BorderLayout.NORTH);

        JScrollPane codeScrollPane = new JScrollPane(codeTextArea);
        rightPanel.add(codeScrollPane, BorderLayout.CENTER);
        
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        openFileButton.addActionListener(e -> openFile());
        lexicalAnalysisButton.addActionListener(e -> performLexicalAnalysis());
        syntaxAnalysisButton.addActionListener(e -> performSyntaxAnalysis());
        semanticAnalysisButton.addActionListener(e -> performSemanticAnalysis());
        clearButton.addActionListener(e -> clearAll());

        this.setContentPane(mainPanel);
        this.setTitle("Mini Compiler");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(800, 500);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void setButtonStyles(JButton button) {
        button.setBackground(Color.WHITE); 
        button.setForeground(new Color(61, 90, 128));
        button.setFocusPainted(false);   
        button.setBorder(BorderFactory.createLineBorder(new Color(61, 90, 128), 2));      
    }

    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select a Source File");

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
                codeTextArea.read(reader, null);
                lexicalAnalysisButton.setEnabled(true);
                syntaxAnalysisButton.setEnabled(false);
                semanticAnalysisButton.setEnabled(false);
                resultTextArea.setText("");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error reading file: " + ex.getMessage(),
                        "File Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void performLexicalAnalysis() {
        String code = codeTextArea.getText();
        List<String> tokens = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        String keywordPattern = "\\b(int|float|String|double|if|else|while|return)\\b";
        String identifierPattern = "\\b[a-zA-Z_][a-zA-Z0-9_]*\\b";
        String operatorPattern = "[+\\-*/=<>!&|]";
        String separatorPattern = "[;(){}]";
        String literalPattern = "\\b\\d+\\.\\d+\\b|\\b\\d+\\b|\"[^\"]*\"";

        String combinedPattern = String.format("%s|%s|%s|%s|%s",
                keywordPattern, identifierPattern, operatorPattern, separatorPattern, literalPattern);

        Pattern pattern = Pattern.compile(combinedPattern);
        Matcher matcher = pattern.matcher(code);

        while (matcher.find()) {
            tokens.add(matcher.group());
        }

        String remaining = matcher.replaceAll("");
        if (!remaining.trim().isEmpty()) {
            errors.add("Lexical error: Unrecognized token(s) found");
        }

        if (!errors.isEmpty()) {
            resultTextArea.setText(String.join("\n", errors));
            syntaxAnalysisButton.setEnabled(false);
        } else {
            resultTextArea.setText("Tokens:\n" + String.join(", ", tokens));
            syntaxAnalysisButton.setEnabled(true);
            lexicalAnalysisButton.setEnabled(false);
        }
    }

    private void performSyntaxAnalysis() {
        String code = codeTextArea.getText();
        List<String> errors = new ArrayList<>();

        int balance = 0;
        for (char ch : code.toCharArray()) {
            if (ch == '(') {
                balance++;
            } else if (ch == ')') {
                balance--;
                if (balance < 0) {
                    errors.add("Syntax error: Unmatched closing parenthesis");
                    break;
                }
            }
        }

        if (balance > 0) {
            errors.add("Syntax error: Unmatched opening parenthesis");
        }

        if (!errors.isEmpty()) {
            resultTextArea.setText(String.join("\n", errors));
            semanticAnalysisButton.setEnabled(false);
        } else {
            resultTextArea.setText("Syntax analysis passed.");
            semanticAnalysisButton.setEnabled(true);
            syntaxAnalysisButton.setEnabled(false);
        }
    }

    private void performSemanticAnalysis() {
        String code = codeTextArea.getText();
        List<String> declaredVariables = new ArrayList<>();
        List<String> errors = new ArrayList<>();
    
        String[] keywords = {
            "public", "private", "protected", "class", "int", "float", "String", "double",
            "if", "else", "while", "for", "return", "void", "true", "false", "null"
        };
        
        List<String> keywordList = new ArrayList<>();
        for (String keyword : keywords) {
            keywordList.add(keyword);
        }
    
        List<String> declaredClasses = new ArrayList<>();
    
        String[] lines = code.split("\n");
    
        for (String line : lines) {
            line = line.trim();
    
            Matcher classMatcher = Pattern.compile("\\bclass\\s+([a-zA-Z_][a-zA-Z0-9_]*)\\b").matcher(line);
            while (classMatcher.find()) {
                String className = classMatcher.group(1);
                if (declaredClasses.contains(className)) {
                    errors.add("Semantic error: Class '" + className + "' is redeclared.");
                } else {
                    declaredClasses.add(className);
                }
            }
    
            Matcher declarationMatcher = Pattern.compile("\\b(int|float|String|double)\\s+([a-zA-Z_][a-zA-Z0-9_]*)\\b").matcher(line);
            while (declarationMatcher.find()) {
                String variable = declarationMatcher.group(2);
    
                if (declaredVariables.contains(variable)) {
                    errors.add("Semantic error: Variable '" + variable + "' is redeclared.");
                } else {
                    declaredVariables.add(variable);
                }
            }
    
            Matcher usageMatcher = Pattern.compile("\\b([a-zA-Z_][a-zA-Z0-9_]*)\\b").matcher(line);
            while (usageMatcher.find()) {
                String identifier = usageMatcher.group(1);
    
                if (keywordList.contains(identifier) || declaredClasses.contains(identifier)) {
                    continue;
                }
    
                if (!declaredVariables.contains(identifier) && !line.startsWith("int") && !line.startsWith("float") &&
                        !line.startsWith("String") && !line.startsWith("double")) {
                    errors.add("Semantic error: Variable '" + identifier + "' is used without being declared.");
                }
            }
        }
    
        if (!errors.isEmpty()) {
            resultTextArea.setText(String.join("\n", errors));
        } else {
            resultTextArea.setText("Semantic analysis passed. All variables are declared and used correctly.");
        }
    
        semanticAnalysisButton.setEnabled(false);
    }

    private void clearAll() {
        codeTextArea.setText("");
        resultTextArea.setText("");
        lexicalAnalysisButton.setEnabled(false);
        syntaxAnalysisButton.setEnabled(false);
        semanticAnalysisButton.setEnabled(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MiniCompiler::new);
    }
}
