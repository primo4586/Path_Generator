import Utils.*;
import com.opencsv.exceptions.CsvException;
import com.sun.tools.jdeprscan.CSVParseException;
import org.w3c.dom.Text;
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
    public static void main(String[] args) {
        PApplet.main("Main");
    }

    public String PathName = "dolev";
    String imagePath = "Images/frc_2020_arena.png";

    public int ImageWidth,ImageHeight;
    Path path_;
    PImage backGround;
    public void settings(){
        try {
            BufferedImage image = ImageIO.read(getClass().getResource(imagePath));
            ImageWidth = image.getWidth();
            ImageHeight = image.getHeight();
            size(ImageWidth,ImageHeight);
            backGround = loadImage(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void setup(){
        reset();
    }
    public void reset(){
        path_ = new Path(ImageWidth,ImageHeight);
    }
    public void draw(){
        background(backGround);
        fill(0);
        if(!path_.isEmpty()) {
            stroke(255, 51, 0);
            for (int i = 0; i < path_.size() - 1; i++) {
                Waypoint p = path_.get2PX(i),p2 = path_.get2PX(i+1);
                line((float)(p.x),(float)(p.y), (float)(p2.x),(float)(p2.y));
            }
            fill(255, 255, 0);
            for(int i = 0; i < path_.size(); i++){
                Waypoint p = path_.get2PX(i);
                text("[]",(float)p.x,(float)p.y);
            }
        }
    }

    public void mousePressed(){
        if(mouseButton == LEFT){
            path_.add2M(new Waypoint(mouseX,mouseY,0));
        }
    }
    public void keyPressed(){
        switch(key){
            case 'r':
                reset();
                break;
            case 'i':
                path_.InjectPoints();
                break;
            case 't':
                path_.SmoothPoints();
                path_.addVelocities();

            case 's':
                try {
                    path_.SavePath(PathName);
                }catch (IOException e){
                   e.printStackTrace();
                }
                break;
            case 'l':
                try {
                    path_.LoadPath(PathName);
                }catch (IOException e){
                    e.printStackTrace();
                }catch (CsvException e){
                    e.getLineNumber();

                }
                break;
        }
    }
}
