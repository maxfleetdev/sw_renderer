import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class MeshFactory {

    // Generate a Cube's vertices and triangles
    public static Mesh CreateCube(float width, float height, float depth, Color color) {
        List<Vertice> vertices = new ArrayList<>();
        List<Triangle> triangles = new ArrayList<>();

        // Vertices for the cube (front and back faces)
        Vertice v0 = new Vertice(new Vector3(-width / 2, -height / 2, depth / 2), 0);  // front face vertices
        Vertice v1 = new Vertice(new Vector3(-width / 2, height / 2, depth / 2), 1);
        Vertice v2 = new Vertice(new Vector3(width / 2, height / 2, depth / 2), 2);
        Vertice v3 = new Vertice(new Vector3(width / 2, -height / 2, depth / 2), 3);
        Vertice v4 = new Vertice(new Vector3(-width / 2, -height / 2, -depth / 2), 4);  // back face vertices
        Vertice v5 = new Vertice(new Vector3(-width / 2, height / 2, -depth / 2), 5);
        Vertice v6 = new Vertice(new Vector3(width / 2, height / 2, -depth / 2), 6);
        Vertice v7 = new Vertice(new Vector3(width / 2, -height / 2, -depth / 2), 7);

        vertices.add(v0); vertices.add(v1); vertices.add(v2); vertices.add(v3);
        vertices.add(v4); vertices.add(v5); vertices.add(v6); vertices.add(v7);

        // Triangles (12 total, 2 per face)
        triangles.add(new Triangle(v0, v1, v2, color)); triangles.add(new Triangle(v0, v2, v3, color)); // Front face
        triangles.add(new Triangle(v4, v0, v3, color)); triangles.add(new Triangle(v4, v3, v7, color)); // Left face
        triangles.add(new Triangle(v5, v4, v7, color)); triangles.add(new Triangle(v5, v7, v6, color)); // Back face
        triangles.add(new Triangle(v1, v5, v6, color)); triangles.add(new Triangle(v1, v6, v2, color)); // Right face
        triangles.add(new Triangle(v4, v5, v1, color)); triangles.add(new Triangle(v4, v1, v0, color)); // Top face
        triangles.add(new Triangle(v2, v6, v7, color)); triangles.add(new Triangle(v2, v7, v3, color)); // Bottom face

        // Return a new Cube with these vertices and triangles
        return new Mesh(vertices, triangles);
    }

    // Generate a Pyramid's vertices and triangles
    public static Mesh CreatePyramid(float baseWidth, float baseDepth, float height, Color color) {
        List<Vertice> vertices = new ArrayList<>();
        List<Triangle> triangles = new ArrayList<>();

        // Vertices for the pyramid
        Vertice v0 = new Vertice(new Vector3(-baseWidth / 2, 0, baseDepth / 2), 0);  // base front-left
        Vertice v1 = new Vertice(new Vector3(baseWidth / 2, 0, baseDepth / 2), 1);   // base front-right
        Vertice v2 = new Vertice(new Vector3(baseWidth / 2, 0, -baseDepth / 2), 2);  // base back-right
        Vertice v3 = new Vertice(new Vector3(-baseWidth / 2, 0, -baseDepth / 2), 3); // base back-left
        Vertice v4 = new Vertice(new Vector3(0, height, 0), 4);                      // apex (top of pyramid)

        vertices.add(v0);
        vertices.add(v1);
        vertices.add(v2);
        vertices.add(v3);
        vertices.add(v4);

        // Base of the pyramid (2 triangles)
        triangles.add(new Triangle(v0, v1, v2, color)); // Base front-right triangle
        triangles.add(new Triangle(v0, v2, v3, color)); // Base back-left triangle

        // Side faces of the pyramid (4 triangles)
        triangles.add(new Triangle(v0, v1, v4, color)); // Front face
        triangles.add(new Triangle(v1, v2, v4, color)); // Right face
        triangles.add(new Triangle(v2, v3, v4, color)); // Back face
        triangles.add(new Triangle(v3, v0, v4, color)); // Left face

        // Return a new Pyramid with these vertices and triangles
        return new Mesh(vertices, triangles);
    }

    public static Mesh CreateTetrahedron(float size, Color color) {
        List<Vertice> vertices = new ArrayList<>();
        List<Triangle> triangles = new ArrayList<>();

        // Vertices for the tetrahedron
        Vertice v0 = new Vertice(new Vector3(0, size, 0), 0); // top point
        Vertice v1 = new Vertice(new Vector3(-size / 2, 0, size / 2), 1); // base-left
        Vertice v2 = new Vertice(new Vector3(size / 2, 0, size / 2), 2);  // base-right
        Vertice v3 = new Vertice(new Vector3(0, 0, -size / 2), 3);        // base-back

        vertices.add(v0); vertices.add(v1); vertices.add(v2); vertices.add(v3);

        // Triangular faces
        triangles.add(new Triangle(v0, v1, v2, color)); // front face
        triangles.add(new Triangle(v0, v2, v3, color)); // right face
        triangles.add(new Triangle(v0, v3, v1, color)); // left face
        triangles.add(new Triangle(v1, v2, v3, color)); // base face

        return new Mesh(vertices, triangles);
    }

    public static Mesh CreateCylinder(float radius, float height, int segments, Color color) {
        List<Vertice> vertices = new ArrayList<>();
        List<Triangle> triangles = new ArrayList<>();

        // Vertices for top and bottom circles
        for (int i = 0; i < segments; i++) {
            float angle = (float) (2 * Math.PI * i / segments);
            float x = (float) Math.cos(angle) * radius;
            float z = (float) Math.sin(angle) * radius;

            // Top circle vertex
            vertices.add(new Vertice(new Vector3(x, height / 2, z), i));
            // Bottom circle vertex
            vertices.add(new Vertice(new Vector3(x, -height / 2, z), i + segments));
        }

        // Center points for the top and bottom circles
        Vertice topCenter = new Vertice(new Vector3(0, height / 2, 0), 2 * segments);
        Vertice bottomCenter = new Vertice(new Vector3(0, -height / 2, 0), 2 * segments + 1);
        vertices.add(topCenter); vertices.add(bottomCenter);

        // Create top and bottom triangles (fan)
        for (int i = 0; i < segments; i++) {
            int next = (i + 1) % segments;

            // Top face triangles
            triangles.add(new Triangle(topCenter, vertices.get(next), vertices.get(i), color));

            // Bottom face triangles (in reverse winding order to match correct face direction)
            triangles.add(new Triangle(bottomCenter, vertices.get(i + segments), vertices.get(next + segments), color));
        }

        // Create side triangles (connecting the top and bottom vertices)
        for (int i = 0; i < segments; i++) {
            int next = (i + 1) % segments;

            // Create two triangles for each quad
            triangles.add(new Triangle(vertices.get(i), vertices.get(i + segments), vertices.get(next), color));           // First triangle
            triangles.add(new Triangle(vertices.get(next), vertices.get(i + segments), vertices.get(next + segments), color)); // Second triangle
        }

        return new Mesh(vertices, triangles);
    }


    public static Mesh CreateSphere(float radius, int segments, int rings, Color color) {
        List<Vertice> vertices = new ArrayList<>();
        List<Triangle> triangles = new ArrayList<>();

        // Create vertices using latitude and longitude angles
        for (int i = 0; i <= rings; i++) {
            float lat = (float) (Math.PI * i / rings);
            float y = radius * (float) Math.cos(lat);
            float ringRadius = radius * (float) Math.sin(lat);

            for (int j = 0; j <= segments; j++) {
                float lon = (float) (2 * Math.PI * j / segments);
                float x = ringRadius * (float) Math.cos(lon);
                float z = ringRadius * (float) Math.sin(lon);

                vertices.add(new Vertice(new Vector3(x, y, z), i * (segments + 1) + j));
            }
        }

        // Create triangles for the sphere
        for (int i = 0; i < rings; i++) {
            for (int j = 0; j < segments; j++) {
                int first = i * (segments + 1) + j;
                int second = first + segments + 1;

                triangles.add(new Triangle(vertices.get(first), vertices.get(second), vertices.get(first + 1), color));
                triangles.add(new Triangle(vertices.get(second), vertices.get(second + 1), vertices.get(first + 1), color));
            }
        }

        return new Mesh(vertices, triangles);
    }
}