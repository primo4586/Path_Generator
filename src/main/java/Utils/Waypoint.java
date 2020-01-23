package Utils;

public class Waypoint extends Vector{
    public double velocity;


    public Waypoint(double x, double y, double v){
        super(x, y);
        this.velocity = v;
    }
    public Waypoint(Waypoint p){
        this(p.x,p.y,p.velocity);
    }
}
