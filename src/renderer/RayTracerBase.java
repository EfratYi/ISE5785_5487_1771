package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

/**
 * Abstract class for ray tracing.
 * Responsible for tracing a ray and calculating the color it sees in the scene.
 */
public abstract class RayTracerBase {

    /**
     * The scene to be rendered.
     * This field is protected and final, meaning it can be accessed by subclasses but cannot be reassigned.
     */
    protected final Scene scene;

    /**
     * Constructor for initializing the ray tracer with a given scene.
     *
     * @param scene the scene to render
     */
    public RayTracerBase(Scene scene) {
        this.scene = scene;
    }

    /**
     * Abstract method to trace a ray and calculate the color seen along the ray.
     *
     * @param ray the ray to trace
     * @return the color seen along the ray
     */
    public abstract Color traceRay(Ray ray);
}
