package Utils;

public class Waypoint extends Vector{
    public double velocity;


    public Waypoint(double x, double y, double v){
        super(x, y);
        this.velocity = v;
    }
}
