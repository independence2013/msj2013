/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Database.DatabaseAccess;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;  
import java.io.File;
import java.io.FileInputStream;  
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javazoom.jl.player.Player;
import org.apache.commons.io.FilenameUtils;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

/**
 * Code for GUI
 */

public class GUI extends javax.swing.JFrame {
    
    static MP3Info[] mp3info;
    static File[] allwavfiles;
    static File songfile = null;
    Clip clip = null;
    boolean x = true;
    Thread thread = new Thread(new thread1());
    long playloc = 0;
    static DatabaseAccess dba = new DatabaseAccess();
    static Connection con = dba.startconnection("orcl");
    static AudioWaveformCreator awc = new AudioWaveformCreator();
    DBRow[] result = new DBRow[25];
    int[] subsong = new int[31];
    String lyricsr = "";
    
    public static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                (l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }
    
    public static int[] convertIntegers(List<Integer> integers){
        int[] ret = new int[integers.size()];
        Iterator<Integer> iterator = integers.iterator();
        for (int i = 0; i < ret.length; i++)
        {
            ret[i] = iterator.next().intValue();
        }
        return ret;
    }
    
    public static double getDurationOfWavInSeconds(File file){   
        AudioInputStream stream = null;
        try 
        {
            stream = AudioSystem.getAudioInputStream(file);
            AudioFormat format = stream.getFormat();
            return file.length() / (format.getSampleRate()*(format.getSampleSizeInBits() / 8.0)*format.getChannels());
        }
        catch (Exception e) 
        {
            // log an error
            return -1;
        }
        finally
        {
            try { stream.close(); } catch (Exception ex) { }
        }
    }
    
    public File loadsong(String name, String artist){
        File out = null;
        try{
            out = new File(dba.songdir(con,null,name,artist));
        } catch (Exception e){
            e.printStackTrace();
        }
        playloc = 0;
        currenttime.setText("0:00");
        audioProgressSlider1.setValue(0);
        playpause1.setText("Play");
        x = true;
        if(!thread.isInterrupted()){
            thread.interrupt();
        }
        if(clip != null){
            clip.close();
        }
        return out;
    }

    //File newfile = new File("C:\\Users\\Mitchell\\Documents\\leftright.wav");
    
    public class thread1 implements Runnable{
        public void run(){
            while ((clip.getMicrosecondPosition() < clip.getMicrosecondLength())&&!thread.isInterrupted()){
                long sprog = (clip.getMicrosecondPosition());
                long slength = (clip.getMicrosecondLength());
                String seconds = Long.toString((sprog/1000000)%60);
                long minutes = ((sprog/1000000)-Integer.parseInt(seconds))/60;
                long position = (long)(sprog*1000/slength); //aduio progress abr is 1000 units long
                System.out.println(sprog + " / " + slength + " * 1000 = " + position);
                audioProgressSlider1.setValue(safeLongToInt(position));
                if(Integer.parseInt(seconds)<10){
                    seconds = "0" + seconds;
                }
                currenttime.setText(minutes+":"+seconds);
                long totminutes = ((slength/1000000)-(slength/1000000)%60)/60;
                String totseconds = Long.toString((slength/1000000)%60);
                if(Integer.parseInt(totseconds)<10){
                   totseconds = "0" + totseconds;
                }
                timeleft.setText(totminutes+":"+totseconds);
                try{Thread.sleep(50);} //Sleep 50 milliseconds
                catch (InterruptedException err){
                    return;
                }
            }
        }
    }
    /**
     * Creates new form newGUI
     */
    public GUI() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        mood5 = new javax.swing.JCheckBox();
        mood6 = new javax.swing.JCheckBox();
        albumtext = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        lyrictext = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        mood4 = new javax.swing.JCheckBox();
        mood1 = new javax.swing.JCheckBox();
        mood0 = new javax.swing.JCheckBox();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        title = new javax.swing.JTextField();
        artist = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        mood2 = new javax.swing.JCheckBox();
        mood3 = new javax.swing.JCheckBox();
        length2 = new javax.swing.JTextField();
        mood7 = new javax.swing.JCheckBox();
        jLabel24 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        year1 = new javax.swing.JCheckBox();
        year2 = new javax.swing.JCheckBox();
        year3 = new javax.swing.JCheckBox();
        year5 = new javax.swing.JCheckBox();
        year6 = new javax.swing.JCheckBox();
        year7 = new javax.swing.JCheckBox();
        jLabel4 = new javax.swing.JLabel();
        year4 = new javax.swing.JCheckBox();
        year0 = new javax.swing.JCheckBox();
        genreselect = new javax.swing.JComboBox();
        searchButton2 = new javax.swing.JButton();
        overallmood = new javax.swing.JCheckBox();
        subsongmood = new javax.swing.JCheckBox();
        jPanel4 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        lyrics = new javax.swing.JScrollPane();
        lyricsoutput = new javax.swing.JTextArea();
        jLabel26 = new javax.swing.JLabel();
        playpause1 = new javax.swing.JButton();
        waveform = new javax.swing.JLabel();
        stopButton1 = new javax.swing.JButton();
        table = new javax.swing.JScrollPane();
        outputtable = new javax.swing.JTable();
        audioProgressSlider1 = new javax.swing.JSlider();
        timeleft = new javax.swing.JLabel();
        currenttime = new javax.swing.JLabel();
        waveformlabel = new javax.swing.JLabel();
        color0 = new javax.swing.JLabel();
        color1 = new javax.swing.JLabel();
        color2 = new javax.swing.JLabel();
        color3 = new javax.swing.JLabel();
        color4 = new javax.swing.JLabel();
        color5 = new javax.swing.JLabel();
        color6 = new javax.swing.JLabel();
        color7 = new javax.swing.JLabel();
        moodkeylabel = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        viewSongs = new javax.swing.JMenuItem();
        addSongs = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        loadClassifier = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();

