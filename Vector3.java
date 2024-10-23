public class Vector3 {
    public float x, y, z;
    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Vector3 Add(Vector3 a, Vector3 b) {
        return new Vector3(
                a.x + b.x,
                a.y + b.y,
                a.z + b.z
        );
    }

    public static Vector3 Minus(Vector3 a, Vector3 b) {
        return new Vector3(
                a.x - b.x,
                a.y - b.y,
                a.z - b.z
        );
    }

    public static Vector3 Zero() {
        return new Vector3(0,0,0);
    }

    public static Vector3 Multiply(Vector3 a, Vector3 b) {
        return new Vector3(
                a.x * b.x,
                a.y * b.y,
                a.z * b.z
        );
    }

    public static Vector3 CalculateSurfaceNormal(Triangle triangle) {
        Vector3 U, V;
        U = Vector3.Minus(triangle.v1.position, triangle.v0.position);
        V = Vector3.Minus(triangle.v2.position, triangle.v1.position);

        Vector3 normal = Vector3.Zero();
        normal.x = (U.y * V.z) - (U.z * V.y);
        normal.y = (U.z * V.x) - (U.x * V.z);
        normal.z = (U.x * V.y) - (U.y * V.x);
        return normal;
    }

    // Dot product of two vectors
    public static float DotProduct(Vector3 a, Vector3 b) {
        return a.x * b.x + a.y * b.y + a.z * b.z;
    }

    // Normalize a vector to a unit vector
    public static Vector3 Normalize(Vector3 v) {
        float length = (float)Math.sqrt(v.x * v.x + v.y * v.y + v.z * v.z);
        return new Vector3(v.x / length, v.y / length, v.z / length);
    }
}