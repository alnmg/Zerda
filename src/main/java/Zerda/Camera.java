package Zerda;

import java.awt.*;

public class Camera {
    private double posX = 3, posY = 2, dirX = -1, dirY = 0;
    private double cameraPlaneX = 0, cameraPlaneY = 0.66f;
    float angle;

    public void rotate(double degrees) {
        angle = (float) ((angle + degrees) % 360);

        double cos = Math.cos(Math.toRadians(degrees));
        double sin = Math.sin(Math.toRadians(degrees));

        double oldDirX = dirX;
        dirX = (float) (dirX * cos - dirY * sin);
        dirY = (float) (oldDirX * sin + dirY * cos);
        double oldPlaneX = cameraPlaneX;
        cameraPlaneX = (float) (cameraPlaneX * cos - cameraPlaneY * sin);
        cameraPlaneY = (float) (oldPlaneX * sin + cameraPlaneY * cos);
    }
    public void moveFront(float moveSpeed) {
        if (Map.map[(int)(posX + dirX * moveSpeed)][(int)posY] == 0) {
            posX += dirX * moveSpeed;
        }
        if (Map.map[(int)posX][(int)(posY + dirY * moveSpeed)] == 0) {
            posY += dirY * moveSpeed;
        }
    }

    public void moveBack(float moveSpeed) {
        if (Map.map[(int)(posX - dirX * moveSpeed)][(int)posY] == 0) {
            posX -= dirX * moveSpeed;
        }
        if (Map.map[(int)posX][(int)(posY - dirY * moveSpeed)] == 0) {
            posY -= dirY * moveSpeed;
        }
    }

    public void moveLeft(float moveSpeed) {
        posX -= dirY * moveSpeed;
        posY += dirX * moveSpeed;
    }

    public void moveRight(float moveSpeed) {
        posX += dirY * moveSpeed;
        posY -= dirX * moveSpeed;
    }


    public void RenderView(Graphics g) {
        int wWidth = Window.WindowWidth;
        int wHeight = Window.WindowHeight;

        for (int x = 0; x < wWidth; x++) {
            int mapX = (int) posX;
            int mapY = (int) posY;

            float cameraX = 2 * x / (float) wWidth - 1;
            float rayDirX = (float) (dirX + cameraPlaneX * cameraX);
            float rayDirY = (float) (dirY + cameraPlaneY * cameraX);

            float sideDistX;
            float sideDistY;

            float deltaDistX = Math.abs(1 / rayDirX);
            float deltaDistY = Math.abs(1 / rayDirY);
            float perpWallDist;

            int stepX;
            int stepY;

            boolean hit = false;
            boolean vert = false;

            //

            if (rayDirX < 0) {
                stepX = -1;
                sideDistX = (float) ((posX - mapX) * deltaDistX);
            } else {
                stepX = 1;
                sideDistX = (float) ((mapX + 1 - posX) * deltaDistX);
            }
            if (rayDirY < 0) {
                stepY = -1;
                sideDistY = (float) ((posY - mapY) * deltaDistY);
            } else {
                stepY = 1;
                sideDistY = (float) ((mapY + 1 - posY) * deltaDistY);
            }

            // DDA
            while (!hit) {
                if (sideDistX < sideDistY) {
                    sideDistX += deltaDistX;
                    mapX += stepX;
                    vert = false;
                } else {
                    sideDistY += deltaDistY;
                    mapY += stepY;
                    vert = true;
                }
                if (Map.map[mapX][mapY] > 0) {
                    hit = true;
                }
            }

            //calculate ray distance in camera space
            if (vert) {
                perpWallDist = (sideDistY - deltaDistY);
            } else {
                perpWallDist = (sideDistX - deltaDistX);
            }

            //calculate line heigh
            int lineHeight = (int) (wHeight / perpWallDist);

            int drawStart = -lineHeight / 2 + wHeight / 2;
            if (drawStart < 0) {
                drawStart = 0;
            }
            int drawEnd = lineHeight / 2 + wHeight / 2;
            if (drawEnd >= wHeight) {
                drawEnd = wHeight - 1;
            }

            //drawn
            if(Map.map[mapX][mapY] == 1){
                g.setColor( (vert)? Color.DARK_GRAY : Color.GRAY );
            }
            if(Map.map[mapX][mapY] == 2){
                g.setColor( (vert)? new Color(00, 10,255) : new Color(00, 10,100) );
            }
            if(Map.map[mapX][mapY] == 3){
                g.setColor( (vert)? new Color(00, 200,00) : new Color(00, 100,00) );
            }
            g.drawLine(x, drawStart, x, drawEnd);
        }


    }
}
