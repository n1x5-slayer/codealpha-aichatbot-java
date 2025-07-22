import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.regex.*;

public class N1X5AI extends JFrame {
    
    // Enhanced GUI Components
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;
    private JComboBox<String> categoryCombo;
    private JLabel headerLabel;
    
    // N1X5 AI Knowledge Base
    private Map<String, Map<Pattern, String>> knowledgeBase;
    private final Color N1X5_BLUE = new Color(0, 92, 175);
    private final Color N1X5_ORANGE = new Color(255, 110, 0);
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new N1X5AI().setVisible(true));
    }

    public N1X5AI() {
        initializeKnowledgeBase();
        createUI();
    }

    private void createUI() {
        // Main Window Setup
        setTitle("N1X5 AI Knowledge Oracle");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);
        
        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(N1X5_BLUE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        headerLabel = new JLabel("N1X5 AI KNOWLEDGE ", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);
        
        // Chat Display
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        chatArea.setMargin(new Insets(10, 15, 10, 15));
        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        
        // Input Panel
        JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        categoryCombo = new JComboBox<>(new String[]{"All Topics", "Technology", "Science", "History", "Business", "Health"});
        categoryCombo.setBackground(Color.WHITE);
        categoryCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        inputField = new JTextField();
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        sendButton = new JButton("ASK N1X5");
        sendButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sendButton.setBackground(N1X5_ORANGE);
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);
        sendButton.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        
        JPanel inputControlPanel = new JPanel(new BorderLayout(10, 0));
        inputControlPanel.add(categoryCombo, BorderLayout.WEST);
        inputControlPanel.add(inputField, BorderLayout.CENTER);
        inputControlPanel.add(sendButton, BorderLayout.EAST);
        
        inputPanel.add(inputControlPanel, BorderLayout.CENTER);
        
        // Add components to frame
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);
        
        // Event Listeners
        sendButton.addActionListener(_ -> processQuery());
        inputField.addActionListener(_ -> processQuery());
        
        // Initial greeting
        displayResponse("N1X5 AI", "Welcome to N1X5 Knowledge Oracle. I can answer questions about:\n" +
                     "• Artificial Intelligence\n• Machine Learning\n• Quantum Computing\n• Business Strategy\n• Health Science\n" +
                     "Select a category or ask anything!");
    }

    private void processQuery() {
        String query = inputField.getText().trim();
        String category = (String) categoryCombo.getSelectedItem();
        
        if (query.isEmpty()) return;
        
        displayResponse("You", query);
        inputField.setText("");
        
        String response = generateResponse(query, category);
        displayResponse("N1X5 AI", response);
    }

    private void displayResponse(String sender, String message) {
        String formatted = String.format("[%s]:\n%s\n\n", sender, message);
        chatArea.append(formatted);
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }

    private void initializeKnowledgeBase() {
        knowledgeBase = new HashMap<>();
        
        // Technology Patterns
        Map<Pattern, String> techPatterns = new LinkedHashMap<>();
        techPatterns.put(Pattern.compile("(?i)AI|artificial intelligence"), 
            "N1X5 AI specializes in machine learning algorithms that adapt to user queries.");
        techPatterns.put(Pattern.compile("(?i)machine learning|deep learning|neural network"), 
            "Our N1X5 systems use transformer architectures similar to GPT models but optimized for factual accuracy.");
        techPatterns.put(Pattern.compile("(?i)quantum comput(ing|er)"), 
            "Quantum computing uses qubits to perform calculations. N1X5 is developing quantum ML algorithms.");
        
        // Science Patterns
        Map<Pattern, String> sciencePatterns = new LinkedHashMap<>();
        sciencePatterns.put(Pattern.compile("(?i)black hole"), 
            "Black holes are spacetime regions with gravity so strong that nothing can escape.");
        sciencePatterns.put(Pattern.compile("(?i)DNA|genetic"), 
            "DNA contains genetic instructions. N1X5 Bio division uses AI for genomic analysis.");
        
        // Business Patterns
        Map<Pattern, String> businessPatterns = new LinkedHashMap<>();
        businessPatterns.put(Pattern.compile("(?i)invest(ing|ment)"), 
            "N1X5 Financial recommends diversified portfolios with 60% stocks, 30% bonds, 10% alternatives.");
        businessPatterns.put(Pattern.compile("(?i)marketing|branding"), 
            "N1X5 Marketing AI analyzes consumer behavior patterns across digital platforms.");
        
        knowledgeBase.put("Technology", techPatterns);
        knowledgeBase.put("Science", sciencePatterns);
        knowledgeBase.put("Business", businessPatterns);
        
        // Add default catch-all patterns
        Map<Pattern, String> defaultPatterns = new LinkedHashMap<>();
        defaultPatterns.put(Pattern.compile("(?i)hello|hi|hey"), 
            "Hello! I'm N1X5 AI. How can I assist you today?");
        defaultPatterns.put(Pattern.compile("(?i)thank"), 
            "You're welcome! The N1X5 team is always happy to help.");
        knowledgeBase.put("All Topics", defaultPatterns);
    }

    private String generateResponse(String query, String category) {
        // Check category-specific patterns first
        if (!category.equals("All Topics")) {
            String categoryResponse = matchPatterns(knowledgeBase.get(category), query);
            if (categoryResponse != null) {
                return categoryResponse;
            }
        }
        
        // Check all patterns if no category match
        String defaultResponse = matchPatterns(knowledgeBase.get("All Topics"), query);
        if (defaultResponse != null) {
            return defaultResponse;
        }
        
        return "I couldn't find information about that topic. The N1X5 knowledge base contains:\n" +
               "- Technology innovations\n- Scientific discoveries\n- Business strategies\n" +
               "Try rephrasing or selecting a specific category.";
    }

    private String matchPatterns(Map<Pattern, String> patterns, String query) {
        for (Map.Entry<Pattern, String> entry : patterns.entrySet()) {
            if (entry.getKey().matcher(query).find()) {
                return entry.getValue();
            }
        }
        return null;
    }
}
