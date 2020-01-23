package Utils;

public class Units {
    public static double feetToMeters(double feet){
        return feet/3.281;
    }
    public static double inchToMeters(double Inches){
        return Inches/39.37;
    }

    private double imgWidth;
    private double imgHeight;

    public double PIXLES_PER_METER;
    public double METERS_PER_PIXLE;
    public Units(int imgWidth,int imgHeight){
        this.imgHeight = imgHeight;
        this.imgWidth = imgWidth;
        PIXLES_PER_METER = imgWidth/MappingConstants.fieldWidth;
        METERS_PER_PIXLE = 1/PIXLES_PER_METER;
    }
}
