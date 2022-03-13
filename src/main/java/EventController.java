import java.util.*;


public class EventController {

    public static EventController getEventController() {
        return eventController;
    }

    static EventController eventController = new EventController();

    ArrayList<Segment> segments = new ArrayList<>();

    float scanLineY = 0;

    BSTree<Segment> scanLineStatus = new BSTree<>(); // 应该是一个搜索树 but 搜索树如何实时排序？？？

    PriorityQueue<Event> events = new PriorityQueue<>(); // 应该是一个优先队列

    void find() {
        for (Segment s : segments) {
            Event se = new Event(s, EventType.Start, s.start);
            Event ee = new Event(s, EventType.End, s.end);
            events.add(se);
            events.add(ee);
        }
        while (!events.isEmpty()) {
            Event e = events.poll();
//            System.out.println(e.type);
//            System.out.println(e.segment.name);
//            System.out.println(e.getY());
            scanLineY = e.getY();
            switch (e.type) {
                case Start:

                    scanLineStatus.insert(e.segment);
                    BSTree<Segment>.BSTNode<Segment> node = scanLineStatus.search(e.segment);
                    BSTree<Segment>.BSTNode<Segment> pre = scanLineStatus.predecessor(node);
                    BSTree<Segment>.BSTNode<Segment> suc = scanLineStatus.successor(node);
//                    while(pre!=null&&e.segment.getX(scanLineY)<pre.getKey().getX(scanLineY))
//                        pre = scanLineStatus.predecessor(pre);
//                    while(suc!=null&&e.segment.getX(scanLineY)>suc.getKey().getX(scanLineY))
//                        suc = scanLineStatus.successor(suc);
                    if (pre != null) {
                        Segment neighbor = pre.getKey();
                        Point p = neighbor.getIntersect(e.segment);
                        if (p != null) {
                            Event neighborInsect = new Event(neighbor, EventType.Intersect, p);
                            Event thisInsect = new Event(e.segment, EventType.Intersect, p);
                            events.add(neighborInsect);
                            events.add(thisInsect);
                        }
                    }
                    if (suc != null) {
                        Segment neighbor = suc.getKey();
                        Point p = e.segment.getIntersect(neighbor);
                        if (p != null) {
                            Event neighborInsect = new Event(neighbor, EventType.Intersect, p);
                            Event thisInsect = new Event(e.segment, EventType.Intersect, p);
                            events.add(neighborInsect);
                            events.add(thisInsect);
                        }
                    }
                    break;
                case Intersect:
                    System.out.printf("卧槽！在点：（%f, %f）处发生了香蕉！香蕉的线段是：\n",e.p.x,e.p.y);
                    HashSet<Segment> segmentArrayList = new HashSet<>();
                    segmentArrayList.add(e.segment);
                    Event next = events.peek();
                    while (next != null &&next.type==EventType.Intersect&& e.p.x == next.p.x && e.p.y == next.p.y) {
                        events.poll();
                        segmentArrayList.add(next.segment);
                        next = events.peek();
                    }
                    Iterator<Segment>it = segmentArrayList.iterator();
                    while (it.hasNext()) {
                        Segment segment = it.next();
                        System.out.println(segment.name);

                        scanLineY -= 0.0001f;
                        if (scanLineStatus.search(segment)!=null){
                        scanLineStatus.remove(segment);
                        scanLineY += 0.0001f;
                        scanLineStatus.insert(segment);}
                        else                         scanLineY += 0.0001f;

                    }



                    pre = scanLineStatus.search(e.segment);
                    BSTree<Segment>.BSTNode<Segment> prepre = scanLineStatus.predecessor(pre);
                    while(prepre!=null&&segmentArrayList.contains(prepre.getKey())){
                        pre=prepre;
                        prepre=scanLineStatus.predecessor(pre);
                    }

                    if (prepre != null) {
                        Segment neighbor = prepre.getKey();
                        Point p = neighbor.getIntersect(pre.getKey());
                        if (p != null) {
                            Event neighborInsect = new Event(neighbor, EventType.Intersect, p);
                            Event thisInsect = new Event(pre.getKey(), EventType.Intersect, p);
                            events.add(neighborInsect);
                            events.add(thisInsect);
                        }
                    }

                    suc = scanLineStatus.search(e.segment);
                    BSTree<Segment>.BSTNode<Segment> sucsuc = scanLineStatus.successor(suc);
                    while(sucsuc!=null&&segmentArrayList.contains(sucsuc.getKey())){
                        suc=sucsuc;
                        sucsuc=scanLineStatus.successor(suc);
                    }
                    if (sucsuc != null) {
                        Segment neighbor = sucsuc.getKey();
                        Point p = neighbor.getIntersect(suc.getKey());
                        if (p != null) {
                            Event neighborInsect = new Event(neighbor, EventType.Intersect, p);
                            Event thisInsect = new Event(suc.getKey(), EventType.Intersect, p);
                            events.add(neighborInsect);
                            events.add(thisInsect);
                        }
                    }
                    while (next != null && e.p.x == next.p.x && e.p.y == next.p.y) {
                        events.poll();
                        scanLineStatus.remove(next.segment);
                        next = events.peek();
                    }
                    break;
                case End:
                    node = scanLineStatus.search(e.segment);
                    pre = scanLineStatus.predecessor(node);
                    suc = scanLineStatus.successor(node);
                    scanLineStatus.remove(e.segment);
                    if (pre != null && suc != null) {
                        Segment preNeighbor = pre.getKey();
                        Segment nextNeighbor = suc.getKey();
                        Point p = preNeighbor.getIntersect(nextNeighbor);
                        if (p != null) {
                            Event preNeighborInsect = new Event(preNeighbor, EventType.Intersect, p);
                            Event nextNeighborInsect = new Event(nextNeighbor, EventType.Intersect, p);
                            events.add(preNeighborInsect);
                            events.add(nextNeighborInsect);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }


}
