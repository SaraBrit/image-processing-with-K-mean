 package image_processing;

 import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.*;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.statistics.HistogramDataset;
 
public class TP_Rec_motif { 
    BufferedImage original; 
    BufferedImage result; 
    static BufferedImage img;
    Cluster[] clusters; 
    public static final int MODE_CONTINUOUS = 1; 
    public static final int MODE_ITERATIVE = 2; 
    static BufferedImage dstImage;
    static int w= 0;
    static int h= 0;
    static BufferedImage source;
 
     
    public static void main(String[] args) { 
    	 
    	
	    try {
		   source = ImageIO.read(new File(\\put here the path of image));
		  }
		  catch (IOException e) {
		   e.printStackTrace();
		  }
\\ filter the image 
Kernel kernel1 = new Kernel(5, 5, 
		new float[]{ 4/1344f, 18/1344f, 19/1344f, 18/1344f, 4/1344f, 18/1344f, 80/1344f, 
				132/1344f, 80/1344f, 18/1344f, 29/1344f, 132/1344f, 218/1344f, 132/1344f, 
				29/1344f, 18/1344f, 80/1344f, 132/1344f, 80/1344f, 18/1344f, 4/1344f, 
				18/1344f, 29/1344f, 18/1344f, 4/1344f});
		  ConvolveOp convolution = new ConvolveOp(kernel1);
		  BufferedImage resultat = convolution.filter(source, null);
		  
		  /* Ecriture du résultat */
		  try {
		  ImageIO.write(resultat, "JPG", new File("/home/sara/Desktop/jai/resulta.jpg"));
		  } 
		  catch (IOException e) {
		   e.printStackTrace();
		  }

        // parse arguments 
        String src = "/home/sara/Desktop/jai/resulta.jpg"; 
         
        System.out.println("entrer le valeur de k:");
         Scanner cl=new Scanner(System.in);
         int k=cl.nextInt();
       
       // int k = 5; 
        String m = "-1"; 
        int mode = 1; 
        if (m.equals("-c")) { 
            mode = MODE_ITERATIVE; 
        } else if (m.equals("-c")) { 
            mode = MODE_CONTINUOUS; 
        } 
        try{
          File  fi = new File("/home/sara/Desktop/jai/resultat.jpg");
           img = ImageIO.read(fi);
          }catch(IOException e){
            System.out.println(e);
          }
        
        TP_Rec_motif kmeans = new TP_Rec_motif(); 
        // call the function to actually start the clustering 
        dstImage = kmeans.calculate(loadImage(src), 
                                                    k,mode); 
         
     
        h=dstImage.getHeight();
        w=dstImage.getWidth();
        EventQueue.invokeLater(() -> {
            new TP_Rec_motif().display();
        });
    }

    static int BINS=256;
    static HistogramDataset dataset;
    static   XYBarRenderer renderer;
    static ChartPanel createChartPanel() {
   	 dataset = new HistogramDataset();
        Raster raster =  dstImage.getRaster();
       
        double[] r = new double[w * h];
        r = raster.getSamples(0, 0, w, h, 0, r);
        dataset.addSeries("Red", r, BINS);
        r = raster.getSamples(0, 0, w, h, 1, r);
        dataset.addSeries("Green", r, BINS);
        r = raster.getSamples(0, 0, w, h, 2, r);
        dataset.addSeries("Blue", r, BINS);
        // chart
        JFreeChart chart = ChartFactory.createHistogram("Histogram", "Value",
            "Count", dataset, PlotOrientation.VERTICAL, true, true, false);
        XYPlot plot = (XYPlot) chart.getPlot();
         renderer = (XYBarRenderer) plot.getRenderer();
        renderer.setBarPainter(new StandardXYBarPainter());
        // translucent red, green & blue
        Paint[] paintArray = {
            new Color(0x80ff0000, true),
            new Color(0x8000ff00, true),
            new Color(0x800000ff, true)
        };
        plot.setDrawingSupplier(new DefaultDrawingSupplier(
            paintArray,
            DefaultDrawingSupplier.DEFAULT_FILL_PAINT_SEQUENCE,
            DefaultDrawingSupplier.DEFAULT_OUTLINE_PAINT_SEQUENCE,
            DefaultDrawingSupplier.DEFAULT_STROKE_SEQUENCE,
            DefaultDrawingSupplier.DEFAULT_OUTLINE_STROKE_SEQUENCE,
            DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE));
        ChartPanel panel = new ChartPanel(chart);
        panel.setMouseWheelEnabled(true);
        return panel;
    }
    class  VisibleAction extends AbstractAction {
        private final int i;

        public VisibleAction(int i) {
            this.i = i;
            this.putValue(NAME, (String) dataset.getSeriesKey(i));
            this.putValue(SELECTED_KEY, true);
            renderer.setSeriesVisible(i, true);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            renderer.setSeriesVisible(i, !renderer.getSeriesVisible(i));
        }
    }
 
