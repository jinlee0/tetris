import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public class PlayPanel extends JPanel implements Runnable {
    private final Set<Block> placedBlock;
    private final Point generatePoint;
    private Block movingBlock;
    private final Thread thread;
    static private int width = 400;
    static private int height = 600;

    private boolean pause = false;


    public PlayPanel() {
        placedBlock = new HashSet<>();
        setSize(width, height);
        generatePoint = new Point(width / 2, (int) (height * 0.1));
        setFocusable(true);
        movingBlock = Block.random(generatePoint);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                synchronized (placedBlock) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_LEFT -> movingBlock.move("Left", placedBlock);
                        case KeyEvent.VK_RIGHT -> movingBlock.move("Right", placedBlock);
                        case KeyEvent.VK_DOWN -> movingBlock.move("Under", placedBlock);
                        case KeyEvent.VK_R -> movingBlock.rotateInPanel(placedBlock);
                        case KeyEvent.VK_SPACE -> {
                            if (thread.getState() != Thread.State.WAITING) {
                                while (movingBlock.move("Under", placedBlock));
                                thread.interrupt();
                            }
                        }
                    }
                }
                repaint();
            }
        });
        thread = new Thread(this);
        thread.start();
    }

    public void setSize(int width, int height) {
        super.setSize(width, height);
        this.width = width;
        this.height = height;
    }

    public static int mGetWidth() {
        return width;
    }

    public static int mGetHeight() {
        return height;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawRect(0, 0, width, height);
        for (Point point : movingBlock.getPoints()) {
            g.setColor(movingBlock.getColor());
            g.fillRect(point.x, point.y, Block.getBlockSize(), Block.getBlockSize());
        }
        synchronized (placedBlock) {
            drawShadow(g, movingBlock);
            for (Block block : placedBlock) {
                for (Point point : block.getPoints()) {
                    g.setColor(block.getColor());
                    int x = point.x;
                    int y = point.y;
                    g.fillRect(x, y, Block.getBlockSize(), Block.getBlockSize());

                    g.setColor(block.getBorderColor());
                    g.drawRect(x, y, Block.getBlockSize(), Block.getBlockSize());
                }
            }
        }
    }

    @Override
    public void run() {

        while (!movingBlock.isClashedWithOthers(movingBlock.getPoints(), placedBlock)) {
            try {
                synchronized (this) {
                    while (pause) {
                        wait();
                    }
                }
                if (movingBlock.move("Under", placedBlock)) {
                    repaint();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                    }
                } else {
                    synchronized (placedBlock) {
                        placedBlock.add(movingBlock);
                    }
                    deleteBlocksOfFullLine();
                    movingBlock = Block.random(generatePoint);
                    repaint();
                }
            } catch (InterruptedException e) {
            }
        }
        System.out.println("finish");
    }

    public synchronized void resume() {
        pause = false;
        notify();
    }

    public void pause() {
        pause = true;
    }

    public boolean isPause() {
        return pause;
    }

    public void deleteBlocksOfFullLine(){
        Set<Point> allPoint = new HashSet<>();
        synchronized (placedBlock) {
            for(Block block : placedBlock){
                allPoint.addAll(block.getPoints());
            }
            for(int y = 0; y < height; y+=Block.getBlockSize()){
                boolean isFull = true;
                for (int x = 0; x < width; x+=Block.getBlockSize()) {
                    if (!allPoint.contains(new Point(x, y))) {
                        isFull = false;
                        break;
                    }
                }
                if(isFull){
                    deleteLine(y);
                    lineAlign(y);
                }
            }
        }
    }

    public void deleteLine(int y) {
        for (Block block : placedBlock) {
            block.getPoints().removeIf(point -> point.y == y);
        }
        placedBlock.removeIf(block -> block.getPoints().isEmpty());
    }

    public void lineAlign(int y){
        for (Block block : placedBlock) {
            block.getPoints().forEach(point -> {
                if (point.y <= y - Block.getBlockSize()) {
                    point.y += Block.getBlockSize();
                }
            });
        }
    }

    public void drawShadow(Graphics g, Block movingBlock){
        Set<Integer> gap = new HashSet<>();

        movingBlock.getPoints().forEach(point -> gap.add(height - point.y));
        placedBlock.forEach(block -> {
            block.getPoints().forEach(point -> {
                movingBlock.getPoints().forEach(movingPoint -> {
                    if (point.x == movingPoint.x && point.y > movingPoint.y) {
                        gap.add(point.y - movingPoint.y);
                    }
                });
            });
        });

        int minGap = gap.stream().min(Integer::compareTo).get() - Block.getBlockSize();
        movingBlock.getPoints().forEach(point->
        {
            int red = movingBlock.getColor().getRed();
            int green = movingBlock.getColor().getGreen();
            int blue = movingBlock.getColor().getBlue();
            g.setColor(new Color(red, green, blue, 100));
            g.fillRect(point.x, point.y + minGap, Block.getBlockSize(), Block.getBlockSize());
        });

    }
}
