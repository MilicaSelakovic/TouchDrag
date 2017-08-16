package com.example.descretegeometrycalculations;

import android.util.Log;

import org.opencv.core.Point;

public class GeometricConstructions {

    /*
    W01 Ако су дате тачке X, Z и W, и рационалан броj r могуће jе конструисати
    тачку Y тако да важи:
            XY /ZW = r;

    */

    public static GeomPoint w01(GeomPoint X, GeomPoint Z, GeomPoint W, float r){
        GeomPoint v = new GeomPoint(W.X() - Z.X(), W.Y() - Z.Y());
        float x = X.X() + r*v.X();
        float y = X.Y() + r*v.Y();
        return new GeomPoint(x, y, false);
    }

    /*
    W02 Ако су дате две тачке X и Y могуће jе конструисати праву XY ; услов
    одређености jе да су тачке X и Y различите;
    */

    public static Line w02(GeomPoint X, GeomPoint Y){
        // TODO razmisliti gde ce se vrsiti provera ako su tacke iste
        return new Line(X, Y);
    }

    /*
    W03 Ако су дате две праве, могуће jе конструисати њихову заjедничку тачку;
    услов недегенерисаности jе да праве нису паралелне, а услов одређености
    да нису jеднаке;
    */

    public static GeomPoint w03(Line l1, Line l2){
        float delta = l1.ACoef()*l2.BCoef() - l1.BCoef()*l2.ACoef();
        float deltaX = l1.CCoef()*l2.BCoef() - l1.BCoef()*l2.CCoef();
        float deltaY = l1.ACoef()*l2.CCoef() - l1.CCoef()*l2.ACoef();

        if(Math.abs(delta) < GeometricObject.EPISLON){
            //TODO nema resenja
        }
        return new GeomPoint(deltaX/delta,deltaY/delta, false);
    }

    /*
    W04 Ако су дати права и круг, могуће jе конструисати њихове заjедничке
            тачке; услов недегенерисаности jе да се права и круг секу;

    */

    public static int w04(Line l, Circle k, GeomPoint s1, GeomPoint s2) {

        GeomPoint pointA = l.getBegin();
        GeomPoint pointB = l.getEnd();

        double baX = pointB.X() - pointA.X();
        double baY = pointB.Y() - pointA.Y();
        double caX = k.getCenter().X() - pointA.X();
        double caY = k.getCenter().Y() - pointA.Y();

        double a = baX * baX + baY * baY;
        double bBy2 = baX * caX + baY * caY;
        double c = caX * caX + caY * caY - k.getRadius() * k.getRadius();

        double pBy2 = bBy2 / a;
        double q = c / a;

        double disc = pBy2 * pBy2 - q;
        if (disc < 0) {
            return 0;
        }

        double tmpSqrt = Math.sqrt(disc);
        double abScalingFactor1 = -pBy2 + tmpSqrt;
        double abScalingFactor2 = -pBy2 - tmpSqrt;


        s1.setX((float) (pointA.X() - baX * abScalingFactor1));
        s1.setY((float) (pointA.Y() - baY * abScalingFactor1));
        if (disc == 0) { // abScalingFactor1 == abScalingFactor2
            return 1;
        }
        s2.setX((float) (pointA.X() - baX * abScalingFactor2));
        s2.setY((float) (pointA.Y() - baY * abScalingFactor2));
        return 2;

    }

    /*
    W05 Ако су дати права и круг и jедна њихова заjедничка тачка, могуће jе
    конструисати њихову другу заjедничку тачку; услов недегенерисаности jе
    да се права и круг секу;*/

    public static GeomPoint w05(Line l, Circle c, GeomPoint P){
        GeomPoint S1 = new GeomPoint(0, 0, false);
        GeomPoint S2 = new GeomPoint(0, 0, false);

        w04(l, c, S1, S2);

        if( P.equal(S1)) {
            return S2;
        } else {
            return S1;
        }
    }

    /*
    W06 Ако су дате две различите тачке X и Y могуће jе конструисати круг
    k(X, Y ) са центром у тачки X коjи садржи тачку Y ; услов недегенериса-
    ности jе да су тачке X и Y различите; */

