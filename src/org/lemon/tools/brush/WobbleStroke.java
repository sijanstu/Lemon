package org.lemon.tools.brush;

import java.awt.BasicStroke;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.FlatteningPathIterator;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static java.awt.geom.PathIterator.SEG_CLOSE;
import static java.awt.geom.PathIterator.SEG_LINETO;
import static java.awt.geom.PathIterator.SEG_MOVETO;

public class WobbleStroke implements Stroke {
	
    private float detail = 2;
    private float amplitude = 2;
    private static final float FLATNESS = 1;
    private final Random rand;
    private final float basicStrokeWidth;

//    private final long seed;

    public WobbleStroke(float detail, float amplitude, float basicStrokeWidth) {
        this.detail = detail;
        this.amplitude = amplitude;
        this.basicStrokeWidth = basicStrokeWidth;

        //noinspection SharedThreadLocalRandom
        rand = ThreadLocalRandom.current();
//        seed = System.nanoTime();
    }

    @Override
    public Shape createStrokedShape(Shape shape) {
//        rand.setSeed(seed);

        GeneralPath result = new GeneralPath();
        shape = new BasicStroke(basicStrokeWidth).createStrokedShape(shape);
        PathIterator it = new FlatteningPathIterator(shape.getPathIterator(null), FLATNESS);
        float[] points = new float[6];
        float moveX = 0, moveY = 0;
        float lastX = 0, lastY = 0;
        float thisX = 0, thisY = 0;
        int type = 0;
//		boolean first = false;
        float next = 0;

        while (!it.isDone()) {
            type = it.currentSegment(points);
            switch (type) {
                case SEG_MOVETO:
                    moveX = lastX = randomize(points[0]);
                    moveY = lastY = randomize(points[1]);
                    result.moveTo(moveX, moveY);
//				first = true;
                    next = 0;
                    break;

                case SEG_CLOSE:
                    points[0] = moveX;
                    points[1] = moveY;
                    // fall through

                case SEG_LINETO:
                    thisX = randomize(points[0]);
                    thisY = randomize(points[1]);
                    float dx = thisX - lastX;
                    float dy = thisY - lastY;
                    float distance = (float) Math.sqrt(dx * dx + dy * dy);
                    if (distance >= next) {
                        float r = 1.0f / distance;
//					float angle = (float)Math.atan2( dy, dx );
                        while (distance >= next) {
                            float x = lastX + next * dx * r;
                            float y = lastY + next * dy * r;
                            result.lineTo(randomize(x), randomize(y));
                            next += detail;
                        }
                    }
                    next -= distance;
//				first = false;
                    lastX = thisX;
                    lastY = thisY;
                    break;
            }
            it.next();
        }

        return result;
    }

    private float randomize(float x) {
//        return x +(float)Math.random()*amplitude*2-1;

        float delta = 2 * (amplitude * (rand.nextFloat() - 0.5f));
        return x + delta;
    }
}
