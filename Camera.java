public class Camera {
    public Vector3 position;
    public Vector3 rotation; // Orientation represented by Euler angles

    public Camera(Vector3 position, Vector3 rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    // Method to create the camera matrix
    public Matrix4x4 getCameraMatrix() {
        // Invert the camera's translation and rotation to simulate the camera's view
        Matrix4x4 translationMatrix = Matrix4x4.Translation(-position.x, -position.y, -position.z);
        Matrix4x4 rotationMatrixX = Matrix4x4.RotationX(-rotation.x);
        Matrix4x4 rotationMatrixY = Matrix4x4.RotationY(-rotation.y);
        Matrix4x4 rotationMatrixZ = Matrix4x4.RotationZ(-rotation.z);

        // Combine rotation matrices (first rotate, then translate)
        Matrix4x4 cameraMatrix = Matrix4x4.Multiply(rotationMatrixZ, Matrix4x4.Multiply(rotationMatrixY, rotationMatrixX));
        cameraMatrix = Matrix4x4.Multiply(cameraMatrix, translationMatrix);

        return cameraMatrix;
    }
}