// Main.java
// Test code WITHOUT using ArrayList / collections.
// Uses only: arrays, primitives, Node<T>, Queue<T> operations.

public class Main {

    // סעיף א:
    // receives flights array + a NON-permanent passenger.
    // Find the shortest flight such that after taking it, passenger becomes permanent (>=20000).
    // If there is room -> add to passengers list (any position).
    // Else -> add to waiting queue.
    // Must update passenger km and permPass accordingly.
    public static void assignFlight(Flight[] flights, Passenger p) {
        // TODO (students)
    }
    
    // סעיף ב:
    // receives flights array, an index in the array, and a passenger who cancels.
    // Assume passenger exists in passengers list.
    // Steps:
    // 1) remove from passengers list
    // 2) update the passenger km accumulation
    // 3) fill the freed seat with:
    //    - a permanent passenger from waiting with MAX km, if any permanent exist
    //    - otherwise the first waiting passenger
    public static void cancelAndReplace(Flight[] flights, int i, Passenger canceling) {
        // TODO (students)
    }


    // סעיף ג:
    /*
    סיבוכיות
    */
    public static void main(String[] args) {
        // -----------------------------
        // Build flights
        // -----------------------------
        Flight[] flights = new Flight[3];
        flights[0] = new Flight("F100", 2, 3000);    // short
        flights[1] = new Flight("F200", 1, 7000);    // medium
        flights[2] = new Flight("F300", 1, 12000);   // long

        // -----------------------------
        // TEST 1: assignFlight chooses shortest that makes passenger permanent.
        // Seat available -> passenger goes to list.
        // -----------------------------
        Passenger p1 = new Passenger(111, "Noam", 18000); // needs 2000 -> F100 (3000)
        Ex1.assignFlight(flights, p1);

        assertTrue(p1.getKm() == 21000, "T1: p1 km should increase by 3000 (to 21000)");
        assertTrue(Boolean.TRUE.equals(p1.getPermPass()), "T1: p1 should become permanent");
        assertTrue(containsInList(flights[0].getPassengers(), p1), "T1: p1 should be in F100 passengers list");
        assertTrue(queueSizePreserve(flights[0].getWaiting()) == 0, "T1: F100 waiting should be empty");

        // Fill F100 second seat
        Passenger p2 = new Passenger(222, "Maya", 19000); // needs 1000 -> F100 (3000)
        Ex1.assignFlight(flights, p2);

        assertTrue(p2.getKm() == 22000, "T1b: p2 km should increase by 3000 (to 22000)");
        assertTrue(Boolean.TRUE.equals(p2.getPermPass()), "T1b: p2 should become permanent");
        assertTrue(countList(flights[0].getPassengers()) == 2, "T1b: F100 passengers should now be 2 (full)");

        // -----------------------------
        // TEST 2: assignFlight when flight is full -> passenger goes to waiting queue
        // (still gets km updated in assignFlight)
        // -----------------------------
        Passenger p3 = new Passenger(333, "Lior", 19900); // needs 100 -> F100 (3000) makes perm, but F100 is full
        Ex1.assignFlight(flights, p3);

        assertTrue(p3.getKm() == 22900, "T2: p3 km should increase by 3000 (to 22900)");
        assertTrue(Boolean.TRUE.equals(p3.getPermPass()), "T2: p3 should be permanent");
        assertTrue(queueContainsPreserve(flights[0].getWaiting(), p3), "T2: p3 should be in F100 waiting queue");
        assertTrue(countList(flights[0].getPassengers()) == 2, "T2: F100 passengers should remain 2");

        // -----------------------------
        // TEST 3: assignFlight chooses a different flight if shorter doesn't reach 20000
        // p4: 15000 + 3000 = 18000 (not enough), so should pick F200 (7000) => 22000
        // -----------------------------
        Passenger p4 = new Passenger(444, "Dana", 15000);
        Ex1.assignFlight(flights, p4);

        assertTrue(p4.getKm() == 22000, "T3: p4 km should increase by 7000 (to 22000)");
        assertTrue(Boolean.TRUE.equals(p4.getPermPass()), "T3: p4 should become permanent");
        assertTrue(containsInList(flights[1].getPassengers(), p4), "T3: p4 should be in F200 passengers list");
        assertTrue(queueSizePreserve(flights[1].getWaiting()) == 0, "T3: F200 waiting should be empty");

        // -----------------------------
        // TEST 4: cancelAndReplace on F100:
        // - remove p2 from passengers
        // - subtract flight km from p2 (should return to 19000 and not permanent)
        // - freed seat filled from waiting:
        //   choose permanent passenger with MAX km from waiting (here only p3)
        // -----------------------------
        Ex1.cancelAndReplace(flights, 0, p2);

        assertTrue(!containsInList(flights[0].getPassengers(), p2), "T4: p2 should be removed from F100 passengers");
        assertTrue(p2.getKm() == 19000, "T4: p2 km should decrease by 3000 (back to 19000)");
        assertTrue(Boolean.FALSE.equals(p2.getPermPass()), "T4: p2 should no longer be permanent");
        assertTrue(containsInList(flights[0].getPassengers(), p3), "T4: p3 should move from waiting to passengers");
        assertTrue(queueSizePreserve(flights[0].getWaiting()) == 0, "T4: F100 waiting should be empty after replacement");
        assertTrue(countList(flights[0].getPassengers()) == 2, "T4: F100 passengers should remain full (2)");

        // -----------------------------
        // TEST 5: cancelAndReplace branch "no permanent in waiting -> take first"
        // NOTE: With assignFlight as defined, waiting passengers are typically permanent already.
        // To explicitly test the 'otherwise first waiting passenger' branch, we enqueue a non-permanent manually.
        // -----------------------------
        Passenger np = new Passenger(555, "Ori", 5000); // non-permanent
        flights[1].getWaiting().enqueue(np);            // manual setup for the test

        Ex1.cancelAndReplace(flights, 1, p4);           // cancel p4 from F200

        assertTrue(!containsInList(flights[1].getPassengers(), p4), "T5: p4 should be removed from F200 passengers");
        assertTrue(p4.getKm() == 15000, "T5: p4 km should decrease by 7000 (back to 15000)");
        assertTrue(Boolean.FALSE.equals(p4.getPermPass()), "T5: p4 should no longer be permanent");
        assertTrue(containsInList(flights[1].getPassengers(), np), "T5: np should be moved from waiting to passengers");
        assertTrue(queueSizePreserve(flights[1].getWaiting()) == 0, "T5: F200 waiting should be empty");

        System.out.println("ALL TESTS PASSED.");
    }

