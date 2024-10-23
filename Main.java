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

    // Camera
    public static final Camera camera = new Camera(Vector3.Zero(), Vector3.Zero());
    public static final float moveSpeed = 0.04f, rotateSpeed = 0.005f;

    public static void main(String[] args) {
        // Make Meshes
        Mesh cube = MeshFactory.CreateCube(1,1, 1, Color.blue);
        Mesh pyramid = MeshFactory.CreatePyramid(1,1,1, Color.red);
        Mesh sphere = MeshFactory.CreateSphere(0.75f,8, 8, Color.green);

        // Set Positions
        cube.SetPosition(new Vector3(-2,0,7));
        pyramid.SetPosition(new Vector3(0,0,10));
        sphere.SetPosition(new Vector3(2,0,7));

        // Create Scene
        Scene mainScene = new Scene();
        mainScene.AddMesh(cube);
        mainScene.AddMesh(pyramid);
        mainScene.AddMesh(sphere);

        while (true) {
            // Clear Pixels
            swapBuffer();

            // Move Camera
            handleInput();

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

    public static void handleInput() {
        InputHandler inputHandler = renderer.inputHandler;

        if (inputHandler.isKeyPressed()) {
            char key = inputHandler.getCurrentKeyPressed();
            switch (key) {
                // Translate Camera
                case 'w':
                    camera.position.z += moveSpeed;
                    break;
                case 's':
                    camera.position.z -= moveSpeed;
                    break;
                case 'q':
                    camera.position.y += moveSpeed;
                    break;
                case 'e':
                    camera.position.y -= moveSpeed;
                    break;

                // Rotate Camera
                case 'a':
                    camera.rotation.y -= rotateSpeed;
                    break;
                case 'd':
                    camera.rotation.y += rotateSpeed;
                    break;
                default:
                    break;
            }
        }
    }

    public static void drawScene(Scene scene) {
        Matrix4x4 m_Camera = camera.getCameraMatrix();
        Mesh[] m = scene.meshes.toArray(new Mesh[0]);

        for (Mesh mesh : m) {
            Matrix4x4 m_Mesh = Matrix4x4.Multiply(m_Camera, mesh.transform.getTransformMatrix());
            renderer3D.RenderMesh(mesh, m_Mesh, camera);
        }
    }

    public static void render() {
        renderer.render();
    }

    public static void swapBuffer() {
        renderer.clearScreen(Color.black);
    }
}