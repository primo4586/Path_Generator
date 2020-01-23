package Utils;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.io.*;
import java.util.*;


public class Path extends ArrayList<Waypoint> {

    private double spacing, tolerance, maxVal;
    private Units unit;

    public Path(int imageWidth, int imageHeight){
        this.spacing = Constants.SPACING;
        this.tolerance = Constants.TOLERENCE;
        this.maxVal = Constants.MAX_VELOCITY;
        this.unit = new Units(imageWidth,imageHeight);
    }

    public Path(Units unit){
        this.spacing = Constants.SPACING;
        this.tolerance = Constants.TOLERENCE;
        this.maxVal = Constants.MAX_VELOCITY;
        this.unit = unit;
    }
    public void SavePath(String pathName) throws IOException{
        String path = "src/main/resources/Paths/"+pathName+".csv";
        CSVWriter CSVwriter = new CSVWriter(new FileWriter(path));
        List<String[]> data = getData();
        CSVwriter.writeAll(data);
        CSVwriter.close();
    }
    public void LoadPath(String pathName) throws IOException, CsvException {
        String path = "src/main/resources/Paths/"+pathName+".csv";
        CSVReader reader = new CSVReader(new FileReader(path));
         List<String[]> data = reader.readAll();
         this.clear();
         Iterator<String[]> it = data.iterator();
         for(int i = 1;i<data.size();i++){
             this.add(new Waypoint(Double.parseDouble(data.get(i)[0]),Double.parseDouble(data.get(i)[1]),Double.parseDouble(data.get(i)[2])));
         }
    }
    private List<String[]> getData(){
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"X pos","Y pos","Velocity"});
        for(int i = 0;i<size();i++){
            data.add(new String[] {""+this.get(i).x,""+this.get(i).y,""+this.get(i).velocity});
        }
        return data;
    }
    public double getCurv(Waypoint p2,Waypoint p1,Waypoint p3){
        p1.x += 0.0001;
        double k1 = 0.5*(p1.getLengthSq()-p2.getLengthSq())/(p1.x-p2.x);
        double k2 = (p1.y-p2.y)/(p1.x-p2.x);

        double b = 0.5*(p2.x*p2.x - 2*p2.x*k1 + p2.y*p2.y - p3.x*p3.x + 2*p3.x*k1-p3.y*p3.y)/(p3.x*k2 - p3.y+p2.y-p2.x*k2);
        double a = k1 - k2*b;
        double rad = Math.sqrt(Math.pow(p1.x-a,2)+Math.pow(p1.y-b,2));
        if(rad > 10000)
            return 0;
        return 1/rad;
    }
    public void addVelocities(){
        double velConst = Constants.VELOCITY_CONST;
        this.get(0).velocity = this.maxVal;
        double curve;
        for(int i = 1; i< this.size()-1;i++){
            curve = getCurv(this.get(i-1),this.get(i),this.get(i+1));
            this.get(i).velocity = Math.min(velConst/curve,maxVal);
        }
        this.get(this.size()-1).velocity = 0;
    }

    public boolean InjectPoints(){
        if(this.isEmpty())
            return false;

        Path newPath = new Path(this.unit);
        double distance = 0;

        for(int i =  1;i< this.size(); i++){
            distance = get(i-1).distance(get(i));

            for(double j=0;j < distance;j += this.spacing){
                newPath.add(new Waypoint(
                         get(i-1).x + j/distance*(get(i).x -get(i-1).x)
                        ,get(i-1).y + j/distance*(get(i).y -get(i-1).y)
                        , 0));
            }
        }
        newPath.add(get(size()-1));
        this.clear();
        this.addAll(newPath);
        return true;
    }
    public boolean SmoothPoints(){
        double weightData = Constants.WEIGHT_DATA;
        double weightSmooth = Constants.WEIGHY_SMOOTH;
        if(this.isEmpty())
            return false;

        Path SmoothedPath = new Path(this.unit);
        SmoothedPath.addAll(this);


        double change = tolerance,aux;

        while(change >= tolerance){
            change = 0;
            for(int i = 1;i< this.size()-1;i++){
                aux =  super.get(i).x;
                SmoothedPath.get(i).x += weightData*(get(i).x - SmoothedPath.get(i).x) +
                        weightSmooth*(SmoothedPath.get(i-1).x + SmoothedPath.get(i+1).x - 2*super.get(i).x);
                change += Math.abs(aux - SmoothedPath.get(i).x);


                aux =  super.get(i).y;
                SmoothedPath.get(i).y += weightData*(super.get(i).y - SmoothedPath.get(i).y) +
                        weightSmooth*(SmoothedPath.get(i-1).y + SmoothedPath.get(i+1).y - 2*super.get(i).y);
                change += Math.abs(aux - SmoothedPath.get(i).y);
            }
        }

        super.clear();
        super.addAll(SmoothedPath);
        return true;
    }

    public void initPath(Waypoint[] waypoints){
        for(Waypoint p : waypoints){
            this.add(p);
        }
    }

    public void initPath(Waypoint[] waypoints, boolean smoothAndInject){
        for(Waypoint p : waypoints){
            this.add(p);
        }
        if(smoothAndInject){
            SmoothPoints();
        }
    }

    public String toString(){
        String str = "";
        for(int i =0; i<this.size();i++){
            str += get(i).x+","+get(i).y+","+get(i).velocity+";\n";
        }
        return str;
    }
    public void toCSVPath() throws IOException{
        String path = "src/main/resources/Paths/PathTostring.csv";
        CSVWriter csvwriter = new CSVWriter(new FileWriter(path));
        List<String[]> data = new ArrayList<>();
        for(int i = 0;i<size();i++){
            data.add(new String[] {"new Waypoint("+get(i).x+","+get(i).y+","+get(i).velocity+"),"});
        }
        csvwriter.writeAll(data);
        csvwriter.close();
    }

    public boolean add2M(Waypoint waypoint) {
        waypoint.multiply(unit.METERS_PER_PIXLE);
        return super.add(waypoint);
    }

    public Waypoint get2PX(int index) {
        Waypoint w = new Waypoint(super.get(index));
        w.multiply(unit.PIXLES_PER_METER);
        return w;
    }

}