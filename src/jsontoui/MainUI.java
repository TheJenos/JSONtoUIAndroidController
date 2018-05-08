/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsontoui;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Vector;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author Thanura
 */
public class MainUI extends javax.swing.JFrame {

    /**
     * Creates new form MainUI
     */
    File file;
    DefaultTreeModel dtm;
    DatabaseReference sellectedfile;
    private boolean Firstime = true;

    private JPopupMenu getPopUpMenu() {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem item2 = new JMenuItem("Add New Value");
        item2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) jTree1.getLastSelectedPathComponent();
                if (selectedNode == null) {
                    return;
                }
                String jTreeVarSelectedPath = "";
                Object[] paths = selectedNode.getPath();
                for (int i = 0; i < paths.length; i++) {
                    jTreeVarSelectedPath += paths[i];
                    if (i + 1 < paths.length) {
                        jTreeVarSelectedPath += "/";

                    }
                }
                jTreeVarSelectedPath = jTreeVarSelectedPath.replace("App/", "");
                String key = JOptionPane.showInputDialog(MainUI.this, "Key");
                if (key == null) {
                    return;
                }
                String value = JOptionPane.showInputDialog(MainUI.this, "Value");
                if (value == null) {
                    return;
                }
                FirebaseDatabase.getInstance().getReference(jTreeVarSelectedPath + "/" + key).setValue(value);

            }
        });
        menu.add(item2);
        JMenuItem item = new JMenuItem("Delete");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) jTree1.getLastSelectedPathComponent();
                if (selectedNode == null) {
                    return;
                }
                String jTreeVarSelectedPath = "";
                Object[] paths = selectedNode.getPath();
                for (int i = 0; i < paths.length; i++) {
                    jTreeVarSelectedPath += paths[i];
                    if (i + 1 < paths.length) {
                        jTreeVarSelectedPath += "/";

                    }
                }
                jTreeVarSelectedPath = jTreeVarSelectedPath.replace("App/", "");
                FirebaseDatabase.getInstance().getReference(jTreeVarSelectedPath).removeValue();

            }
        });
        menu.add(item);
        return menu;
    }

    public MainUI() {
        initComponents();
        try {
            jTree1.setComponentPopupMenu(getPopUpMenu());
            Utils.intColor();
            ClassLoader classLoader = this.getClass().getClassLoader();
            if (classLoader instanceof URLClassLoader) {
                URLClassLoader urlClassLoader = URLClassLoader.class.cast(classLoader);
                URL resourceUrl = urlClassLoader.findResource("jsontoui/JsonToUI.json");
                if ("file".equals(resourceUrl.getProtocol())) {
                    URI uri = resourceUrl.toURI();
                    file = new File(uri);
                    FileInputStream serviceAccount = new FileInputStream(file);
                    FirebaseOptions options = new FirebaseOptions.Builder()
                            .setCredential(FirebaseCredentials.fromCertificate(serviceAccount))
                            .setDatabaseUrl("https://jsontoui.firebaseio.com/")
                            .build();

                    FirebaseApp.initializeApp(options);
                    DatabaseReference ref = FirebaseDatabase
                            .getInstance()
                            .getReference("/");
                    jProgressBar1.setIndeterminate(true);
                    DefaultMutableTreeNode treeNode1 = new DefaultMutableTreeNode("App");
                    dtm = new javax.swing.tree.DefaultTreeModel(treeNode1);
                    jTree1.setModel(dtm);
                    jColorChooser1.getSelectionModel().addChangeListener(new ChangeListener() {
                        @Override
                        public void stateChanged(ChangeEvent e) {
                            String RGB = String.format("#%06X", (0xFFFFFF & jColorChooser1.getColor().hashCode()));
                            jTextArea1.setText(RGB);
                        }
                    });
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Firstime = true;
                            treeNode1.removeAllChildren();
                            String old = getExpansionState();
                            jProgressBar1.setIndeterminate(true);
                            addNode(dataSnapshot, treeNode1);
                            jProgressBar1.setIndeterminate(false);
                            dtm.reload();
                            jTree1.repaint();
                            setExpansionState(old);
                        }

                        @Override
                        public void onCancelled(DatabaseError de) {
                            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void expandAll() {
        int row = 0;
        while (row < jTree1.getRowCount()) {
            jTree1.expandRow(row);
            row++;
        }
    }

    public void addNode(DataSnapshot ds, DefaultMutableTreeNode tm) {
        if (Firstime) {
            Firstime = false;
            if (ds.hasChildren()) {
                for (DataSnapshot object : ds.getChildren()) {
                    addNode(object, tm);
                }
            }
            return;
        }
        DefaultMutableTreeNode dtm = new DefaultMutableTreeNode(ds.getKey());
        tm.add(dtm);
        if (ds.hasChildren()) {
            for (DataSnapshot object : ds.getChildren()) {
                addNode(object, dtm);
            }
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jProgressBar1 = new javax.swing.JProgressBar();
        jButton1 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        jColorChooser1 = new javax.swing.JColorChooser();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jToggleButton1 = new javax.swing.JToggleButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jTextField11 = new javax.swing.JTextField();
        jTextField12 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jTextField13 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jTextField14 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jTextField15 = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jTextField16 = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jTextField17 = new javax.swing.JTextField();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jButton3 = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        jTextField18 = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel21 = new javax.swing.JLabel();
        jTextField19 = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("App");
        jTree1.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jTree1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTree1MouseClicked(evt);
            }
        });
        jTree1.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                jTree1ValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jTree1);

        jButton1.setText("Save");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        jTabbedPane1.addTab("Text Editor", jScrollPane2);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jColorChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, 659, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jColorChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, 442, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Color Editor", jPanel1);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Path Changer"));

        jLabel1.setText("Old Path");

        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        jLabel2.setText("New Path");

        jButton2.setText("Rename/Move");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton4.setText("Duplicate");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jToggleButton1.setText("Delete");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jToggleButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField2)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton4)
                    .addComponent(jToggleButton1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Add Component"));

        jLabel3.setText("Component Name");

        jLabel4.setText("Component Type");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "button", "textview", "layout", "togglebutton", "checkbox", "radiobutton", "progressbar", "gridlayout", "radiogroup" }));

        jLabel5.setText("Background Color");

        jLabel6.setText("Lable");

        jLabel7.setText("Padding-Left");

        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });

        jLabel8.setText("Padding-Top");

        jLabel9.setText("Padding-Right");

        jLabel10.setText("Padding-Bottom");

        jLabel11.setText("Padding - All");

        jLabel12.setText("Margin-Top");

        jLabel13.setText("Margin-Right");

        jLabel14.setText("Margin-Bottom");

        jLabel15.setText("Margin - All");

        jLabel16.setText("Margin-Left");

        jLabel17.setText("Width");

        jLabel18.setText("Height");

        jCheckBox1.setText("height-matchparent");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jCheckBox2.setText("width-matchparent");
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });

        jButton3.setText("Insert Or Save");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel19.setText("Id");

        jLabel20.setText("Gravity");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "center", "bottom", "center_horizontal", "center_vertical", "top", "right", "left", "fill", "fill_horizontal", "fill_vertical", "no_gravity", "start", "end", "clip_horizontal", "clip_vertical" }));

        jLabel21.setText("Selected View");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel7))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextField6, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
                                    .addComponent(jTextField7)
                                    .addComponent(jTextField8)))
                            .addComponent(jLabel11)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(68, 68, 68)
                                .addComponent(jLabel6)))
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addComponent(jLabel19)
                            .addGap(76, 76, 76)
                            .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addComponent(jTextField18)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addComponent(jLabel20)
                                    .addGap(101, 101, 101))
                                .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jTextField3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
                            .addComponent(jTextField4, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jTextField5)
                            .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(jCheckBox2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(10, 10, 10))
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(jTextField16)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel18)
                                    .addComponent(jTextField17)
                                    .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 117, Short.MAX_VALUE))
                                .addGap(18, 18, 18))))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField14))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel16))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextField12)
                                    .addComponent(jTextField11, javax.swing.GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)
                                    .addComponent(jTextField13)))
                            .addComponent(jLabel15)
                            .addComponent(jTextField15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)
                                    .addComponent(jTextField19))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3)))
                        .addContainerGap())))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel17))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jCheckBox2, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                    .addComponent(jCheckBox1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                            .addComponent(jLabel21)
                            .addGap(35, 35, 35))
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel19)
                                .addComponent(jLabel20))
                            .addGap(32, 32, 32)))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 14, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Other Tools", jPanel2);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Propertie", "Value"
            }
        ));
        jScrollPane3.setViewportView(jTable1);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Previews"));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 420, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 442, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Properties", jPanel5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTabbedPane1))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jTabbedPane1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1)
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTree1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTree1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTree1MouseClicked
    private StringBuilder sb;

    public String getExpansionState() {

        sb = new StringBuilder();

        for (int i = 0; i < jTree1.getRowCount(); i++) {
            TreePath tp = jTree1.getPathForRow(i);
            if (jTree1.isExpanded(i)) {
                sb.append(tp.toString());
                sb.append(",");
            }
        }

        return sb.toString();

    }

    public void setExpansionState(String s) {

        for (int i = 0; i < jTree1.getRowCount(); i++) {
            TreePath tp = jTree1.getPathForRow(i);
            if (s.contains(tp.toString())) {
                jTree1.expandRow(i);
            }
        }
    }

    private void jTree1ValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_jTree1ValueChanged
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) jTree1.getLastSelectedPathComponent();
        if (selectedNode == null) {
            return;
        }
        String jTreeVarSelectedPath = "";
        Object[] paths = selectedNode.getPath();
        for (int i = 0; i < paths.length; i++) {
            jTreeVarSelectedPath += paths[i];
            if (i + 1 < paths.length) {
                jTreeVarSelectedPath += "/";

            }
        }
        jTreeVarSelectedPath = jTreeVarSelectedPath.replace("App/", "");
        if (paths.length > 0 && paths[paths.length - 2].toString().equals("view")) {
            jTabbedPane1.setSelectedIndex(2);
            String rootpath = jTreeVarSelectedPath.replace("/" + paths[paths.length - 1], "");
            jTextField3.setText(paths[paths.length - 1].toString());
            jTextField19.setText(rootpath);
            DatabaseReference BasicPath = FirebaseDatabase.getInstance().getReference(jTextField19.getText() + "/" + jTextField3.getText());
            getValue(BasicPath, "bg-color", jTextField4);
            getValue(BasicPath, "lable", jTextField5);
            getValue(BasicPath, "padding-left", jTextField6);
            getValue(BasicPath, "padding-top", jTextField7);
            getValue(BasicPath, "padding-right", jTextField8);
            getValue(BasicPath, "padding-bottom", jTextField9);
            getValue(BasicPath, "padding", jTextField10);
            getValue(BasicPath, "margin-left", jTextField11);
            getValue(BasicPath, "margin-top", jTextField12);
            getValue(BasicPath, "margin-right", jTextField13);
            getValue(BasicPath, "margin-bottom", jTextField14);
            getValue(BasicPath, "margin", jTextField15);
            getValue(BasicPath, "width", jTextField16);
            getValue(BasicPath, "height", jTextField17);
            getValue(BasicPath, "id", jTextField18);
            BasicPath.child("viewtype").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot ds) {
                    jComboBox1.setSelectedItem(ds.getValue() + "");
                }

                @Override
                public void onCancelled(DatabaseError de) {
                }
            });
            BasicPath.child("gravity").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot ds) {
                    jComboBox2.setSelectedItem(ds.getValue() + "");
                }

                @Override
                public void onCancelled(DatabaseError de) {
                }
            });
            BasicPath.child("height-matchparent").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot ds) {
                    jCheckBox1.setSelected(ds.getValue() != null);
                }

                @Override
                public void onCancelled(DatabaseError de) {
                }
            });
            BasicPath.child("width-matchparent").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot ds) {
                    jCheckBox2.setSelected(ds.getValue() != null);
                }

                @Override
                public void onCancelled(DatabaseError de) {
                }
            });
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);
            dtm.addRow(dataSet(rootpath, rootpath));
            AddDataToTable(BasicPath, "bg-color", dtm);
            AddDataToTable(BasicPath, "lable", dtm);
            AddDataToTable(BasicPath, "padding-left", dtm);
            AddDataToTable(BasicPath, "padding-top", dtm);
            AddDataToTable(BasicPath, "padding-right", dtm);
            AddDataToTable(BasicPath, "padding-bottom", dtm);
            AddDataToTable(BasicPath, "padding", dtm);
            AddDataToTable(BasicPath, "margin-left", dtm);
            AddDataToTable(BasicPath, "margin-top", dtm);
            AddDataToTable(BasicPath, "margin-right", dtm);
            AddDataToTable(BasicPath, "margin-bottom", dtm);
            AddDataToTable(BasicPath, "margin", dtm);
            AddDataToTable(BasicPath, "width", dtm);
            AddDataToTable(BasicPath, "height", dtm);
            AddDataToTable(BasicPath, "id", dtm);

        }
        if (selectedNode.toString().equals("view")) {
            jTextField19.setText(jTreeVarSelectedPath);
        }
        if (selectedNode.getChildCount() > 0) {
            jTextField1.setText(jTreeVarSelectedPath);
            jTextField2.setText(jTreeVarSelectedPath);
            return;
        }
        setTitle("Sellected Path - " + jTreeVarSelectedPath);
        sellectedfile = FirebaseDatabase.getInstance().getReference(jTreeVarSelectedPath);
        jProgressBar1.setIndeterminate(true);
        sellectedfile.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {
                jProgressBar1.setIndeterminate(true);
                String s = ds.getValue() + "";
                if (Utils.Mcolors.get(s) != null) {
                    jTabbedPane1.setSelectedIndex(1);
                    jColorChooser1.setColor(Color.decode(Utils.Mcolors.get(s)));
                } else if (s.startsWith("#") && s.length() == 7) {
                    jTabbedPane1.setSelectedIndex(1);
                    jColorChooser1.setColor(Color.decode(s));
                } else {
                    jTabbedPane1.setSelectedIndex(0);
                }
                jTextArea1.setText(ds.getValue() + "");
                jProgressBar1.setIndeterminate(false);
            }

            @Override
            public void onCancelled(DatabaseError de) {

            }
        });
    }//GEN-LAST:event_jTree1ValueChanged

    private void AddDataToTable(DatabaseReference ds, String s,final DefaultTableModel dtm) {
        ds.child(s).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {
                dtm.addRow(dataSet(s, ds.getValue()+""));
            }

            @Override
            public void onCancelled(DatabaseError de) {
            }
        });
    }

    private Vector dataSet(String s, String s1) {
        Vector v = new Vector();
        v.add(s);
        v.add(s1);
        return v;
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (sellectedfile == null) {
            return;
        }
        sellectedfile.setValue(jTextArea1.getText());
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        jTextField2.setText(jTextField1.getText());
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        renameData(jTextField1.getText(), jTextField2.getText());
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        duplicateData(jTextField1.getText(), jTextField2.getText());
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        FirebaseDatabase.getInstance().getReference(jTextField1.getText()).removeValue();
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (isEmpty(jTextField19.getText())) {
            return;
        }
        if (isEmpty(jTextField3.getText())) {
            return;
        }
        DatabaseReference BasicPath = FirebaseDatabase.getInstance().getReference(jTextField19.getText() + "/" + jTextField3.getText());
        BasicPath.child("viewtype").setValue((String) jComboBox1.getSelectedItem());
        if (((String) jComboBox1.getSelectedItem().toString()).equalsIgnoreCase("layout")) {
            BasicPath.child("view").setValue("null");
        }
        BasicPath.child("gravity").setValue((String) jComboBox2.getSelectedItem());
        setValues(BasicPath, "bg-color", jTextField4.getText());
        setValues(BasicPath, "lable", jTextField5.getText());
        setValues(BasicPath, "padding-left", jTextField6.getText());
        setValues(BasicPath, "padding-top", jTextField7.getText());
        setValues(BasicPath, "padding-right", jTextField8.getText());
        setValues(BasicPath, "padding-bottom", jTextField9.getText());
        setValues(BasicPath, "padding", jTextField10.getText());
        setValues(BasicPath, "margin-left", jTextField11.getText());
        setValues(BasicPath, "margin-top", jTextField12.getText());
        setValues(BasicPath, "margin-right", jTextField13.getText());
        setValues(BasicPath, "margin-bottom", jTextField14.getText());
        setValues(BasicPath, "margin", jTextField15.getText());
        setValues(BasicPath, "width", jTextField16.getText());
        setValues(BasicPath, "height", jTextField17.getText());
        setValues(BasicPath, "id", jTextField18.getText());
        setValues(BasicPath, "width-matchparent", jCheckBox2.isSelected() ? "True" : null);
        setValues(BasicPath, "height-matchparent", jCheckBox1.isSelected() ? "True" : null);

    }//GEN-LAST:event_jButton3ActionPerformed

    public void getValue(DatabaseReference ds, String key, JTextField tf) {
        ds.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {
                String s = (ds.getValue() == null) ? null : ds.getValue() + "";
                tf.setText(s);
            }

            @Override
            public void onCancelled(DatabaseError de) {
            }
        });
    }

    public void setValues(DatabaseReference BasicPath, String key, String val) {
        if (!isEmpty(val)) {
            BasicPath.child(key).setValue(val);
        }
    }

    public boolean isEmpty(String s) {
        return s == null || s.length() < 1 || s.equals("");
    }

    public void renameData(String s1, String s2) {
        FirebaseDatabase.getInstance().getReference(s1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {
                FirebaseDatabase.getInstance().getReference(s2).setValue(ds.getValue());
                FirebaseDatabase.getInstance().getReference(s1).removeValue();
            }

            @Override
            public void onCancelled(DatabaseError de) {
            }
        });
    }

    public void duplicateData(String s1, String s2) {
        FirebaseDatabase.getInstance().getReference(s1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {
                FirebaseDatabase.getInstance().getReference(s2).setValue(ds.getValue());
            }

            @Override
            public void onCancelled(DatabaseError de) {
            }
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JColorChooser jColorChooser1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField18;
    private javax.swing.JTextField jTextField19;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JTree jTree1;
    // End of variables declaration//GEN-END:variables
}
