package geometries;

import primitives.Double3;

public class Material {
    public Double3 kA=Double3.ONE;

    public Material setKA(Double3 kA) {
        this.kA = kA;
        return this;
    }
    public Material setKA(double kA) {
        this.kA = new Double3(kA);
        return this;
    }

}
