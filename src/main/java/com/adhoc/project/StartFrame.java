package com.adhoc.project;

import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

public class StartFrame extends JFrame{
    private JButton SearchButton  = new JButton("Search");


    private  ArrayList<String> allResults = null;
    private JLabel lblA = new JLabel("Enter query :");
    private JLabel lblB = new JLabel("Data Path :");
    private JLabel lblC = new JLabel("Index Path :");
    private JLabel errorLabel = new JLabel();
    private JTextArea results = new JTextArea();


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
        errorLabel.setBounds(20,200, 200,25);
        results.setBounds(20, 250, 600, 300);
        results.setBorder(new LineBorder(Color.BLACK));
        results.setLineWrap(true);
        results.setWrapStyleWord(true);

        add(lblA);
        add(searchTextField);
        add(lblB);
        add(dataPathField);
        add(lblC);
        add(indexPathField);
        add(c1);
        add(errorLabel);
        add(SearchButton);
        add(results);

    }

    private void initEvent(){

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                System.exit(1);
            }
        });

        SearchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    btnSearchClick(e);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });

    }

    private void btnSearchClick(ActionEvent event) throws Exception {

        allResults = new ArrayList<String>();
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
                TopDocs topDocs = searcher.search(searchTextField.getText());

                for(ScoreDoc doc : topDocs.scoreDocs) {
                    allResults.add(searcher.getDocument(doc).get(LuceneConstants.TABLE_ID));
                }
                results.removeAll();
                results.setText(String.join("\t", allResults));
            } catch (Exception e) {
                e.printStackTrace();
                errorLabel.setText(e.getMessage());
            }
    }

}