    public static Circle w06(GeomPoint X, GeomPoint Y){
        double r = Math.sqrt(Math.pow(X.X() - Y.X(), 2) + Math.pow(X.Y() - Y.Y(), 2));

        return new Circle(X, r);
    }
    /*
    W07 Ако су датa два круга, могуће jе конструисати њиховe две заjедничке
            тачке; услов недегенерисаности jе да се кругови секу, а услов одређености
    да су кругови различити;
    */

    public static int w07(Circle c1, Circle c2, GeomPoint S1, GeomPoint S2){
        double d = c1.getCenter().distance(c2.getCenter());
        double r1 = c1.getRadius();
        double r2 = c2.getRadius();

        if(d + 0.001 > r1 + r1 ){
            return 0;
        }

        // TODO konstanta
        if (Math.abs(r1+r2-d) < 0.001){
            GeomPoint P = w01(c1.getCenter(), c1.getCenter(), c2.getCenter(), (float)(r1/r2));
            S1.setX(P.X());
            S2.setY(P.Y());

            return 1;
        }

        // Tacka P je presek S1S2 sa C1C2
        // x je |PC2|

        double x = (d*d + r2*r2 - r1*r1)/(2*d);

        // a = |PS1| = |PS2|
        GeomPoint P = w01(c2.getCenter(), c2.getCenter(), c1.getCenter(), (float)(x/d));

        // vektor pravca normale na C1C2
        GeomPoint s = new GeomPoint(-(c2.getCenter().Y() - c1.getCenter().Y()), c2.getCenter().X() - c1.getCenter().X());

        float a = (float)Math.sqrt(r2*r2 - x*x);

        S1.setX(P.X() + a*s.X());
        S1.setY(P.Y() + a*s.Y());

        S2.setX(P.X() - a*s.X());
        S2.setY(P.Y() - a*s.Y());

        return 2;

    }
    /*
    W08 Ако су датa два круга и jедна њихова заjедничка тачка, могуће jе кон-
    струисати њихову другу заjедничку тачку; услов недегенерисаности jе да
    се кругови секу, а услов одређености да су кругови различити;
    */

    public static GeomPoint w08(Circle c1, Circle c2, GeomPoint P){
        GeomPoint S1 = new GeomPoint(0,0, false);
        GeomPoint S2 = new GeomPoint(0,0, false);

        int i = w07(c1, c2, S1, S2);

        if(i !=  2){
            // Ovo ne bi trebalo da se desi
            return null;
        }

        if(S1.equal(P)){
            return S2;
        }

        return S1;
    }



    /*
    W09 Ако су дате тачке X и Y могуће jе конструисати круг са пречником XY ;
    услов недегенерисаности jе да су тачке различите;
    */

    public static Circle w09(GeomPoint X, GeomPoint Y){
        GeomPoint S = new GeomPoint((X.X() + Y.X())/ 2, (X.Y() + Y.Y())/2, false);
        double r = Math.sqrt(Math.pow(X.X() - Y.X(), 2) + Math.pow(X.Y() - Y.Y(), 2))/2;

        return new Circle(S, r);

    }

    /*
    W10 Ако су дати тачка X и права p могуће jе конструисати праву q коjа са-
    држи тачку X и управна jе на праву p; */

    public static Line w10(GeomPoint X, Line l){
        GeomPoint v = l.getVector();

        // GeomPoint v1 = new GeomPoint(-v.Y(), v.X());  vektor pravca nove prave
        // TODO da li novonastala tacka moze biti 0,0 posto X moze pripadati L

        return new Line(X, new GeomPoint(X.X() - v.Y(), X.Y() + v.X()));
    }

    /*
    W11 Aко су дати права p и тачка X коjа не припада правоj p могуће jе констру-
    исати круг k са центром X коjи додируjе праву p; услов недегенерисаности
    jе да тачка X не припада правоj p;*/

