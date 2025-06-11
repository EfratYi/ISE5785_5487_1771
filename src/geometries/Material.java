package geometries;

import primitives.Double3;

/**
 * Represents the material properties of a geometry,
 * including ambient, diffuse, and specular reflection coefficients,
 * as well as shininess factor for specular highlights.
 */
public class Material {


    public Double3 kT = Double3.ZERO;


    public Double3 kR = Double3.ZERO;
    /**
     * Ambient reflection coefficient (kA), default is (1,1,1).
     */
    public Double3 kA = Double3.ONE;

    /**
     * Diffuse reflection coefficient (kD), default is (0,0,0).
     */
    public Double3 kD = Double3.ZERO;

    /**
     * Specular reflection coefficient (kS), default is (0,0,0).
     */
    public Double3 kS = Double3.ZERO;

    /**
     * Shininess factor for specular highlights.
     */
    public int sh = 0;

    /**
     * Sets the ambient reflection coefficient.
     *
     * @param kA the ambient reflection coefficient as a Double3
     * @return this material instance for chaining
     */
    public Material setKA(Double3 kA) {
        this.kA = kA;
        return this;
    }
    public Material setkT(Double3 kT) {
        this.kT = kT;
        return this;
    }
    public Material setkT(double kT) {
        this.kT = new Double3(kT);
        return this;
    }
    public Material setkR(Double3 kR) {
        this.kR = kR;
        return this;
    }
    public Material setkR(double kR) {
        this.kR = new Double3(kR);
        return this;
    }
    /**
     * Sets the ambient reflection coefficient with a single double value for all components.
     *
     * @param kA the ambient reflection coefficient as a double
     * @return this material instance for chaining
     */
    public Material setKA(double kA) {
        this.kA = new Double3(kA);
        return this;
    }

    /**
     * Sets the diffuse reflection coefficient.
     *
     * @param kD the diffuse reflection coefficient as a Double3
     * @return this material instance for chaining
     */
    public Material setKD(Double3 kD) {
        this.kD = kD;
        return this;
    }

    /**
     * Sets the diffuse reflection coefficient with a single double value for all components.
     *
     * @param kD the diffuse reflection coefficient as a double
     * @return this material instance for chaining
     */
    public Material setKD(double kD) {
        this.kD = new Double3(kD);
        return this;
    }

    /**
     * Sets the specular reflection coefficient.
     *
     * @param kS the specular reflection coefficient as a Double3
     * @return this material instance for chaining
     */
    public Material setKS(Double3 kS) {
        this.kS = kS;
        return this;
    }

    /**
     * Sets the specular reflection coefficient with a single double value for all components.
     *
     * @param kS the specular reflection coefficient as a double
     * @return this material instance for chaining
     */
    public Material setKS(double kS) {
        this.kS = new Double3(kS);
        return this;
    }

    /**
     * Sets the shininess factor for specular highlights.
     *
     * @param sh the shininess exponent
     * @return this material instance for chaining
     */
    public Material setShininess(int sh) {
        this.sh = sh;
        return this;
    }
}
