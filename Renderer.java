import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Renderer extends Canvas {
    private final JFrame frame;
    private final BufferStrategy bufferStrategy;
    private final BufferedImage pixelBuffer;

    // Screen dimensions
    private final int width;
    private final int height;

    public Renderer(int width, int height) {
        this.width = width;
        this.height = height;

        // Create the JFrame window
        frame = new JFrame("Software Renderer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the size of the canvas and window
        this.setPreferredSize(new Dimension(width, height));
        frame.add(this);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);

        // Create a BufferedImage for pixel manipulation
        pixelBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Request the Canvas to create a BufferStrategy
        this.createBufferStrategy(3);
        bufferStrategy = this.getBufferStrategy();
    }

    public void putPixel(int x, int y, Color color) {
        // Convert from 0,0 being top right into 0,0 being center of screen
        int sx = (pixelBuffer.getWidth() / 2) + x;
        int sy = (pixelBuffer.getHeight() / 2) - y;

        // Make sure pixels are within limits
        if (sx >= 0 && sx < pixelBuffer.getWidth() &&
                sy >= 0 && sy < pixelBuffer.getHeight()) {
            pixelBuffer.setRGB(sx, sy, color.getRGB());
        }
    }

    public void clearScreen(Color color) {
        Graphics2D g = pixelBuffer.createGraphics();
        g.setColor(color);
        g.fillRect(0, 0, width, height);
        g.dispose();
    }

    // Main rendering loop where the buffer strategy is used
    public void render() {
        do {
            // Obtain the graphics context from the BufferStrategy
            Graphics g = bufferStrategy.getDrawGraphics();

            // Draw the pixel buffer (our image) to the screen
            g.drawImage(pixelBuffer, 0, 0, null);

            // Dispose of the graphics object and show the buffer
            g.dispose();
            bufferStrategy.show();
        } while (bufferStrategy.contentsLost());
    }
}