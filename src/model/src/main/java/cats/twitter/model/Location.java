package cats.twitter.model;

import javax.persistence.*;

/**
 * Created by Nathanael on 01/02/2016.
 */
@Entity
public class Location {
    @Id
    @SequenceGenerator(name="location_seq",
            sequenceName="location_seq",
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="location_seq")
    private int id;
    private double neLat;
    private double neLng;
    private double swLat;
    private double swLng;

    public Location() {
    }

    public Location(double neLat, double neLng, double swLat, double swLng) {
        this.neLat = neLat;
        this.neLng = neLng;
        this.swLat = swLat;
        this.swLng = swLng;
    }

    public double getSwLng() {
        return swLng;
    }

    public double getSwLat() {
        return swLat;
    }

    public double getNeLng() {
        return neLng;
    }

    public double getNeLat() {
        return neLat;
    }
}
