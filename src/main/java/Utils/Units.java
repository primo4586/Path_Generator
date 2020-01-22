package Utils;

public class Units {
    public static double feetToMeters(double feet){
        return feet/3.281;
    }
    public static double inchToMeters(double Inches){
        return Inches/39.37;
    }
    public static double pixlesPerMeters(double meters,double pixles){
        if(meters >0)
            return pixles/meters;
        return 0;
    }
}