    static JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.add(new JCheckBox(new TP_Rec_motif().new VisibleAction(0)));
        panel.add(new JCheckBox( new TP_Rec_motif().new VisibleAction(1)));
        panel.add(new JCheckBox(new TP_Rec_motif().new VisibleAction(2)));
        return panel;
    
    }
    //histogram 2
    static ChartPanel createChartPanel2() {
      	 dataset = new HistogramDataset();
           Raster raster =  img.getRaster();
          ;
           double[] r = new double[w * h];
           r = raster.getSamples(0, 0, w, h, 0, r);
           dataset.addSeries("Red", r, BINS);
           r = raster.getSamples(0, 0, w, h, 1, r);
           dataset.addSeries("Green", r, BINS);
           r = raster.getSamples(0, 0, w, h, 2, r);
           dataset.addSeries("Blue", r, BINS);
           // chart
           JFreeChart chart = ChartFactory.createHistogram("Histogram", "Value",
               "Count", dataset, PlotOrientation.VERTICAL, true, true, false);
           XYPlot plot = (XYPlot) chart.getPlot();
            renderer = (XYBarRenderer) plot.getRenderer();
           renderer.setBarPainter(new StandardXYBarPainter());
           // translucent red, green & blue
           Paint[] paintArray = {
               new Color(0x80ff0000, true),
               new Color(0x8000ff00, true),
               new Color(0x800000ff, true)
           };
           plot.setDrawingSupplier(new DefaultDrawingSupplier(
               paintArray,
               DefaultDrawingSupplier.DEFAULT_FILL_PAINT_SEQUENCE,
               DefaultDrawingSupplier.DEFAULT_OUTLINE_PAINT_SEQUENCE,
               DefaultDrawingSupplier.DEFAULT_STROKE_SEQUENCE,
               DefaultDrawingSupplier.DEFAULT_OUTLINE_STROKE_SEQUENCE,
               DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE));
           ChartPanel panel = new ChartPanel(chart);
           panel.setMouseWheelEnabled(true);
           return panel;
       }

    static JPanel createControlPanel2() {
        JPanel panel = new JPanel();
        panel.add(new JCheckBox(new TP_Rec_motif().new VisibleAction(0)));
        panel.add(new JCheckBox( new TP_Rec_motif().new VisibleAction(1)));
        panel.add(new JCheckBox(new TP_Rec_motif().new VisibleAction(2)));
        return panel;} 

    private void display() {
        JFrame f = new JFrame("Clustering of image");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(createChartPanel2(),BorderLayout.EAST);
        f.add(createControlPanel2(), BorderLayout.SOUTH);
       // f.add(new JLabel(new ImageIcon(img)),BorderLayout.BEFORE_LINE_BEGINS);
        f.add(new JLabel(new ImageIcon(img)), BorderLayout.WEST);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        JPanel panel = new JPanel();
        f.add(panel, BorderLayout.WEST);
        JButton button = new JButton("cluster");
        panel.add(button);
        button.addActionListener (new Action1());

        
      }
      static class Action1 implements ActionListener {        
        public void actionPerformed (ActionEvent e) {     
          JFrame frame2 = new JFrame("After using K-mean");
          frame2.setVisible(true);
          frame2.setSize(200,200);
          frame2.add(createChartPanel(),BorderLayout.EAST);
          frame2.add(createControlPanel(), BorderLayout.SOUTH);
         // f.add(new JLabel(new ImageIcon(img)),BorderLayout.BEFORE_LINE_BEGINS);
          frame2.add(new JLabel(new ImageIcon(dstImage)), BorderLayout.WEST);
          frame2.pack();
          frame2.setLocationRelativeTo(null);
          frame2.setVisible(true);       
        }
      }   
     
    
    
    
   
   

		   
    public TP_Rec_motif() {    } 
     
    public BufferedImage calculate(BufferedImage image, int k, int mode) { 
        long start = System.currentTimeMillis(); 
        int w = image.getWidth(); 
       int h = image.getHeight(); 
        // create clusters 
        clusters = createClusters(image,k); 
        // create cluster lookup table 
        int[] lut = new int[w*h]; 
        Arrays.fill(lut, -1); 
         
        // at first loop all pixels will move their clusters 
        boolean pixelChangedCluster = true; 
        // loop until all clusters are stable! 
        int loops = 0; 
        while (pixelChangedCluster) { 
            pixelChangedCluster = false; 
            loops++; 
            for (int y=0;y<h;y++) { 
                for (int x=0;x<w;x++) { 
                    int pixel = image.getRGB(x, y); 
                    Cluster cluster = findMinimalCluster(pixel); 
                    if (lut[w*y+x]!=cluster.getId()) { 
                        // cluster changed 
                        if (mode==MODE_CONTINUOUS) { 
                            if (lut[w*y+x]!=-1) { 
                                // remove from possible previous  
                                // cluster 
                                clusters[lut[w*y+x]].removePixel( 
                                                            pixel); 
                            } 
                            // add pixel to cluster 
                            cluster.addPixel(pixel); 
                        } 
                        // continue looping  
                        pixelChangedCluster = true; 
                     
                        // update lut 
                        lut[w*y+x] = cluster.getId(); 
                    } 
                } 
            } 
            if (mode==MODE_ITERATIVE) { 
                // update clusters 
                for (int i=0;i<clusters.length;i++) { 
                    clusters[i].clear(); 
                } 
                for (int y=0;y<h;y++) { 
                    for (int x=0;x<w;x++) { 
                        int clusterId = lut[w*y+x]; 
                        // add pixels to cluster 
                        clusters[clusterId].addPixel( 
                                            image.getRGB(x, y)); 
                    } 
                } 
            } 
             
        } 
        // create result image 
        BufferedImage result = new BufferedImage(w, h,  
                                    BufferedImage.TYPE_INT_RGB); 
        for (int y=0;y<h;y++) { 
            for (int x=0;x<w;x++) { 
                int clusterId = lut[w*y+x]; 
                result.setRGB(x, y, clusters[clusterId].getRGB()); 
            } 
        } 
        long end = System.currentTimeMillis(); 
        System.out.println("Clustered to "+k 
                            + " clusters in "+loops 
                            +" loops in "+(end-start)+" ms."); 
        return result; 
    } 
     
    public Cluster[] createClusters(BufferedImage image, int k) { 
        Cluster[] result = new Cluster[k]; 
        int x = 0; int y = 0; 
        int dx = image.getWidth()/k; 
        int dy = image.getHeight()/k; 
        for (int i=0;i<k;i++) { 
            result[i] = new Cluster(i,image.getRGB(x, y)); 
            x+=dx; y+=dy; 
        } 
        return result; 
    } 
     
    public Cluster findMinimalCluster(int rgb) { 
        Cluster cluster = null; 
        int min = Integer.MAX_VALUE; 
        for (int i=0;i<clusters.length;i++) { 
            int distance = clusters[i].distance(rgb); 
            if (distance<min) { 
                min = distance; 
                cluster = clusters[i]; 
            } 
        } 
        return cluster; 
    } 
     
    public static void saveImage(String filename,  
            BufferedImage image) { 
        File file = new File(filename); 
        try { 
            ImageIO.write(image, "png", file); 
        } catch (Exception e) { 
            System.out.println(e.toString()+" Image '"+filename 
                                +"' saving failed."); 
        } 
    } 
     
    public static BufferedImage loadImage(String filename) { 
        BufferedImage result = null; 
        try { 
            result = ImageIO.read(new File(filename)); 
        } catch (Exception e) { 
            System.out.println(e.toString()+" Image '" 
                                +filename+"' not found."); 
        } 
        return result; 
    } 
     
    class Cluster { 
        int id; 
        int pixelCount; 
        int red; 
        int green; 
        int blue; 
        int reds; 
        int greens; 
        int blues; 
         
        public Cluster(int id, int rgb) { 
            int r = rgb>>16&0x000000FF;  
            int g = rgb>> 8&0x000000FF;  
            int b = rgb>> 0&0x000000FF;  
            red = r; 
            green = g; 
            blue = b; 
            this.id = id; 
            addPixel(rgb); 
        } 
         
        public void clear() { 
            red = 0; 
            green = 0; 
            blue = 0; 
            reds = 0; 
            greens = 0; 
            blues = 0; 
            pixelCount = 0; 
        } 
         
        int getId() { 
            return id; 
        } 
         
        int getRGB() { 
            int r = reds / pixelCount; 
            int g = greens / pixelCount; 
            int b = blues / pixelCount; 
            return 0xff000000|r<<16|g<<8|b; 
        } 
        void addPixel(int color) { 
            int r = color>>16&0x000000FF;  
            int g = color>> 8&0x000000FF;  
            int b = color>> 0&0x000000FF;  
            reds+=r; 
            greens+=g; 
            blues+=b; 
            pixelCount++; 
            red   = reds/pixelCount; 
            green = greens/pixelCount; 
            blue  = blues/pixelCount; 
        } 
         
        void removePixel(int color) { 
            int r = color>>16&0x000000FF;  
            int g = color>> 8&0x000000FF;  
            int b = color>> 0&0x000000FF;  
            reds-=r; 
            greens-=g; 
            blues-=b; 
            pixelCount--; 
            red   = reds/pixelCount; 
            green = greens/pixelCount; 
            blue  = blues/pixelCount; 
        } 
         
        int distance(int color) { 
            int r = color>>16&0x000000FF;  
            int g = color>> 8&0x000000FF;  
            int b = color>> 0&0x000000FF;  
            int rx = Math.abs(red-r); 
            int gx = Math.abs(green-g); 
            int bx = Math.abs(blue-b); 
            int d = (rx+gx+bx) / 3; 
            return d; 
        } 
    } 
  }
    
    
    
    
 
