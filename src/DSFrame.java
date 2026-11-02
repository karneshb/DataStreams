import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class DSFrame extends JFrame{
    private JTextArea txtOriginal;
    private JTextArea txtFiltered;
    private JTextField txtSearch;
    private Path currentFilePath;

    public DSFrame(){
        super("Java Data Streams");
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout());
        txtSearch = new JTextField(20);
        topPanel.add(new JLabel("Search: "));
        topPanel.add(txtSearch);

        JButton btnLoad = new JButton("Load File");
        JButton btnSearch = new JButton("Search");
        JButton btnQuit = new JButton("Quit");

        topPanel.add(btnLoad);
        topPanel.add(btnSearch);
        topPanel.add(btnQuit);
        add(topPanel, BorderLayout.NORTH);

        txtOriginal = new JTextArea(30, 40);
        txtOriginal.setEditable(false);
        txtFiltered = new JTextArea();
        txtFiltered.setEditable(false);

        JSplitPane paneSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(txtOriginal), new JScrollPane(txtFiltered));
        add(paneSplit, BorderLayout.CENTER);

        btnLoad.addActionListener((ActionEvent e)  -> loadFile());
        btnSearch.addActionListener((ActionEvent e) -> searchFile());
        btnQuit.addActionListener((ActionEvent e) -> System.exit(0));

        setSize(800,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void loadFile(){
        JFileChooser fileChooser = new JFileChooser(".");
        int result = fileChooser.showOpenDialog(this);

        if(result == JFileChooser.APPROVE_OPTION){
            currentFilePath = fileChooser.getSelectedFile().toPath();
            try{
                Stream<String> lines = Files.lines(currentFilePath);
                txtOriginal.setText("");
                lines.forEach(line -> txtOriginal.append(line+"\n"));
                lines.close();
            } catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    private void searchFile(){
        String strSearch = txtSearch.getText();
        if(strSearch.isEmpty()){
            return;
        }

        String[] strOriginal = txtOriginal.getText().split("\n");
        Stream<String> lines = Stream.of(strOriginal);
        Stream<String> strFiltered = lines.filter(line -> line.contains(strSearch));
        txtFiltered.setText("");
        strFiltered.forEach(line -> txtFiltered.append(line + "\n"));
    }
}
