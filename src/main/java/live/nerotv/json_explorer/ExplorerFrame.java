package live.nerotv.json_explorer;

import javax.swing.*;
import java.awt.*;

public class ExplorerFrame extends JFrame {

    private JTextArea inputField;
    private JButton button;
    private JTextArea outputArea;

    public void initialise(APIExplorer instance) {
        setTitle("JSON-Explorer");

        JPanel content = new JPanel();
        content.setLayout(new GridBagLayout());

        inputField = new JTextArea();
        GridBagConstraints gbcInputField = new GridBagConstraints();
        gbcInputField.gridx = 0;
        gbcInputField.gridy = 0;
        gbcInputField.gridwidth = 1;
        gbcInputField.fill = GridBagConstraints.HORIZONTAL;
        gbcInputField.weightx = 1.0;
        content.add(inputField, gbcInputField);

        button = new JButton("Make request");
        GridBagConstraints gbcButton = new GridBagConstraints();
        gbcButton.gridx = 1;
        gbcButton.gridy = 0;
        button.addActionListener(e -> instance.makeRequest(inputField.getText()));
        content.add(button, gbcButton);

        outputArea = new JTextArea("No output yet...");
        outputArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(outputArea);
        GridBagConstraints gbcScrollPane = new GridBagConstraints();
        gbcScrollPane.gridx = 0;
        gbcScrollPane.gridy = 1;
        gbcScrollPane.gridwidth = 2;
        gbcScrollPane.fill = GridBagConstraints.BOTH;
        gbcScrollPane.weightx = 1.0;
        gbcScrollPane.weighty = 1.0;
        content.add(scrollPane, gbcScrollPane);

        add(content);

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public JTextArea getInputField() {
        return inputField;
    }

    public JButton getButton() {
        return button;
    }

    public JTextArea getOutputArea() {
        return outputArea;
    }
}
