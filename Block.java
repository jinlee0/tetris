import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class Block {
    protected Set<Point> points;
    protected Color color;
    protected Color borderColor;
    protected Point benchmark;
    static protected int BLOCK_SIZE = 20;
    int state;

    public Block(Point generatePoint) {
        this.color = new Color(
                (int) (Math.random() * 200),
                (int) (Math.random() * 200),
                (int) (Math.random() * 200)
        );

        this.borderColor = new Color(
                color.getRed() + 50,
                color.getGreen() + 50,
                color.getBlue() + 50
        );

        state = 0;
        this.benchmark = new Point(generatePoint);
        points = new HashSet<>();
    }

    public static void setBlockSize(int blockSize) {
        BLOCK_SIZE = blockSize;
    }

    static public int getBlockSize() {
        return BLOCK_SIZE;
    }

    public Set<Point> getPoints() {
        return points;
    }

    public Color getColor() {
        return color;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void moveLeft() {
        for (Point point : points) {
            if (point.getX() - BLOCK_SIZE < 0) {
                return;
            }
        }
        for (Point point : points) {
            point.x -= BLOCK_SIZE;
        }
        this.benchmark.x -= BLOCK_SIZE;
    }

    public void moveRight() {
        for (Point point : points) {
            if (point.x + BLOCK_SIZE * 2 > PlayPanel.mGetWidth()) {
                return;
            }
        }
        for (Point point : points) {
            point.x += BLOCK_SIZE;
        }
        this.benchmark.x += BLOCK_SIZE;
    }

    public void moveDown() {
        for (Point point : points) {
            if (point.y + BLOCK_SIZE * 2 > PlayPanel.mGetHeight()) {
                return;
            }
        }
        for (Point point : points) {
            point.y += BLOCK_SIZE;
        }
        this.benchmark.y += BLOCK_SIZE;
    }

    public boolean move(String where, Set<Block> others) {
        for (Block otherBlock : others) {
            if (otherBlock.isAt(where, this)) {
                return false;
            }
        }
        switch (where) {
            case "Left" -> moveLeft();
            case "Right" -> moveRight();
            case "Under" -> moveDown();
        }
        return !isPlaced();
    }


    public boolean isAt(String where, Block other) {
        switch (where) {
            case "Left":
                for (Point point : points) {
                    for (Point otherPoint : other.getPoints()) {
                        if (new Point(point.x + BLOCK_SIZE, point.y).equals(otherPoint)) {
                            return true;
                        }
                    }
                }
                break;
            case "Right":
                for (Point point : points) {
                    for (Point otherPoint : other.getPoints()) {
                        if (new Point(point.x - BLOCK_SIZE, point.y).equals(otherPoint)) {
                            return true;
                        }
                    }
                }
                break;
            case "Under":
                for (Point point : points) {
                    for (Point otherPoint : other.getPoints()) {
                        if (new Point(point.x, point.y - BLOCK_SIZE).equals(otherPoint)) {
                            return true;
                        }
                    }
                }
                break;
        }
        return false;
    }

    public boolean isPlaced() {
        for (Point point : points) {
            if (point.y + BLOCK_SIZE * 2 > PlayPanel.mGetHeight()) {
                return true;
            }
        }
        return false;
    }

    public boolean isClashedWithBorder(Set<Point> points) {
        for (Point point : points) {
            if (point.x >= PlayPanel.mGetWidth() || point.x < 0) {
                return true;
            }
        }
        return false;
    }

    public boolean isClashedWithOthers(Set<Point> points, Set<Block> otherBlocks) {
        for (Point point : points) {
            for (Block other : otherBlocks) {
                for (Point otherPoint : other.getPoints()) {
                    if (point.equals(otherPoint))
                        return true;
                }
            }
        }
        return false;
    }


    public Set<Point> getPointByState(int state) {
        return new HashSet<>();
    }

    public void rotate() {
        Set<Point> rotated = getPointByState((state + 1) % 4);
        if (!isClashedWithBorder(rotated)) {
            this.points = rotated;
            state = (++state) % 4;
        }
    }

    public void rotateInPanel(Set<Block> others) {
        if (!isClashedWithOthers(getPointByState(state + 1), others)) {
            rotate();
        }
    }

    static public Block random(Point generatePoint) {
        int i = (int) (Math.random() * 2);

        if (i == 0) {
            return new TBlock(generatePoint);
        } else if (i == 1) {
            return new IBlock(generatePoint);
        }
        return null;
    }
}
