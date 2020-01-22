package Utils;

public class PurePursuit{
    private int lastClosestPoint;
    private double lastLookaheadindex;
    private Vector lastLookaheadPoint;

    public PurePursuit(Waypoint[] path){
        this.lastClosestPoint = 0;
        this.lastLookaheadindex = 0;
        this.lastLookaheadPoint = path[0];
    }


    public Waypoint getClosestPoint(Waypoint[] path, Vector robotPos){
        int closest = lastClosestPoint + 1;
        for(int i = lastClosestPoint; i < path.length; i++){
            if(robotPos.distance(path[i])< robotPos.distance(path[closest])) closest = i;
        }

        this.lastClosestPoint = closest;
        return path[closest];
    }
    public static Vector getLookaheadPoint(Vector pos, double lookDistance,Path path) {
        Vector lookahead = null;
        double length;
        // iterate through all pairs of points
        for (int i = 0; i < path.size() - 1; i++) {
            // form a segment from each two adjacent points
            Vector E = path.get(i);
            Vector L = path.get(i + 1);

            Vector d = Vector.subtract(L, E);
            Vector f = Vector.subtract(E, pos);
            length = d.getLength();
            double a = d.dot(d);
            double b = 2 * f.dot(d);
            double c = f.dot(f) - lookDistance * lookDistance;

            double discriminant = b * b - 4 * a * c;

            if (discriminant >= 0) {

                discriminant = Math.sqrt(discriminant);
                double t1 = (-b - discriminant) / (2 * a);
                double t2 = (-b + discriminant) / (2 * a);
                lookahead = new Vector(E);
                d.normalize();
                if (t1 >= 0 && t1 <= 1) {
                    d.multiply(t1 * length);
                    lookahead.add(d);
                }
                d.normalize();
                lookahead = new Vector(E);
                if (t2 >= 0 && t2 <= 1){
                    d.multiply(t2 * length);
                    lookahead.add(d);
                }
            }
        }
        return lookahead;
    }
    public double getSignesCurvature(Vector lookaheadpoint, double robotAngle, Vector robotPos){
        return Math.signum(Math.sin(robotAngle)*(lookaheadpoint.x - robotPos.x) - Math.cos(robotAngle)*(lookaheadpoint.y - robotPos.y));
    }

    public double getRightTargetVelocity(Waypoint closestWaypoint, double curvature, double trackWidth){
        return closestWaypoint.velocity* (2 + curvature * trackWidth)/2;
    }
    
    public double getLeftTargetVelocity(Waypoint closestWaypoint, double curvature, double trackWidth){
        return closestWaypoint.velocity* (2 - curvature * trackWidth)/2;
    }

}
