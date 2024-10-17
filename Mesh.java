import java.util.List;

public class Mesh {
    public List<Vertice> vertices;
    public List<Triangle> triangles;
    public Transform transform;

    public Mesh(List<Vertice> vertices, List<Triangle> triangles) {
        this.vertices = vertices;
        this.triangles = triangles;
        this.transform = new Transform(new Vector3(1,1,1), Vector3.Zero(), Vector3.Zero());  // Default scale is 1
    }

    public void SetPosition(Vector3 position) {
        transform.translation = position;
    }

    public void SetRotation(Vector3 rotation) {
        transform.rotation = rotation;
    }

    public void SetScale(Vector3 scale) {
        transform.scale = scale;
    }
}