package com.example.descretegeometrycalculations;

import org.opencv.core.Mat;

/**
 * Created by milica on 22.5.17..
 */

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
        return new GeomPoint(x, y);
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
        float deltaY = l1.ACoef()*l2.CCoef() - l1.BCoef()*l2.CCoef();

        if(Math.abs(delta) < GeometricObject.EPISLON){
            //TODO nema resenja
        }
        return new GeomPoint(deltaX/delta,deltaY/delta);
    }

    /*
    W04 Ако су дати права и круг, могуће jе конструисати њихове заjедничке
            тачке; услов недегенерисаности jе да се права и круг секу;

    */

    public static int w04(Line l, Circle c, GeomPoint s1, GeomPoint s2){
        double d = l.distance(c.getCenter()) - c.getRadius();
        // TODO konstanta
        double epsilon = 10;

        GeomPoint p = l.getVector();
        GeomPoint P = l.getBegin();

        GeomPoint PC = new GeomPoint(c.getCenter().X() - P.X(), c.getCenter().Y() - P.Y());

        double t = ConnectionCalculations.dotProduct(PC, p);

        double sx = P.X() + t*p.X();
        double sy = P.Y() + t*p.Y();

        if(d > epsilon){
            return 0;
        } else if (Math.abs(d) < epsilon){
            s1.setX((float)sx);
            s1.setY((float)sy);
            return 1;
        } else {
            double a = Math.sqrt(c.getRadius()*c.getRadius() - d*d);

            s1.setX((float)(sx - a*p.X()));
            s1.setY((float)(sy - a*p.Y()));

            s1.setX((float)(sx + a*p.X()));
            s1.setY((float)(sy + a*p.Y()));
            return 2;
        }

    }

    /*
    W05 Ако су дати права и круг и jедна њихова заjедничка тачка, могуће jе
    конструисати њихову другу заjедничку тачку; услов недегенерисаности jе
    да се права и круг секу;*/

    public static GeomPoint w05(Line l, Circle c, GeomPoint P){
        GeomPoint S1 = new GeomPoint(0, 0);
        GeomPoint S2 = new GeomPoint(0, 0);

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

    private static Circle w06(GeomPoint X, GeomPoint Y){
        double r = Math.sqrt(Math.pow(X.X() - Y.X(), 2) + Math.pow(X.Y() - Y.Y(), 2));

        return new Circle(X, r);
    }
    /*
    W07 Ако су датa два круга, могуће jе конструисати њиховe две заjедничке
            тачке; услов недегенерисаности jе да се кругови секу, а услов одређености
    да су кругови различити;
    */
    //TODO napisati presek dva kruga

    /*
    W08 Ако су датa два круга и jедна њихова заjедничка тачка, могуће jе кон-
    струисати њихову другу заjедничку тачку; услов недегенерисаности jе да
    се кругови секу, а услов одређености да су кругови различити;
    */

    /*
    W09 Ако су дате тачке X и Y могуће jе конструисати круг са пречником XY ;
    услов недегенерисаности jе да су тачке различите;
    */

    public static Circle w09(GeomPoint X, GeomPoint Y){
        GeomPoint S = new GeomPoint((X.X() + Y.X())/ 2, (X.Y() + Y.Y())/2);
        double r = Math.sqrt(Math.pow(X.X() - Y.X(), 2) + Math.pow(X.Y() - Y.Y(), 2))/2;

        return new Circle(S, r);

    }

    /*
    W10 Ако су дати тачка X и права p могуће jе конструисати праву q коjа са-
    држи тачку X и управна jе на праву p; */

    public static Line w10(GeomPoint X, Line l){
        GeomPoint v = l.getVector();

        // GeomPoint v1 = new GeomPoint(-v.Y(), v.X());  vektor pravca nove prave

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


    /*
    W13 Ако су дати круг k, тачка X ван круга k и jедна тангента из тачке X
    на круг k, могуће jе конструисати другу тангенту из тачке X на круг k;
    услов недегенерисаности jе да jе тачка X ван круга k;*/

    /*
    W14 Ако су дате тачке X и Y могуће jе конструисати медиjатрису дужи XY ;
    услов одређености jе да су тачке X и Y различите;
    */

    public static GeomPoint w14(GeomPoint X, GeomPoint Y){
        return new GeomPoint((X.X() + Y.X())/2, (X.Y() + Y.Y()) /2);
    }

    /*
    W15 Aко су дати тачка X, права p и рационалан броj r, могуће jе конструи-
    сати праву коjа jе слика праве p при хомотетиjи у односу на тачку X са
    коефициjентом r; */


    /*
    W16 Ако су дати тачка X и права p могуће jе конструисати праву коjа садржи
    тачку X и паралелна jе правоj p; */

    public static Line w16(GeomPoint X, Line p){
        return new Line(X, new GeomPoint(X.X() + p.getVector().X(), X.Y() + p.getVector().Y()));
    }

    /*
    TODO ovde fali w18 i w17 ne razumem
    W17 Ако су дате тачке X и Y и угао α могуће jе конструисати праву q тако
    да jе угао ∠(XY , q) = A · α/2B + C · π/2D; */

    /*
    W19 Ако су дате тачке X, Y и Z могуће jе конструисати тачку W коjа jе
    хармониjски спрегнута са осталим; услов недегенерисаности jе да су тачке
    X и Y различите, тачке Y и Z различите и да тачка Y ниjе средиште дужи
    XZ;*/

    /*
    W20 Ако су дате тачке X и Y и угао α могуће jе конструисати скуп тачака из
    коjих се дуж XY види под углом A · α/2
    B + C · π/2D. */


    /*
    W22 Ако су дати тачка X и круг k1 могуће jе конструисати круг k2 са центром
    у тачки X коjи изнутра додируjе круг k1; услов недегенерисаности jе да
    je тачка X унутар круга k1 и да тачка X ниjе центар круга k1;
    */
}
