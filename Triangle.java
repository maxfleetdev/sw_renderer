import java.awt.*;

public class Triangle {
    public Vertice v0, v1, v2;
    public Color color;

    public Triangle(Vertice v0, Vertice v1, Vertice v2, Color color) {
        this.v0 = v0;
        this.v1 = v1;
        this.v2 = v2;
        this.color = color;
    }
}