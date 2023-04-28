package Zerda;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

public class Window implements Runnable, KeyListener {
    public static int WindowHeight = 600, WindowWidth = 800;
    private JFrame frame;
    private Canvas canvas;
    private BufferStrategy bufferStrategy;
    private Graphics g;
    private boolean running;
    private final int FPS = 60;
    private final long OPTIMAL_TIME = 1000000000 / FPS;
    private long lastUpdateTime = System.nanoTime();
    private Thread thread;

    public Window() {
        frame = new JFrame("bah");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(WindowWidth, WindowHeight));
        frame.setMinimumSize(new Dimension(480,360));

        frame.add(canvas);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        canvas.setFocusable(true);
        canvas.addKeyListener(this);
        canvas.requestFocus();
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                WindowWidth = frame.getWidth();
                WindowHeight = frame.getHeight();
                canvas.setPreferredSize(new Dimension(WindowWidth, WindowHeight));
            }
        });

        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();

        thread = new Thread(this);

        running = true;
        thread.start();


    }

    @Override
    public void run() {
        System.out.println("running");

        while (running) {
            long now = System.nanoTime();
            long updateTime = now - lastUpdateTime;
            lastUpdateTime = now;

            while (updateTime > 0) {
                int deltaTime = (int) Math.min(updateTime, OPTIMAL_TIME);
                update(deltaTime);
                render();
                updateTime -= deltaTime;
            }

            long sleepTime = (lastUpdateTime - System.nanoTime() + OPTIMAL_TIME) / 1000000;
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    Camera camera = new Camera();

    private boolean arrowUpPressed;
    private boolean arrowDownPressed;
    private boolean arrowLeftPressed;
    private boolean arrowRightPressed;
    private boolean aPressed;
    private boolean dPressed;

    private float movespeed = 0.06f;
    private float rotspeed = 0.9f;
    private void update(int deltaTime) {
        float normalizedMoveSpeed = (movespeed );
        float normalizedRotSpeed = (rotspeed );
        //System.out.println(normalizedMoveSpeed);
        //System.out.println(deltaTime);

        if(arrowUpPressed){
            camera.moveFront(normalizedMoveSpeed);
        }
        if(arrowDownPressed){
            camera.moveBack(normalizedMoveSpeed);
        }
        if(arrowLeftPressed){
            camera.moveLeft(normalizedMoveSpeed);
        }
        if(arrowRightPressed){
            camera.moveRight(normalizedMoveSpeed);
        }
        if(aPressed){
            camera.rotate(normalizedRotSpeed);
        }
        if(dPressed){
            camera.rotate(-normalizedRotSpeed);
        }
    }

    private void render() {
        g = bufferStrategy.getDrawGraphics();
        g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        camera.RenderView(g);

        bufferStrategy.show();
        g.dispose();
    }

    public void setTitle(String title) {
        frame.setTitle(title);
    }

    public void setSize(int width, int height) {
        frame.setSize(width, height);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                arrowUpPressed = true;
                break;
            case KeyEvent.VK_DOWN:
                arrowDownPressed = true;
                break;
            case KeyEvent.VK_LEFT:
                arrowLeftPressed = true;
                break;
            case KeyEvent.VK_RIGHT:
                arrowRightPressed = true;
                break;
            case KeyEvent.VK_A:
                aPressed = true;
                break;
            case KeyEvent.VK_D:
                dPressed = true;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                arrowUpPressed = false;
                break;
            case KeyEvent.VK_DOWN:
                arrowDownPressed = false;
                break;
            case KeyEvent.VK_LEFT:
                arrowLeftPressed = false;
                break;
            case KeyEvent.VK_RIGHT:
                arrowRightPressed = false;
                break;
            case KeyEvent.VK_A:
                aPressed = false;
                break;
            case KeyEvent.VK_D:
                dPressed = false;
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Handle key typed event
    }

    public static void main(String[] args) {
        Window window = new Window();
    }
}
