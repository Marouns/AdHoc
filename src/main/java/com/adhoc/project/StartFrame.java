package com.adhoc.project;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.ParseException;

public class StartFrame extends JFrame{
    private JButton SearchButton  = new JButton("Search");



    private JLabel lblA = new JLabel("Enter query :");
    private JLabel lblB = new JLabel("Data Path :");
    private JLabel lblC = new JLabel("Index Path :");
    private JLabel errorLabel = new JLabel();

    JCheckBox c1 = new JCheckBox("Index data", false);
    JTextField searchTextField = new JTextField();
    JTextField dataPathField = new JTextField();
    JTextField indexPathField = new JTextField();

    public StartFrame(){
        setTitle("Search Ad");
        setSize(800,600);
        setLocation(new Point(300,200));
        setLayout(null);
        setResizable(false);

        initComponent();
        initEvent();
    }

    private void initComponent(){

        searchTextField = new JTextField();
        dataPathField = new JTextField();
        indexPathField = new JTextField();
        errorLabel.setForeground(Color.RED);
        errorLabel.setBorder(new LineBorder(Color.BLACK));

        SearchButton.setBounds(300,130, 80,25);
        c1.setBounds(500, 130, 100,50);
        searchTextField.setBounds(100,10,100,20);
        dataPathField.setBounds(100,50,100,20);
        indexPathField.setBounds(100,100,100,20);


        lblA.setBounds(20,10,100,20);
        lblB.setBounds(20,50,100,20);
        lblC.setBounds(20,100,100,20);

        dataPathField.setText(Consts.dataDir);
        indexPathField.setText(Consts.indexDir);

        add(lblA);
        add(searchTextField);
        add(lblB);
        add(dataPathField);
        add(lblC);
        add(indexPathField);
        add(c1);
        add(errorLabel);


        add(SearchButton);
        errorLabel.setBounds(20,200, 200,25);
    }

    private void initEvent(){

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                System.exit(1);
            }
        });

        SearchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnSearchClick(e);
            }
        });

    }

    private void btnSearchClick(ActionEvent event){
        if(c1.isSelected()) {

            try {
                CreateIndexService.createIndex(dataPathField.getText(), indexPathField.getText());
            } catch (IOException e) {
                e.printStackTrace();
                errorLabel.setText(e.getMessage());
                }
            }

            try {
                Searcher searcher = new Searcher(indexPathField.getText());
                searcher.init();
                searcher.search(searchTextField.getText());
            } catch (Exception e) {
                e.printStackTrace();
                errorLabel.setText(e.getMessage());
            }
    }

}
