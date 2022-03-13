public class Segment implements Comparable<Segment>{
    String name;
    Point start;
    Point end;
    // 约束：start.y < end.y

    float getSlope() {
        return (start.x - end.x) / (start.y - end.y);
    }
    Segment(String name,Point A,Point B){
        this.name = name;
        if (A.y>B.y){
            start = B;
            end = A;
        }
        else{
            start = A;
            end = B;
        }
    }
    float trArea(Point A,Point B,Point C){
        return (A.x-C.x)*(B.y-C.y)-(A.y-C.y)*(B.x-C.x);
    }
    Point getIntersect(Segment s){
        Point A = this.start;
        Point B = this.end;
        Point C = s.start;
        Point D = s.end;
        float tr_ABC = trArea(A,B,C);
        float tr_ABD = trArea(A,B,D);
        float tr_CDA = trArea(C,D,A);
        float tr_CDB = trArea(C,D,B);
        if(tr_ABC*tr_ABD>0){
            return null;
        }
        if(tr_CDA*tr_CDB>0){
            return null;
        }
        float t = tr_CDA/(tr_ABD-tr_ABC);
        return new Point(A.x+t*(B.x-A.x),A.y+t*(B.y-A.y));
    }

    float getX(float y){
        if((y>end.y)||(y<start.y)){
            return -1f;
        }
        return (y-start.y)*(end.x-start.x)/(end.y-start.y)+start.x;
    }

    @Override
    public int compareTo(Segment o) {
        if(name.equals(o.name))return 0;
        float y = EventController.getEventController().scanLineY;
        if(this.getX(y)!=o.getX(y))return this.getX(y)>o.getX(y)?1:-1;
        else  return this.getSlope()>o.getSlope()?1:-1;
    }
    @Override
    public int hashCode() {
        // return id.hashCode();
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "Segment{" +
                "name='" + name + '\'' +
                "slope='"+getSlope()+'\''+
                '}';
    }
}
