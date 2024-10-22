public class Matrix4x4 {
    public float[][] m;

    // Constructor for a 4x4 matrix initialized with zeros
    public Matrix4x4() {
        m = new float[4][4];
    }

    // Identity matrix (no transformation)
    public static Matrix4x4 Identity() {
        Matrix4x4 matrix = new Matrix4x4();
        matrix.m[0][0] = 1; matrix.m[1][1] = 1;
        matrix.m[2][2] = 1; matrix.m[3][3] = 1;
        return matrix;
    }

    // Translation matrix
    public static Matrix4x4 Translation(float x, float y, float z) {
        Matrix4x4 matrix = Matrix4x4.Identity();
        matrix.m[0][3] = x;
        matrix.m[1][3] = y;
        matrix.m[2][3] = z;
        return matrix;
    }

    // Scaling matrix
    public static Matrix4x4 Scale(float x, float y, float z) {
        Matrix4x4 matrix = Matrix4x4.Identity();
        matrix.m[0][0] = x;
        matrix.m[1][1] = y;
        matrix.m[2][2] = z;
        return matrix;
    }

    // Rotation matrix around the X-axis
    public static Matrix4x4 RotationX(float angle) {
        Matrix4x4 matrix = Matrix4x4.Identity();
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);
        matrix.m[1][1] = cos; matrix.m[1][2] = -sin;
        matrix.m[2][1] = sin; matrix.m[2][2] = cos;
        return matrix;
    }

    // Rotation matrix around the Y-axis
    public static Matrix4x4 RotationY(float angle) {
        Matrix4x4 matrix = Matrix4x4.Identity();
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);
        matrix.m[0][0] = cos; matrix.m[0][2] = sin;
        matrix.m[2][0] = -sin; matrix.m[2][2] = cos;
        return matrix;
    }

    // Rotation matrix around the Z-axis
    public static Matrix4x4 RotationZ(float angle) {
        Matrix4x4 matrix = Matrix4x4.Identity();
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);
        matrix.m[0][0] = cos; matrix.m[0][1] = -sin;
        matrix.m[1][0] = sin; matrix.m[1][1] = cos;
        return matrix;
    }

    // Matrix multiplication (this * other)
    public static Matrix4x4 Multiply(Matrix4x4 a, Matrix4x4 b) {
        Matrix4x4 result = new Matrix4x4();

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                result.m[row][col] = a.m[row][0] * b.m[0][col] +
                        a.m[row][1] * b.m[1][col] +
                        a.m[row][2] * b.m[2][col] +
                        a.m[row][3] * b.m[3][col];
            }
        }

        return result;
    }

    // Multiply a matrix by a vector (used to transform a 3D point)
    public static Vector3 Multiply(Matrix4x4 m, Vector3 v) {
        float x = v.x * m.m[0][0] + v.y * m.m[0][1] + v.z * m.m[0][2] + m.m[0][3];
        float y = v.x * m.m[1][0] + v.y * m.m[1][1] + v.z * m.m[1][2] + m.m[1][3];
        float z = v.x * m.m[2][0] + v.y * m.m[2][1] + v.z * m.m[2][2] + m.m[2][3];

        return new Vector3(x, y, z);
    }

    // Print the matrix (for debugging purposes)
    public void PrintMatrix() {
        for (int i = 0; i < 4; i++) {
            System.out.println(m[i][0] + " " + m[i][1] + " " + m[i][2] + " " + m[i][3]);
        }
    }
}