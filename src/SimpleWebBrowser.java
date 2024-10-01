import javax.swing.*;
import java.awt.*;
import java.util.Stack;

public class SimpleWebBrowser extends JFrame {
    private JTextField urlField;
    private JEditorPane webPane;
    private JButton backButton;
    private JButton forwardButton;
    private JProgressBar progressBar;
    private Stack<String> backStack = new Stack<>();
    private Stack<String> forwardStack = new Stack<>();

    public SimpleWebBrowser() {
        super("Trình Duyệt Web Đơn Giản");

        // Tạo thanh nhập URL và nút Go
        urlField = new JTextField("https://www.example.com");
        JButton goButton = new JButton("Go");
        goButton.addActionListener(e -> loadPage(urlField.getText()));

        JPanel urlPanel = new JPanel(new BorderLayout());
        urlPanel.add(urlField, BorderLayout.CENTER);
        urlPanel.add(goButton, BorderLayout.EAST);

        // Tạo khu vực hiển thị trang web
        webPane = new JEditorPane();
        webPane.setEditable(false);
        webPane.setContentType("text/html; charset=UTF-8");

        // Tạo các nút điều hướng
        backButton = new JButton("Back");
        forwardButton = new JButton("Forward");

        backButton.addActionListener(e -> goBack());
        forwardButton.addActionListener(e -> goForward());

        JPanel navigationPanel = new JPanel();
        navigationPanel.add(backButton);
        navigationPanel.add(forwardButton);

        // Tạo thanh tiến trình
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(false);

        // Thêm các thành phần vào JFrame
        add(urlPanel, BorderLayout.NORTH);
        add(new JScrollPane(webPane), BorderLayout.CENTER);
        add(navigationPanel, BorderLayout.SOUTH);
        add(progressBar, BorderLayout.SOUTH);

        // Cài đặt JFrame
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        // Tải trang web mặc định
        loadPage(urlField.getText());
    }

    private void loadPage(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "https://" + url;
        }
        try {
            if (webPane.getPage() != null && !url.equals(webPane.getPage().toString())) {
                backStack.push(webPane.getPage().toString());
                forwardStack.clear();
            }
            progressBar.setIndeterminate(true);
            webPane.setPage(url);
            urlField.setText(url);
            progressBar.setIndeterminate(false);
        } catch (Exception e) {
            progressBar.setIndeterminate(false);
            webPane.setText("<html><body><h2>Không thể tải trang web</h2><p>" + e.getMessage() + "</p></body></html>");
        }
    }

    private void goBack() {
        if (!backStack.isEmpty()) {
            forwardStack.push(webPane.getPage().toString());
            loadPage(backStack.pop());
        }
    }

    private void goForward() {
        if (!forwardStack.isEmpty()) {
            backStack.push(webPane.getPage().toString());
            loadPage(forwardStack.pop());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SimpleWebBrowser();
            }
        });
    }
}
