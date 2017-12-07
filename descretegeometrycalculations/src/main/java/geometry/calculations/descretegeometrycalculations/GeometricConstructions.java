package geometry.calculations.descretegeometrycalculations;

import android.util.Log;

import org.opencv.core.Point;

public class GeometricConstructions {

    /*
    W01 Ако су дате тачке X, Z и W, и рационалан броj r могуће jе конструисати
    тачку Y тако да важи:
            XY /ZW = r;

    */

    public static GeomPoint w01(GeomPoint X, GeomPoint Z, GeomPoint W, float r) {
        GeomPoint v = new GeomPoint(W.X() - Z.X(), W.Y() - Z.Y());
        float x = X.X() + r * v.X();
        float y = X.Y() + r * v.Y();
        return new GeomPoint(x, y, false);
    }

    /*
    W02 Ако су дате две тачке X и Y могуће jе конструисати праву XY ; услов
    одређености jе да су тачке X и Y различите;
    */

    public static Line w02(GeomPoint X, GeomPoint Y) {
        // TODO razmisliti gde ce se vrsiti provera ako su tacke iste
        return new Line(X, Y);
    }

    /*
    W03 Ако су дате две праве, могуће jе конструисати њихову заjедничку тачку;
    услов недегенерисаности jе да праве нису паралелне, а услов одређености
    да нису jеднаке;
    */