    public static Circle w11(Line p, GeomPoint X){
        return new Circle(X, p.distance(X));
    }
    /*
    W12 Ако су дати круг k и тачка X ван круга k, могуће jе конструисати две
    тангенте из тачке X на круг k; услов недегенерисаности jе да jе тачка X
    ван круга k; */

    //TODO proveriti tacnost

    public static void w12(Circle k, GeomPoint X, Line t1, Line t2){
        double d = k.getCenter().distance(X);
        GeomPoint T1 = new GeomPoint(0,0);
        GeomPoint T2 = new GeomPoint(0,0);

        double a = Math.sqrt(d*d - Math.pow(k.getRadius(),2));

        w07(k, new Circle(X, a), T1, T2);

        t1.setBegin(X);
        t2.setEnd(T1);

        t2.setBegin(X);
        t2.setEnd(T2);
    }

    /*
    W13 Ако су дати круг k, тачка X ван круга k и jедна тангента из тачке X
    на круг k, могуће jе конструисати другу тангенту из тачке X на круг k;
    услов недегенерисаности jе да jе тачка X ван круга k;*/

    public static Line w13(Circle k, GeomPoint X, Line t){
        Line t1 = new Line(X, new GeomPoint(0,0));
        Line t2 = new Line(X, new GeomPoint(0,0));

        w12(k, X, t1, t2);

        if(t1.getEnd().equal(t.getEnd())){
            return t2;
        }

        return t1;
    }

    /*
    W14 Ако су дате тачке X и Y могуће jе конструисати медиjатрису дужи XY ;
    услов одређености jе да су тачке X и Y различите;
    */

    public static Line w14(GeomPoint X, GeomPoint Y){
        GeomPoint A = new GeomPoint((X.X() + Y.X())/2, (X.Y() + Y.Y()) /2);
//        GeomPoint v= new GeomPoint( - (X.Y() - Y.Y()), X.X() - Y.X());
//        return new Line(A, new GeomPoint(A.X() + v.X(), A.Y() + v.Y()));
        return GeometricConstructions.w10(A, new Line(X, Y));
    }

    /*
    W15 Aко су дати тачка X, права p и рационалан броj r, могуће jе конструи-
    сати праву коjа jе слика праве p при хомотетиjи у односу на тачку X са
    коефициjентом r; */


    // TODO proveriti
    public static Line w15(GeomPoint X, Line p, float r){
        GeomPoint begin = w01(X,X, p.getBegin(), r);
        GeomPoint end = w01(X, X, p.getEnd(), r);

        return new Line(begin, end);
    }

    /*
    W16 Ако су дати тачка X и права p могуће jе конструисати праву коjа садржи
    тачку X и паралелна jе правоj p; */

    public static Line w16(GeomPoint X, Line p){
        float dx = p.getEnd().X() - p.getBegin().X();
        float dy = p.getEnd().Y() - p.getBegin().Y();

        return new Line(X, new GeomPoint(X.X() + dx, X.Y() + dy));
    }

    /*
    W17 Ако су дате тачке X и Y и угао α могуће jе конструисати праву q тако
    да jе угао ∠(XY , q) = A · α/2B + C · π/2D; */

    public static Line w17(GeomPoint X, GeomPoint Y, GeomPoint A, GeomPoint B, GeomPoint C, int a,
                           int b, int c, int d) {
        double alpha = angle(A, B, C);

        alpha = a * alpha / Math.pow(2, b) + c * Math.PI / Math.pow(2, d);

        double x = Math.cos(alpha) * Y.X() - Math.sin(alpha) * Y.Y();
        double y = Math.sin(alpha) * Y.X() + Math.cos(alpha) * Y.Y();

        return new Line(X, new GeomPoint((float) x, (float) y));
    }

    /*
    W19 Ако су дате тачке X, Y и Z могуће jе конструисати тачку W коjа jе
    хармониjски спрегнута са осталим; услов недегенерисаности jе да су тачке
    X и Y различите, тачке Y и Z различите и да тачка Y ниjе средиште дужи
    XZ;*/

