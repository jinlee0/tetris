import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class TBlock extends Block {

    public TBlock(Point generatePoint) {
        super(generatePoint);
        this.points = getPointByState(0);
    }

    @Override
    public Set<Point> getPointByState(int state) {
        int x = benchmark.x;
        int y = benchmark.y;
        Set<Point> points = new HashSet<>();
        if (state == 0) {
            points.add(new Point(x - BLOCK_SIZE, y - BLOCK_SIZE));
            points.add(new Point(x, y - BLOCK_SIZE));
            points.add(new Point(x + BLOCK_SIZE, y - BLOCK_SIZE));
            points.add(new Point(x, y));
        } else if (state == 1) {
            points.add(new Point(x - BLOCK_SIZE, y - BLOCK_SIZE));
            points.add(new Point(x - BLOCK_SIZE, y));
            points.add(new Point(x - BLOCK_SIZE, y + BLOCK_SIZE));
            points.add(new Point(x, y));
        } else if (state == 2) {
            points.add(new Point(x, y));
            points.add(new Point(x, y + BLOCK_SIZE));
            points.add(new Point(x - BLOCK_SIZE, y + BLOCK_SIZE));
            points.add(new Point(x + BLOCK_SIZE, y + BLOCK_SIZE));
        } else {
            points.add(new Point(x, y));
            points.add(new Point(x + BLOCK_SIZE, y - BLOCK_SIZE));
            points.add(new Point(x + BLOCK_SIZE, y));
            points.add(new Point(x + BLOCK_SIZE, y + BLOCK_SIZE));
        }
        return points;
    }
}
