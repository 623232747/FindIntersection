public class Event implements Comparable<Event> {

    Point p;

    public float getY() {
        return p.y;
    }

    Segment segment;
    EventType type;
    Event(Segment segment,EventType type,Point p){
        this.segment = segment;
        this.type = type;
        this.p = p;
    }

    @Override
    public int compareTo(Event e) {
        if (this.getY()-e.getY()!=0f) return this.getY()-e.getY()>0f?1:-1;
        else if(this.p.x-e.p.x!=0f) return this.p.x-e.p.x>0f?1:-1;
        else if(this.type.ordinal()-e.type.ordinal()!=0)return this.type.ordinal()-e.type.ordinal();
        else return this.segment.start.x-e.segment.start.x>0f?1:-1;
    }
}
