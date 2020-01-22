import Utils.*;
import com.opencsv.exceptions.CsvException;
import com.sun.tools.jdeprscan.CSVParseException;
import processing.core.PApplet;
import processing.core.PImage;
import Utils.Follower;
import Utils.Path;
import Utils.Vector;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Scanner;


public class Main extends PApplet
{
    public static Scanner input = new Scanner(System.in);
    public static void main(String[] args) {
        PApplet.main("Main");
    }

    double lookaheadDis = 100f;
    double ImageWidth,ImageHeight;
    double MetersPerPixles,PixlesPerMeters;
    Vector lookAheadPoint;
    Path path_;
    String imagePath = "Images/frc_2020_arena.png";
    Follower follower;
    PImage backGround;
    public void settings(){
        try {
            BufferedImage image = ImageIO.read(getClass().getResource(imagePath));
            ImageWidth = image.getWidth();
            ImageHeight = image.getHeight();
            MetersPerPixles = MappingConstants.fieldWidth/ImageWidth;
            PixlesPerMeters = 1/MetersPerPixles;
            size((int)ImageWidth,(int)ImageHeight);
            backGround = loadImage(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void setup(){
        reset();
    }
    public void reset(){
        follower = null;
        path_ = new Path(0.5,1.3,2);
    }
    public void draw(){
        background(backGround);


        fill(0);
        if(!path_.isEmpty()) {
            stroke(255, 51, 0);
            for (int i = 0; i < path_.size() - 1; i++) {
                line((float)(path_.get(i).x/MetersPerPixles),(float)(path_.get(i).y/MetersPerPixles)
                        , (float)(path_.get(i+1).x/MetersPerPixles),(float)(path_.get(i+1).y/MetersPerPixles));
            }
            fill(255, 255, 0);
//            for(int i = 0; i<path_.size();i++){
//                text("[" +path_.get(i).velocity+ "]", (float)(path_.get(i).x/MetersPerPixles),(float)(path_.get(i).y/MetersPerPixles));
//            }
        }



    }

    public void mousePressed(){
        if(mouseButton == LEFT){
            path_.add(new Waypoint(MetersPerPixles*mouseX,MetersPerPixles*mouseY,0));
        }
    }
    public void keyPressed(){
        switch(key){
            case 'r':
                reset();
                break;
            case 'i':
                path_.InjectPoints();
                path_.SmoothPoints(Constants.weightData,Constants.weightSmooth);
                path_.addVelocities(Constants.VelocityConst);
                break;
            case 'f':
                if(!path_.isEmpty()&& path_.size()>1)
                    follower = new Follower(path_.get(0),path_.get(1));
                break;
            case 's':
                try {
                    path_.SavePath("ttt");
                }catch (IOException e){
                   e.printStackTrace();
                }
                break;
            case 'l':
                try {
                    path_.LoadPath("dolev");
                }catch (IOException e){
                    e.printStackTrace();
                }catch (CsvException e){
                    e.getLineNumber();

                }
                break;

        }
    }


}
