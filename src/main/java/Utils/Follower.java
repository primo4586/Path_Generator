package Utils;

public class Follower extends Odometry{
    private double angle;
    private double width,height;

    private double frameangle;
    public Follower(Vector pos,Vector NextPoint){
        super(pos);
        this.angle = Vector.getProjectedVector(pos,NextPoint).getAngle();

        this.width = Constants.width;
        this.height = Constants.height;

        this.frameangle = Math.atan2(this.width,this.height);
    }

    public Vector getrobotPoints(){
        double drawAngle = 0;
        Vector robot = Vector.getProjectedVector(this,Vector.toCartesian(100,angle));

        return robot;
    }
}
