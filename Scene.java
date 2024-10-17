import java.util.ArrayList;
import java.util.List;

public class Scene {
    public List<Mesh> meshes = new ArrayList<>();

    public void AddMesh(Mesh mesh) {
        meshes.add(mesh);
    }

    public void RemoveMesh(Mesh mesh) {
        meshes.remove(mesh);
    }
}