    public static GeomPoint w03(Line l1, Line l2) {
        float delta = l1.ACoef() * l2.BCoef() - l1.BCoef() * l2.ACoef();
        float deltaX = l1.CCoef() * l2.BCoef() - l1.BCoef() * l2.CCoef();
        float deltaY = l1.ACoef() * l2.CCoef() - l1.CCoef() * l2.ACoef();

        // TODO: 18.8.17. Konstanta
        if (Math.abs(delta) == 0) {
            //TODO nema resenja
            // return null;
            delta = 0.0000002f;
        }
        return new GeomPoint(deltaX / delta, deltaY / delta, false);
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

    public static GeomPoint w05(Line l, Circle c, GeomPoint P) {
        GeomPoint S1 = new GeomPoint(0, 0, false);
        GeomPoint S2 = new GeomPoint(0, 0, false);

        w04(l, c, S1, S2);

        if (P.equal(S1)) {
            return S2;
        } else {
            return S1;
        }
    }

    /*
    W06 Ако су дате две различите тачке X и Y могуће jе конструисати круг
    k(X, Y ) са центром у тачки X коjи садржи тачку Y ; услов недегенериса-
    ности jе да су тачке X и Y различите; */

    public static Circle w06(GeomPoint X, GeomPoint Y) {
        double r = Math.sqrt(Math.pow(X.X() - Y.X(), 2) + Math.pow(X.Y() - Y.Y(), 2));

        return new Circle(X, r);
    }
    /*
    W07 Ако су датa два круга, могуће jе конструисати њиховe две заjедничке
            тачке; услов недегенерисаности jе да се кругови секу, а услов одређености
    да су кругови различити;
    */

    public static int w07(Circle c1, Circle c2, GeomPoint S1, GeomPoint S2) {

        // distance between the centers
        double d = c1.getCenter().distance(c2.getCenter());

        // find number of solutions
        if (d > c1.getRadius() + c2.getRadius()) // circles are too far apart, no solution(s)
        {
            return 0;
        } else if (d == 0 && c1.getRadius() == c2.getRadius()) // circles coincide
        {
            return 0;
        }
        // one circle contains the other
        else if (d + Math.min(c1.getRadius(), c2.getRadius()) < Math.max(c1.getRadius(), c2.getRadius())) {
            //std::cout << "One circle contains the other\n";
            return 0;
        } else {
            double r = c1.getRadius();
            double r2 = c2.getRadius();
            double a = (r * r - r2 * r2 + d * d) / (2.0 * d);
            double h = Math.sqrt(r * r - a * a);

            // find p2
            GeomPoint c = c1.getCenter();
            GeomPoint C2 = c2.getCenter();

            double x = c.X() + (a * (C2.X() - c.X())) / d;
            double y = c.Y() + (a * (C2.Y() - c.Y())) / d;

            // find intersection points p3
            S1.setX((float) (x + (h * (C2.Y() - c.Y()) / d)));
            S1.setY((float) (y - (h * (C2.X() - c.X()) / d)));

            if (d == r + r2)
                return 1;

            S2.setX((float) (x - (h * (C2.Y() - c.Y()) / d)));
            S2.setY((float) (y + (h * (C2.X() - c.X()) / d)));


//            i2.setCoords( p2.x() - (h * (C2.c.y() - c.y())/ d),
//                    p2.y() + (h * (C2.c.x() - c.x())/ d)
//            );


            return 2;
        }
    }
    /*
    W08 Ако су датa два круга и jедна њихова заjедничка тачка, могуће jе кон-
    струисати њихову другу заjедничку тачку; услов недегенерисаности jе да
    се кругови секу, а услов одређености да су кругови различити;
    */

    public static GeomPoint w08(Circle c1, Circle c2, GeomPoint P) {
        GeomPoint S1 = new GeomPoint(0, 0, false);
        GeomPoint S2 = new GeomPoint(0, 0, false);

        int i = w07(c1, c2, S1, S2);

        if (i != 2) {
            // Ovo ne bi trebalo da se desi
            return null;
        }

        if (S1.equal(P)) {
            return S2;
        }

        return S1;
    }



    /*
    W09 Ако су дате тачке X и Y могуће jе конструисати круг са пречником XY ;
    услов недегенерисаности jе да су тачке различите;
    */

    public static Circle w09(GeomPoint X, GeomPoint Y) {
        GeomPoint S = new GeomPoint((X.X() + Y.X()) / 2, (X.Y() + Y.Y()) / 2, false);
        double r = Math.sqrt(Math.pow(X.X() - Y.X(), 2) + Math.pow(X.Y() - Y.Y(), 2)) / 2;

        return new Circle(S, r);

    }

    /*
    W10 Ако су дати тачка X и права p могуће jе конструисати праву q коjа са-
    држи тачку X и управна jе на праву p; */

    public static Line w10(GeomPoint X, Line l) {
        GeomPoint v = l.getVector();

        // GeomPoint v1 = new GeomPoint(-v.Y(), v.X());  vektor pravca nove prave
        // TODO da li novonastala tacka moze biti 0,0 posto X moze pripadati L

        return new Line(X, new GeomPoint(X.X() - v.Y() * 10, X.Y() + v.X() * 10));
    }

    /*
    W11 Aко су дати права p и тачка X коjа не припада правоj p могуће jе констру-
    исати круг k са центром X коjи додируjе праву p; услов недегенерисаности
    jе да тачка X не припада правоj p;*/

    public static Circle w11(Line p, GeomPoint X) {
        return new Circle(X, p.distance(X));
    }
    /*
    W12 Ако су дати круг k и тачка X ван круга k, могуће jе конструисати две
    тангенте из тачке X на круг k; услов недегенерисаности jе да jе тачка X
    ван круга k; */


    public static void w12(Circle k, GeomPoint X, Line t1, Line t2) {

        GeomPoint C = k.getCenter();
        double d = C.distance(X);

        double cos = k.getRadius() / d;
        double sin = Math.sqrt(1 - cos * cos);

        GeomPoint T1 = new GeomPoint(0, 0);
        GeomPoint T2 = new GeomPoint(0, 0);

        w04(new Line(X, C), k, T1, T2);

        double d1 = X.distance(T1);
        double d2 = X.distance(T2);

        GeomPoint T;

        if (d1 < d2) {
            T = T1;
        } else {
            T = T2;
        }

        float x = T.X() - C.X();
        float y = T.Y() - C.Y();

        t1.setBegin(X);
        t1.setEnd(new GeomPoint((float) (x * cos - y * sin) + C.X(), (float) (x * sin + y * cos) + C.Y()));

        t2.setBegin(X);
        t2.setEnd(new GeomPoint((float) (x * cos + y * sin) + C.X(), (float) (-x * sin + y * cos) + C.Y()));
    }

    /*
    W13 Ако су дати круг k, тачка X ван круга k и jедна тангента из тачке X
    на круг k, могуће jе конструисати другу тангенту из тачке X на круг k;
    услов недегенерисаности jе да jе тачка X ван круга k;*/

    public static Line w13(Circle k, GeomPoint X, Line t) {
        Line t1 = new Line(X, new GeomPoint(0, 0));
        Line t2 = new Line(X, new GeomPoint(0, 0));
        t1.setConstraints(k.constants);
        t2.setConstraints(k.constants);

        w12(k, X, t1, t2);

        if (t1.contain(t.getEnd())) {
            return t2;
        }

        return t1;
    }

    /*
    W14 Ако су дате тачке X и Y могуће jе конструисати медиjатрису дужи XY ;
    услов одређености jе да су тачке X и Y различите;
    */

    public static Line w14(GeomPoint X, GeomPoint Y) {
        GeomPoint A = new GeomPoint((X.X() + Y.X()) / 2, (X.Y() + Y.Y()) / 2);
//        GeomPoint v= new GeomPoint( - (X.Y() - Y.Y()), X.X() - Y.X());
//        return new Line(A, new GeomPoint(A.X() + v.X(), A.Y() + v.Y()));
        return GeometricConstructions.w10(A, new Line(X, Y));
    }

    /*
    W15 Aко су дати тачка X, права p и рационалан броj r, могуће jе конструи-
    сати праву коjа jе слика праве p при хомотетиjи у односу на тачку X са
    коефициjентом r; */


    // TODO proveriti
    public static Line w15(GeomPoint X, Line p, float r) {
        GeomPoint begin = w01(X, X, p.getBegin(), r);
        GeomPoint end = w01(X, X, p.getEnd(), r);

        return new Line(begin, end);
    }

    /*
    W16 Ако су дати тачка X и права p могуће jе конструисати праву коjа садржи
    тачку X и паралелна jе правоj p; */

    public static Line w16(GeomPoint X, Line p) {
        float dx = p.getEnd().X() - p.getBegin().X();
        float dy = p.getEnd().Y() - p.getBegin().Y();

        return new Line(X, new GeomPoint(X.X() + dx, X.Y() + dy));
    }

    private static Line podUglom(GeomPoint X, GeomPoint Y, double alpha) {
        float xp = Y.X() - X.X();
        float yp = Y.Y() - X.Y();

        double x = Math.cos(alpha) * xp - Math.sin(alpha) * yp;
        double y = Math.sin(alpha) * xp + Math.cos(alpha) * yp;

        return new Line(X, new GeomPoint((float) x + X.X(), (float) y + X.Y()));
    }
    /*
    W17 Ако су дате тачке X и Y и угао α могуће jе конструисати праву q тако
    да jе угао ∠(XY , q) = A · α/2B + C · π/2D; */

    // Isprobano radi
    public static Line w17(GeomPoint X, GeomPoint Y, GeomPoint A, GeomPoint B, GeomPoint C, int a,
                           int b, int c, int d) {
        double alpha = angle(A, B, C);

        alpha = a * alpha / Math.pow(2, b) + c * Math.PI / Math.pow(2, d);

        // translacija td X predje u koord pocetak
        return podUglom(X, Y, -alpha);
    }

    public static Line w18(GeomPoint X, GeomPoint Y, GeomPoint A, GeomPoint B, GeomPoint C, int a,
                           int b, int c, int d) {
        double alpha = angle(A, B, C);

        alpha = a * alpha / Math.pow(2, b) + c * Math.PI / Math.pow(2, d);

        // translacija td X predje u koord pocetak
        return podUglom(X, Y, alpha);
    }

    /*
    W19 Ако су дате тачке X, Y и Z могуће jе конструисати тачку W коjа jе
    хармониjски спрегнута са осталим; услов недегенерисаности jе да су тачке
    X и Y различите, тачке Y и Z различите и да тачка Y ниjе средиште дужи
    XZ;*/

    public static GeomPoint w19(GeomPoint X, GeomPoint Y, GeomPoint Z) {
        /*Tacka van prave XY
        * */

        // rotiram Y oko Z za 90
        float x = Y.X() - Z.X();
        float y = Y.Y() - Z.Y();

        float tmp = x;

        x = -y + Z.Y();
        y = tmp + Z.X();

        GeomPoint Rotirana = new GeomPoint(x, y);

        Line TbR = new Line(Z, Rotirana);

        GeomPoint Sred = new GeomPoint((Y.X() + Rotirana.X()) / 2, (Y.Y() + Rotirana.Y()) / 2);

        Line XSred = new Line(X, Sred);

        Line YSred = new Line(Y, Sred);

        GeomPoint G = w03(TbR, XSred);

        Line XP = new Line(X, Rotirana);

        Line YG = new Line(Y, G);

        GeomPoint D = w03(XP, YG);

        Line DSred = new Line(Sred, D);


        return w03(DSred, new Line(X, Z));

//        GeomPoint O = new GeomPoint((X.X() + Y.X()) / 2 + 200, (X.Y() + Y.Y()) / 2 + 200);
//
//        Line b = new Line(X, O);
//        Line c = w16(Z, b);
//        Line m = new Line(Y, O);
//
//        GeomPoint E = w03(c, m);
//
//        GeomPoint F = w01(Z, E, Z, 1);
//
//        Line n = new Line(O, F);
//        Line a = new Line(X, Y);

//        return w03(n, a);
    }

    // Ovde ce povratna vrednost biti krug
    /*
    W20 Ако су дате тачке X и Y и угао α могуће jе конструисати скуп тачака из
    коjих се дуж XY види под углом A · α/2
    B + C · π/2D. */


    public static Circle w20(GeomPoint X, GeomPoint Y, GeomPoint A, GeomPoint B, GeomPoint C, int a, int b, int c, int d) {
        double alpha = angle(A, B, C);

        alpha = a * alpha / Math.pow(2, b) + c * Math.PI / Math.pow(2, d);

        //90-(1/pow(2,1)*angle[_G15392]+1/pow(2,1)*180)
        double angle = Math.PI / 2 - alpha;

        Line l = podUglom(X, Y, -angle);

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

    public static Line bisectorAngle(GeomPoint A, GeomPoint B, GeomPoint C) {
        GeomPoint B1 = w01(B, B, C, (float) (B.distance(A) / B.distance(C)));
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


    public static Circle circleAroundTriangle(GeomPoint A, GeomPoint B, GeomPoint C) {
        Line l1 = w14(A, B);
        Line l2 = w14(B, C);

        GeomPoint Cen = w03(l1, l2);

        return new Circle(Cen, Cen.distance(A));
    }

    public static Circle circleInsideTriangle(GeomPoint A, GeomPoint B, GeomPoint C) {
        Line l1 = bisectorAngle(A, B, C);
        Line l2 = bisectorAngle(B, C, A);

        GeomPoint Cen = w03(l1, l2);
        Line a = new Line(A, B);

        return new Circle(Cen, a.distance(Cen));
    }

    public static GeomPoint eulerPoint(Line h, Circle k, Line a) {
        return w05(h, k, w03(h, a));
    }


}
