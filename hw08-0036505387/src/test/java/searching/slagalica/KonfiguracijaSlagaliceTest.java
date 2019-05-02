package searching.slagalica;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KonfiguracijaSlagaliceTest {

    private final KonfiguracijaSlagalice PDFConfiguration = new KonfiguracijaSlagalice(new int[] {
            1,6,4,
            5,0,2,
            8,7,3
    });

    private final KonfiguracijaSlagalice movedLeft = new KonfiguracijaSlagalice(new int[] {
            1,6,4,
            0,5,2,
            8,7,3
    });

    private final KonfiguracijaSlagalice movedRight = new KonfiguracijaSlagalice(new int[] {
            1,6,4,
            5,2,0,
            8,7,3
    });

    private final KonfiguracijaSlagalice movedUp = new KonfiguracijaSlagalice(new int[] {
            1,0,4,
            5,6,2,
            8,7,3
    });

    private final KonfiguracijaSlagalice movedDown = new KonfiguracijaSlagalice(new int[] {
            1,6,4,
            5,7,2,
            8,0,3
    });

    @Test
    void moveLeft() {
        assertEquals(movedLeft, PDFConfiguration.moveLeft());
    }

    @Test
    void moveRight() {
        assertEquals(movedRight, PDFConfiguration.moveRight());
    }

    @Test
    void moveUp() {
        assertEquals(movedUp, PDFConfiguration.moveUp());
    }

    @Test
    void moveDown() {
        assertEquals(movedDown, PDFConfiguration.moveDown());
    }
}