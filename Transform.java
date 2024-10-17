public class Transform {
    public Vector3 scale;
    public Vector3 rotation;
    public Vector3 translation;
    public Transform(Vector3 scale, Vector3 rotation, Vector3 position) {
        this.scale = scale;
        this.rotation = rotation;
        this.translation = position;
    }

    public static Vector3 Scale(Vector3 vertex, Vector3 scale) {
        return new Vector3(
                vertex.x * scale.x,
                vertex.y * scale.y,
                vertex.z * scale.z);
    }

    public static Vector3 Rotate(Vector3 vertex, Vector3 rotation) {
        // Step 1: Rotate around the X-axis
        float sinX = (float) Math.sin(rotation.x);
        float cosX = (float) Math.cos(rotation.x);
        float y1 = vertex.y * cosX - vertex.z * sinX;
        float z1 = vertex.y * sinX + vertex.z * cosX;

        // Step 2: Rotate around the Y-axis
        float sinY = (float) Math.sin(rotation.y);
        float cosY = (float) Math.cos(rotation.y);
        float x2 = vertex.x * cosY + z1 * sinY;
        float z2 = z1 * cosY - vertex.x * sinY;

        // Step 3: Rotate around the Z-axis
        float sinZ = (float) Math.sin(rotation.z);
        float cosZ = (float) Math.cos(rotation.z);
        float x3 = x2 * cosZ - y1 * sinZ;
        float y3 = x2 * sinZ + y1 * cosZ;

        // Return the final rotated vector
        return new Vector3(x3, y3, z2);
    }

    public static Vector3 Translate(Vector3 vertex, Vector3 translation) {
        return Vector3.AddVectors(vertex, translation);
    }
}