    public static GeomPoint w19(GeomPoint X, GeomPoint Y, GeomPoint Z) {
        /*Tacka van prave XY
        * */

        GeomPoint O = new GeomPoint((X.X() + Y.X()) / 2 + 200, (X.Y() + Y.Y()) / 2 + 200);

        Line b = new Line(X, O);
        Line c = w16(Z, b);
        Line m = new Line(Y, O);

        GeomPoint E = w03(c, m);

        GeomPoint F = w01(Z, E, Z, 1);

        Line n = new Line(O, F);
        Line a = new Line(X, Y);

        return w03(n, a);
    }

    // Ovde ce povratna vrednost biti krug
    /*
    W20 Ако су дате тачке X и Y и угао α могуће jе конструисати скуп тачака из
    коjих се дуж XY види под углом A · α/2
    B + C · π/2D. */


    public static Circle w20(GeomPoint X, GeomPoint Y, GeomPoint A, GeomPoint B, GeomPoint C, int a, int b, int c, int d) {
        Line l = w17(X, Y, A, B, C, a, b, c, d);

        Line m = w14(X, Y);

        GeomPoint O = w03(l, m);
        return w06(O, A);
    }

    /*
    W22 Ако су дати тачка X и круг k1 могуће jе конструисати круг k2 са центром
    у тачки X коjи изнутра додируjе круг k1; услов недегенерисаности jе да
    je тачка X унутар круга k1 и да тачка X ниjе центар круга k1;
    */

    public static Circle w22(GeomPoint X, Circle k) {
        double XO = X.distance(k.getCenter());

        double r = k.getRadius() - XO;

        return new Circle(X, r);
    }

    /*Simetrala ugla*/

    public static Line bisectorAngle(GeomPoint A, GeomPoint B, GeomPoint C){
        GeomPoint B1 = w01(B, B, C, (float) (B.distance(A)/B.distance(C)));
        GeomPoint S = w01(A, A, B1, 0.5f);

        return new Line(B, S);
        
    }

    public static Line medianLine(GeomPoint A, GeomPoint B, GeomPoint C) {
        GeomPoint sB = new GeomPoint((A.X() + C.X()) / 2, (A.Y() + C.Y()) / 2);

        return new Line(B, sB);
    }

    static GeomPoint centroid(GeomPoint A, GeomPoint B, GeomPoint C) {
        Line tb = medianLine(A, B, C);
        Line tc = medianLine(B, C, A);

        return w03(tb, tc);
    }

    static GeomPoint incenter(GeomPoint A, GeomPoint B, GeomPoint C) {
        Line sb = GeometricConstructions.bisectorAngle(A, B, C);
        Line sc = GeometricConstructions.bisectorAngle(B, C, A);

        return GeometricConstructions.w03(sb, sc);
    }

    static GeomPoint circumcenter(GeomPoint A, GeomPoint B, GeomPoint C) {
        Line sBC = GeometricConstructions.w10(new GeomPoint((B.X() + C.X()) / 2, (B.Y() + C.Y()) / 2), new Line(B, C));
        Line sAC = GeometricConstructions.w10(new GeomPoint((A.X() + C.X()) / 2, (A.Y() + C.Y()) / 2), new Line(A, C));

        return GeometricConstructions.w03(sBC, sAC);

    }

    static GeomPoint orthocenter(GeomPoint A, GeomPoint B, GeomPoint C) {
        Line hb = GeometricConstructions.w10(B, new Line(A, C));
        Line ha = GeometricConstructions.w10(A, new Line(B, C));

        return GeometricConstructions.w03(hb, ha);
    }


    private static double angle(GeomPoint X, GeomPoint Y, GeomPoint Z) {

        float dx1 = Y.X() - X.X();
        float dy1 = Y.Y() - X.Y();

        float dx2 = Y.X() - Z.X();
        float dy2 = Y.Y() - Z.Y();

        Point P = new Point(dx1, dy1);
        Point R = new Point(dx2, dy2);

        double dprod = P.dot(R);
        double normp = Math.sqrt(P.dot(P));
        double normr = Math.sqrt(R.dot(R));

        return Math.acos(dprod / (normp * normr));
    }

}
