import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Renderer3D {
    private final Renderer renderer;
    private final float vpWidth, vpHeight, vpDistance;
    public Renderer3D(float vp_width, float vp_height, float distance, Renderer renderer) {
        this.vpWidth = vp_width;
        this.vpHeight = vp_height;
        this.vpDistance = distance;
        this.renderer = renderer;
    }

    public void RenderMesh(Mesh mesh, Matrix4x4 matrix, Camera camera) {
        List<Vector3> projected = new ArrayList<>();

        // Project Vertex to Screenspace
        for (Vertice vertex : mesh.vertices) {
            Vector3 transVertex = Matrix4x4.Multiply(matrix, vertex.position);
            projected.add(ProjectVertex(transVertex));
        }

        // Fill Triangle from Vertices
        List<Triangle> shownFaces = CullBackFaces(mesh, camera);
        for (Triangle triangle : shownFaces) {
            RenderTriangle(triangle, projected);
        }
    }

    public void RenderTriangle(Triangle triangle, List<Vector3> projected) {
        // Use the vertex indices of the triangle to fetch the projected points
        Vector3 p0 = projected.get(triangle.v0.index);
        Vector3 p1 = projected.get(triangle.v1.index);
        Vector3 p2 = projected.get(triangle.v2.index);

        // Now draw the filled triangle using the 2D projected points
        DrawWireframeTriangle(p0, p1, p2, triangle.color);
    }

    // Project a 3D vertex onto the 2D canvas using perspective projection
    public Vector3 ProjectVertex(Vector3 v) {
        float x = v.x * vpDistance / v.z;
        float y = v.y * vpDistance / v.z;
        return ViewportToCanvas(x, y);
    }

    // Convert viewport coordinates (3D space) to canvas (2D screen) coordinates
    public Vector3 ViewportToCanvas(float x, float y) {
        float Cw = (float)renderer.getWidth();
        float Ch = (float)renderer.getHeight();

        // Map viewport coordinates to canvas coordinates
        int canvasX = (int) (x * (Cw / vpWidth));
        int canvasY = (int) (y * (Ch / vpHeight));
        return new Vector3(canvasX, canvasY, 0);
    }

    public List<Triangle> CullBackFaces(Mesh mesh, Camera camera) {
        List<Triangle> culledFaces = new ArrayList<>();
        for (Triangle T : mesh.triangles) {
            // Step 1: Calculate the surface normal of the triangle (in world space)
            Vector3 normal = Vector3.CalculateSurfaceNormal(T);

            // Step 2: Compute the vector from the camera to the triangle's v0 vertex (in world space)
            Vector3 viewVector = Vector3.Minus(T.v0.position, camera.position);

            // Normalize the view vector (optional, but improves precision)
            viewVector = Vector3.Normalize(viewVector);

            // Step 3: Compute the dot product between the normal and the view vector
            float dotProduct = Vector3.DotProduct(normal, viewVector);

            // Step 4: If the dot product is less than 0, the triangle is back-facing (flip sign if necessary)
            if (dotProduct <= 0.001f) {
                // Keep this triangle (front-facing)
                culledFaces.add(T);
            }
        }

        return culledFaces;
    }

    // Sorts points and fills a triangle
    public void DrawFilledTriangle(Vector3 p0, Vector3 p1, Vector3 p2, Color color) {
        // Step 1: Sort the points by their y-coordinates
        if (p1.y < p0.y) { Vector3 temp = p0; p0 = p1; p1 = temp; }
        if (p2.y < p0.y) { Vector3 temp = p0; p0 = p2; p2 = temp; }
        if (p2.y < p1.y) { Vector3 temp = p1; p1 = p2; p2 = temp; }

        // Step 2: Compute the x-coordinates of the triangle's edges
        List<Float> x01 = Interpolate(p0.y, p0.x, p1.y, p1.x); // Edge P0 -> P1
        List<Float> x12 = Interpolate(p1.y, p1.x, p2.y, p2.x); // Edge P1 -> P2
        List<Float> x02 = Interpolate(p0.y, p0.x, p2.y, p2.x); // Edge P0 -> P2

        // Step 3: Concatenate the short sides to get the full edge from P0 to P2
        x01.remove(x01.size() - 1); // Remove last element to avoid duplication
        List<Float> x012 = new ArrayList<>(x01); // Edge from P0 to P2 via P1
        x012.addAll(x12);

        // Step 4: Determine which side is left and which is right
        List<Float> x_left, x_right;
        int m = x012.size() / 2;
        if (x02.get(m) < x012.get(m)) {
            x_left = x02;
            x_right = x012;
        } else {
            x_left = x012;
            x_right = x02;
        }

        int startY = Math.round(p0.y);
        int endY = Math.round(p2.y);

        // Step 5: Draw the horizontal segments to fill the triangle
        for (int y = startY; y < endY; y++) {
            int xStart = Math.round(x_left.get(y - startY));
            int xEnd = Math.round(x_right.get(y - startY));
            for (int x = xStart; x < xEnd; x++) {
                renderer.putPixel(x, y, color); // Draw pixel
            }
        }
    }

    public void DrawWireframeTriangle(Vector3 v0, Vector3 v1, Vector3 v2, Color color) {
        DrawLine(v0, v1, color);
        DrawLine(v1, v2, color);
        DrawLine(v2, v0, color);
    }

    public void DrawLine(Vector3 p0, Vector3 p1, Color color) {
        // Determine if the line is more horizontal or vertical
        if (Math.abs(p1.x - p0.x) > Math.abs(p1.y - p0.y)) {
            // Line is horizontal-ish
            if (p0.x > p1.x) {
                // Swap points to ensure p0.x < p1.x
                Vector3 temp = p0;
                p0 = p1;
                p1 = temp;
            }

            // Interpolate y-values between p0 and p1
            List<Float> ys = Interpolate(p0.x, p0.y, p1.x, p1.y);

            // Draw the line
            int startX = Math.round(p0.x);
            int endX = Math.round(p1.x);

            for (int x = startX; x < endX; x++) {
                renderer.putPixel(x, ys.get(x - startX).intValue(), color);
            }
        } else {
            // Line is vertical-ish
            if (p0.y > p1.y) {
                // Swap points to ensure p0.y < p1.y
                Vector3 temp = p0;
                p0 = p1;
                p1 = temp;
            }

            // Interpolate x-values between p0 and p1
            List<Float> xs = Interpolate(p0.y, p0.x, p1.y, p1.x);
            int startY = Math.round(p0.y);
            int endY = Math.round(p1.y);

            // Draw the line
            for (int y = startY; y < endY; y++) {
                renderer.putPixel(xs.get(y - startY).intValue(), y, color);
            }
        }
    }

    // Interpolates between i0, d0 and i1, d1 and returns a list of values
    public List<Float> Interpolate(float i0, float d0, float i1, float d1) {
        List<Float> values = new ArrayList<>();

        if (i0 == i1) {
            values.add(d0);  // No need to interpolate if the points are the same
            return values;
        }

        float a = (d1 - d0) / (i1 - i0);  // Compute the slope
        float d = d0;  // Start from the initial value

        for (float i = i0; i <= i1; i++) {  // Interpolate over the range
            values.add(d);
            d += a;  // Increment by the slope
        }

        return values;
    }
}