        jLabel1.setText("jLabel1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1024, 600));
        setResizable(false);

        jPanel2.setFocusCycleRoot(true);
        jPanel2.setMaximumSize(new java.awt.Dimension(3840, 2160));
        jPanel2.setMinimumSize(new java.awt.Dimension(1024, 576));
        jPanel2.setPreferredSize(new java.awt.Dimension(1280, 720));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel3.setToolTipText("");
        jPanel3.setAutoscrolls(true);
        jPanel3.setMaximumSize(new java.awt.Dimension(320, 2160));
        jPanel3.setMinimumSize(new java.awt.Dimension(320, 1280));
        jPanel3.setName(""); // NOI18N
        jPanel3.setOpaque(false);
        jPanel3.setPreferredSize(new java.awt.Dimension(320, 720));

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel18.setText("Search");

        mood5.setText("Mood 5");

        mood6.setText("Mood 6");

        albumtext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                albumtextjTextField1ActionPerformed(evt);
            }
        });

        jLabel19.setText("Lyrics");

        lyrictext.setText("(type any part of a song's lyrics you remember here)");
        lyrictext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lyrictextjTextField2ActionPerformed(evt);
            }
        });

        jLabel20.setText("Album");

        mood4.setText("Mood 4");
        mood4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mood4jCheckBox5ActionPerformed(evt);
            }
        });

        mood1.setText("Mood 1");

        mood0.setText("Mood 0");

        jLabel21.setText("Artist");

        jLabel22.setText("Mood(s):");

        title.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                titleActionPerformed(evt);
            }
        });

        artist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                artistartistActionPerformed(evt);
            }
        });

        jLabel23.setText("Length (min:sec)");

        mood2.setText("Mood 2");

        mood3.setText("Mood 3");

        length2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                length2lengthActionPerformed(evt);
            }
        });

        mood7.setText("Mood 7");

        jLabel24.setText("Song Name");

        jLabel3.setText("Genre(s)");

        year1.setText("1950-1959");
        year1.setToolTipText("");

        year2.setText("1960-1969");

        year3.setText("1970-1979");
        year3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                year3ActionPerformed(evt);
            }
        });

        year5.setText("1990-1999");

        year6.setText("2000-2009");

        year7.setText("2010-2014");

        jLabel4.setText("Year(s) Released");

        year4.setText("1980-1989");
        year4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                year4jCheckBox5ActionPerformed(evt);
            }
        });

        year0.setText("1940-1949");
        year0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                year0ActionPerformed(evt);
            }
        });

        genreselect.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Choose One:", "Alternative", "Christian", "Classical", "Country", "Dance", "Electronic", "Gospel", "Hip Hop", "Holiday", "Pop", "R&B", "Rap", "Rock", "Soul", "Soundtrack", " " }));
        genreselect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                genreselectActionPerformed(evt);
            }
        });

        searchButton2.setText("Search");
        searchButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButton2searchButtonActionPerformed(evt);
            }
        });

        overallmood.setText("Overall");

        subsongmood.setText("Subsong");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel22)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(mood2)
                                .addGap(18, 18, 18)
                                .addComponent(mood6))
                            .addComponent(jLabel3)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(mood3)
                                .addGap(18, 18, 18)
                                .addComponent(mood7))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(year0)
                                .addGap(18, 18, 18)
                                .addComponent(year4))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(year1)
                                .addGap(18, 18, 18)
                                .addComponent(year5))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(year2)
                                .addGap(18, 18, 18)
                                .addComponent(year6))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(year3)
                                .addGap(18, 18, 18)
                                .addComponent(year7))
                            .addComponent(jLabel4)
                            .addComponent(jLabel18)
                            .addComponent(jLabel24)
                            .addComponent(jLabel23)
                            .addComponent(jLabel21)
                            .addComponent(jLabel20)
                            .addComponent(albumtext)
                            .addComponent(artist)
                            .addComponent(jLabel19)
                            .addComponent(lyrictext)
                            .addComponent(length2)
                            .addComponent(title)
                            .addComponent(genreselect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(searchButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(mood0)
                                .addGap(18, 18, 18)
                                .addComponent(mood4))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(mood1)
                                .addGap(18, 18, 18)
                                .addComponent(mood5)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(subsongmood)
                            .addComponent(overallmood))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(title, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(artist, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(albumtext, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(length2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lyrictext, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mood0)
                    .addComponent(mood4)
                    .addComponent(overallmood))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mood1)
                    .addComponent(mood5)
                    .addComponent(subsongmood))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mood2)
                    .addComponent(mood6))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mood3)
                    .addComponent(mood7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(genreselect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addGap(9, 9, 9)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(year0)
                    .addComponent(year4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(year1)
                    .addComponent(year5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(year2)
                    .addComponent(year6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(year3)
                    .addComponent(year7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(searchButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel25.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel25.setText("Lyrics");

        lyricsoutput.setColumns(20);
        lyricsoutput.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        lyricsoutput.setLineWrap(true);
        lyricsoutput.setRows(5);
        lyricsoutput.setWrapStyleWord(true);
        lyrics.setViewportView(lyricsoutput);

        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel26.setText("Results");

        playpause1.setText("Play");
        playpause1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playpauseActionPerformed(evt);
            }
        });

        stopButton1.setText("Stop");
        stopButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopButton1ActionPerformed(evt);
            }
        });

        outputtable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Song Name", "Artist", "Album", "Length", "Overall Mood", "Genre", "Year Released"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        outputtable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                outputtableMouseClicked(evt);
            }
        });
        table.setViewportView(outputtable);

        audioProgressSlider1.setMaximum(1000);
        audioProgressSlider1.setValue(0);
        audioProgressSlider1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                audioProgressSlider1MouseDragged(evt);
            }
        });

        timeleft.setText("0:00");

        currenttime.setText("0:00");

        waveformlabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        waveformlabel.setText("Waveform and Subsong Mood Visualizer");

        color0.setBackground(new java.awt.Color(255, 255, 0));
        color0.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        color0.setText("0");
        color0.setOpaque(true);

        color1.setBackground(new java.awt.Color(255, 102, 0));
        color1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        color1.setText("1");
        color1.setOpaque(true);

        color2.setBackground(new java.awt.Color(0, 153, 0));
        color2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        color2.setText("2");
        color2.setOpaque(true);

        color3.setBackground(new java.awt.Color(153, 153, 153));
        color3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        color3.setText("3");
        color3.setOpaque(true);

        color4.setBackground(new java.awt.Color(102, 0, 204));
        color4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        color4.setText("4");
        color4.setOpaque(true);

        color5.setBackground(new java.awt.Color(0, 51, 255));
        color5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        color5.setText("5");
        color5.setOpaque(true);

        color6.setBackground(new java.awt.Color(255, 0, 0));
        color6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        color6.setText("6");
        color6.setOpaque(true);

        color7.setBackground(new java.awt.Color(0, 128, 128));
        color7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        color7.setText("7");
        color7.setOpaque(true);

        moodkeylabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        moodkeylabel.setText("Mood Color Key");

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(waveformlabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(waveform, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(audioProgressSlider1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(table, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                .addComponent(currenttime)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(timeleft))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel26, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addComponent(playpause1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(stopButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addComponent(color0, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(color1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(color2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(color3, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(color4, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(color5, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(color6, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(color7, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(moodkeylabel))
                                        .addGap(112, 112, 112)
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel25)
                                            .addComponent(lyrics, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel26)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(table, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(waveformlabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(waveform, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(audioProgressSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(currenttime)
                    .addComponent(timeleft))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(playpause1)
                    .addComponent(stopButton1)
                    .addComponent(jLabel25))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(moodkeylabel)
                        .addGap(6, 6, 6)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(color0, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(color1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(color2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(color3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(color4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(color5, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(color6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(color7, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(lyrics, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jSeparator1)
        );

        jMenuBar1.setMaximumSize(new java.awt.Dimension(3840, 21));
        jMenuBar1.setMinimumSize(new java.awt.Dimension(1024, 21));
        jMenuBar1.setName(""); // NOI18N

        jMenu1.setText("Songs");

        viewSongs.setText("View All");
        viewSongs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewSongsActionPerformed(evt);
            }
        });
        jMenu1.add(viewSongs);

        addSongs.setText("Add New");
        jMenu1.add(addSongs);

        jMenuItem3.setText("jMenuItem3");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Advanced");

        loadClassifier.setText("Load Classifier");
        jMenu2.add(loadClassifier);

        jMenuItem1.setText("Change Directory");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 1275, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 1, Short.MAX_VALUE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 723, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void viewSongsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewSongsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_viewSongsActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void stopButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopButton1ActionPerformed
        // TODO add your handling code here:
        playpause1.setText("Play");
        x = true;
        playloc = 0;
        if((clip != null)){
            clip.stop();
            clip.setMicrosecondPosition(0);
        }
        if(!thread.isInterrupted()&&thread.isAlive()){
            thread.interrupt();
        }
        currenttime.setText("0:00");
        audioProgressSlider1.setValue(0);
    }//GEN-LAST:event_stopButton1ActionPerformed

    private void playpauseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playpauseActionPerformed
        // TODO add your handling code here:
        if(x == true){
            if(audioProgressSlider1.getValue()!=0){
                playloc = clip.getMicrosecondLength()*audioProgressSlider1.getValue()/1000;
            }
            playpause1.setText("Pause");
            x = false;
            System.out.println(x);
            try{
                clip = AudioSystem.getClip();
                // getAudioInputStream() also accepts a File or InputStream
                AudioInputStream ais = AudioSystem.getAudioInputStream(songfile);
                clip.open(ais);
                clip.setMicrosecondPosition(playloc);
                clip.start();
                System.out.println(thread.isInterrupted());
                if(!thread.isAlive()){
                    thread = new Thread(new thread1());
                    thread.start();
                }
                else if(thread.isInterrupted()){
                    thread.interrupted();
                }

            } catch (Exception e){
                e.printStackTrace();
            }
        } else {
            if(!thread.isInterrupted()){
                thread.interrupt();
                System.out.println("Interrupted!");
            }
            playpause1.setText("Play");
            playloc = clip.getMicrosecondPosition();
            clip.stop();
            x = true;
        }
    }//GEN-LAST:event_playpauseActionPerformed

    private void year0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_year0ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_year0ActionPerformed

    private void year4jCheckBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_year4jCheckBox5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_year4jCheckBox5ActionPerformed

    private void year3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_year3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_year3ActionPerformed

    private void length2lengthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_length2lengthActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_length2lengthActionPerformed

    private void searchButton2searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButton2searchButtonActionPerformed
        int moodlevel = 0;
        String titleText = title.getText();
        String artistText = artist.getText();
        ArrayList<Integer> moodt = new ArrayList<Integer>();
        if(mood0.isSelected()){
            moodt.add(0);
        }
        if(mood1.isSelected()){
            moodt.add(1);
        }
        if(mood2.isSelected()){
            moodt.add(2);
        }
        if(mood3.isSelected()){
            moodt.add(3);
        }
        if(mood4.isSelected()){
            moodt.add(4);
        }
        if(mood5.isSelected()){
            moodt.add(5);
        }
        if(mood6.isSelected()){
            moodt.add(6);
        }
        if(mood7.isSelected()){
            moodt.add(7);
        }
        
        if(subsongmood.isSelected()){
            moodlevel = 1;
            if(overallmood.isSelected()){
                moodlevel = 2;
            }
        }
        
        int[] moods = convertIntegers(moodt);
        int seconds = 0;
        if(!length2.getText().equals("")){
            int index = length2.getText().indexOf(":");
            seconds = Integer.parseInt(length2.getText().substring(index+1)) + Integer.parseInt(length2.getText().substring(0,index))*60;
            System.out.println(seconds);
        }
        //get results from database
        try{
            result = dba.getSearchResults(con,moods,seconds,titleText,artistText,moodlevel);
        } catch (SQLException e){
            e.printStackTrace();
        }
        
        //output to table
        for(int i = 0; i<25; i++){
            if(result[i] != null){
                outputtable.setValueAt(result[i].name,i,0);
                outputtable.setValueAt(result[i].artist,i,1);
                outputtable.setValueAt(result[i].album,i,2);
                outputtable.setValueAt(result[i].length,i,3);
                outputtable.setValueAt(result[i].mood,i,4);
                outputtable.setValueAt(result[i].name,i,5);
                outputtable.setValueAt(result[i].year,i,6);
            }
            else { //clear any unused rows (so old results don't remain)
                outputtable.setValueAt("",i,0);
                outputtable.setValueAt("",i,1);
                outputtable.setValueAt("",i,2);
                outputtable.setValueAt("",i,3);
                outputtable.setValueAt("",i,4);
                outputtable.setValueAt("",i,5);
                outputtable.setValueAt("",i,6);
            }
        }
    }//GEN-LAST:event_searchButton2searchButtonActionPerformed

    private void artistartistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_artistartistActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_artistartistActionPerformed

    private void mood4jCheckBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mood4jCheckBox5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mood4jCheckBox5ActionPerformed

    private void lyrictextjTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lyrictextjTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lyrictextjTextField2ActionPerformed

    private void albumtextjTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_albumtextjTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_albumtextjTextField1ActionPerformed

    private void titleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_titleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_titleActionPerformed

    private void outputtableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_outputtableMouseClicked
        // TODO add your handling code here:
        ImageIcon image = new ImageIcon();
        int row = outputtable.getSelectedRow();
        System.out.println(row);
        String[] selectrow = new String[6];
        for(int i = 0; i<6; i++){
            selectrow[i] = String.valueOf(outputtable.getModel().getValueAt(row, i));
            System.out.println(selectrow[i]);
        }
        try {
            subsong = dba.retrievesubsong(con,selectrow[0],selectrow[1]);
            lyricsr = dba.retrievelyrics(con,selectrow[0],selectrow[1]);
            lyricsoutput.setText(lyricsr);
            songfile = loadsong(selectrow[0],selectrow[1]);
            image = awc.AudioWaveformCreator(songfile, subsong);
            waveform.setIcon(image);
        } catch (Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_outputtableMouseClicked

    private void genreselectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_genreselectActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_genreselectActionPerformed

    private void audioProgressSlider1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_audioProgressSlider1MouseDragged
        // TODO add your handling code here:
    }//GEN-LAST:event_audioProgressSlider1MouseDragged

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        int row = outputtable.getSelectedRow();
        String[] selectrow = new String[6];
        for(int i = 0; i<6; i++){
            selectrow[i] = String.valueOf(outputtable.getModel().getValueAt(row, i));
        }
        try{
            dba.songdir(con,title.getText(),selectrow[0],selectrow[1]);
        } catch (Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])throws Exception {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        /* Create and display the form */
//        String mp3mdir = "F:\\Jeffrey\\Music\\Songs"; //directory for MP3
//        File musicdir = new File(mp3mdir);
//        //array of MP3 files (to get artist and title)
//        File[] allmp3files = musicdir.listFiles(new FilenameFilter(){ //use filter to make sure we don't read any album art files (.jpg)
//            @Override
//            public boolean accept(File dir, String name){
//                if(name.toLowerCase().endsWith(".jpg")){
//                    return false;
//                }
//                return true;
//            }
//        }
//        );
//        
//        String wavmdir = "..\\Songs"; //directory for WAV
//        musicdir = new File(wavmdir);
//        //array of WAV files (to play)
//        allwavfiles = musicdir.listFiles(new FilenameFilter(){ //use filter to make sure we don't read any album art files (.jpg)
//            @Override
//            public boolean accept(File dir, String name){
//                if(name.toLowerCase().endsWith(".wav")){
//                    return true;
//                }
//                return false;
//            }
//        }
//        );
//        
//        mp3info = new MP3Info[allmp3files.length];
//        String filedir;
//        for(int i = 0; i<allmp3files.length; i++){ //loop where there are files that haven't been run through
//            mp3info[i] = new MP3Info();
//            if(allmp3files[i].isFile()){ //if it is a file
//                filedir = allmp3files[i].getAbsolutePath(); //get absolute path of the files
//                
//                File currentfile = new File(filedir); //file loaded here
//                AudioFile f = null;
//                try {
//                    f = AudioFileIO.read(currentfile);
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//                Tag tag = f.getTag();
//                AudioHeader AudioHeader = f.getAudioHeader(); //get tags
//                mp3info[i].artist = tag.getFirst(FieldKey.ARTIST).toLowerCase().replaceAll("[']","").replaceAll("\\(.*\\)","").replaceAll("[é]","e"); //make lowercase so any capitalization issues are gone
//                mp3info[i].name = tag.getFirst(FieldKey.TITLE).toLowerCase().replaceAll("[']","").replaceAll("\\(.*\\)","").replaceAll("[é]","e");
//                mp3info[i].length = f.getAudioHeader().getTrackLength(); //gives length in seconds
//                mp3info[i].album = tag.getFirst(FieldKey.ALBUM).toLowerCase().replaceAll("[']","").replaceAll("\\(.*\\)","").replaceAll("[é]","e");
//                mp3info[i].path = filedir;
//            }
//        }
        
        songfile = null;
        
        int[] moodtest = {0,1,2,3,4,5,6,7,-1,-1,-1};
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUI().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem addSongs;
    private javax.swing.JTextField albumtext;
    private javax.swing.JTextField artist;
    private javax.swing.JSlider audioProgressSlider1;
    private javax.swing.JLabel color0;
    private javax.swing.JLabel color1;
    private javax.swing.JLabel color2;
    private javax.swing.JLabel color3;
    private javax.swing.JLabel color4;
    private javax.swing.JLabel color5;
    private javax.swing.JLabel color6;
    private javax.swing.JLabel color7;
    private javax.swing.JLabel currenttime;
    private javax.swing.JComboBox genreselect;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField length2;
    private javax.swing.JMenuItem loadClassifier;
    private javax.swing.JScrollPane lyrics;
    private javax.swing.JTextArea lyricsoutput;
    private javax.swing.JTextField lyrictext;
    private javax.swing.JCheckBox mood0;
    private javax.swing.JCheckBox mood1;
    private javax.swing.JCheckBox mood2;
    private javax.swing.JCheckBox mood3;
    private javax.swing.JCheckBox mood4;
    private javax.swing.JCheckBox mood5;
    private javax.swing.JCheckBox mood6;
    private javax.swing.JCheckBox mood7;
    private javax.swing.JLabel moodkeylabel;
    private javax.swing.JTable outputtable;
    private javax.swing.JCheckBox overallmood;
    private javax.swing.JButton playpause1;
    private javax.swing.JButton searchButton2;
    private javax.swing.JButton stopButton1;
    private javax.swing.JCheckBox subsongmood;
    private javax.swing.JScrollPane table;
    private javax.swing.JLabel timeleft;
    private javax.swing.JTextField title;
    private javax.swing.JMenuItem viewSongs;
    private javax.swing.JLabel waveform;
    private javax.swing.JLabel waveformlabel;
    private javax.swing.JCheckBox year0;
    private javax.swing.JCheckBox year1;
    private javax.swing.JCheckBox year2;
    private javax.swing.JCheckBox year3;
    private javax.swing.JCheckBox year4;
    private javax.swing.JCheckBox year5;
    private javax.swing.JCheckBox year6;
    private javax.swing.JCheckBox year7;
    // End of variables declaration//GEN-END:variables
}