    // =========================================================
    // Minimal "assert" helpers (no extra data structures)
    // =========================================================
    private static void assertTrue(boolean cond, String msg) {
        if (!cond) {
            throw new RuntimeException("ASSERT FAILED: " + msg);
        }
    }

    // Count nodes in passengers list
    private static int countList(Node<Passenger> head) {
        int c = 0;
        Node<Passenger> p = head;
        while (p != null) {
            c++;
            p = p.getNext();
        }
        return c;
    }

    // Check if a passenger object reference is in the list
    private static boolean containsInList(Node<Passenger> head, Passenger target) {
        Node<Passenger> p = head;
        while (p != null) {
            if (p.getValue() == target) return true; // reference equality is fine for tests
            p = p.getNext();
        }
        return false;
    }

    // Queue size without losing data: rotate elements through dequeue/enqueue.
    private static int queueSizePreserve(Queue<Passenger> q) {
        int n = 0;
        // Count by rotating once
        while (true) {
            Passenger x = q.dequeue();
            if (x == null) break;
            q.enqueue(x);
            n++;
            if (n > 1000000) break; // safety (should never happen)
            // Stop when we are back to the first element: we need a marker, but we don't have DS.
            // Instead, do a second pass to detect cycle length using a marker object.
        }

        // The above empties queue into itself but cannot terminate properly without a marker.
        // So we use a marker passenger (unique object) as sentinel.
        Passenger marker = new Passenger(-999999, "MARKER", 0);
        q.enqueue(marker);

        int count = 0;
        while (true) {
            Passenger x = q.dequeue();
            if (x == null) break; // should not happen
            if (x == marker) break;
            q.enqueue(x);
            count++;
        }
        // marker is removed; queue restored
        return count;
    }

    // Check if queue contains a specific passenger, preserving order/content.
    private static boolean queueContainsPreserve(Queue<Passenger> q, Passenger target) {
        Passenger marker = new Passenger(-999998, "MARKER2", 0);
        q.enqueue(marker);

        boolean found = false;
        while (true) {
            Passenger x = q.dequeue();
            if (x == null) break; // should not happen
            if (x == marker) break;
            if (x == target) found = true;
            q.enqueue(x);
        }
        // marker removed; queue restored
        return found;
    }
}
