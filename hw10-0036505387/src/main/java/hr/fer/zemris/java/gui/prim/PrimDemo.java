package hr.fer.zemris.java.gui.prim;

import javax.swing.*;
import java.awt.*;

public class PrimDemo extends JFrame {

    public PrimDemo() {
        super();
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Prime numbers");
        setLocation(20, 20);
        initGUI();
        pack();
    }

    private void initGUI() {
        PrimListModel model = new PrimListModel();
        JList<Integer> list1 = new JList<>(model);
        JList<Integer> list2 = new JList<>(model);
        JButton nextButton = new JButton("Sljedeći");

        Container cp = getContentPane();

        cp.setLayout(new BorderLayout());

        cp.add(nextButton, BorderLayout.PAGE_END);

        nextButton.addActionListener(e -> model.next());

        JPanel listPanel = new JPanel(new GridLayout(0, 2));

        listPanel.add(new JScrollPane(list1));
        listPanel.add(new JScrollPane(list2));

        cp.add(listPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PrimDemo().setVisible(true);
        });
    }

}
