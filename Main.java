import java.awt.*;

public class Main {
    // Screen
    public static final int SCREEN_WIDTH = 1920/2, SCREEN_HEIGHT = 1080/2;
    public static final Renderer renderer = new Renderer(SCREEN_WIDTH, SCREEN_HEIGHT);

    // Viewport
    public static float aspectRatio = (float)SCREEN_WIDTH / (float)SCREEN_HEIGHT;
    public static float d = 1.0f;
    public static float Vw = 1.0f;
    public static float Vh = Vw / aspectRatio;

    // 3D Renderer
    public static final Renderer3D renderer3D = new Renderer3D(Vw, Vh, d, renderer);

    public static void main(String[] args) {
        // Make Meshes
        Mesh cube = MeshFactory.CreateCube(1,1, 1, Color.blue);
        Mesh pyramid = MeshFactory.CreatePyramid(1,1,1, Color.red);
        Mesh sphere = MeshFactory.CreateSphere(0.75f,8, 8, Color.green);

        // Set Positions
        cube.SetPosition(new Vector3(-2,0,7));
        pyramid.SetPosition(new Vector3(0,0,7));
        sphere.SetPosition(new Vector3(2,0,7));

        // Create Scene
        Scene mainScene = new Scene();
        mainScene.AddMesh(cube);
        mainScene.AddMesh(pyramid);
        mainScene.AddMesh(sphere);

        // For Movement
        float speed = 0.01f;

        while (true) {
            // Clear Pixels
            swapBuffer();

            // Manipulate Meshes
            cube.transform.rotation.x += speed;
            cube.transform.rotation.y += speed;
            pyramid.transform.rotation.x += speed;
            pyramid.transform.rotation.y += speed;
            sphere.transform.rotation.x += speed;
            sphere.transform.rotation.y += speed;

            // Make Pixels
            drawScene(mainScene);

            // Show Pixels
            render();

            // For speed normalisation (60fps)
            try {
                Thread.sleep(6);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void drawScene(Scene scene) {
        Mesh[] m = scene.meshes.toArray(new Mesh[0]);
        for (Mesh mesh : m) {
            renderer3D.RenderMesh(mesh);
        }
    }

    public static void render() {
        renderer.render();
    }

    public static void swapBuffer() {
        renderer.clearScreen(Color.white);
    }
}