import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class IBlock extends Block {

    public IBlock(Point generatePoint) {
        super(generatePoint);
        this.points = getPointByState(0);
    }

    @Override
    public Set<Point> getPointByState(int state) {
        int x = benchmark.x;
        int y = benchmark.y;
        Set<Point> points = new HashSet<>();
        if (state == 0 || state == 2) {
            points.add(new Point(x, y));
            points.add(new Point(x, y - BLOCK_SIZE));
            points.add(new Point(x, y - BLOCK_SIZE * 2));
            points.add(new Point(x, y - BLOCK_SIZE * 3));
        } else {
            points.add(new Point(x - BLOCK_SIZE, y));
            points.add(new Point(x, y));
            points.add(new Point(x + BLOCK_SIZE, y));
            points.add(new Point(x + BLOCK_SIZE * 2, y));
        }
        return points;
    }
}
