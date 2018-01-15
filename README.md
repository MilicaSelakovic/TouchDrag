## Touch&Drag
### Introduction
This is android application for geometric constructions. In this application a user can construct geometric objects just by drawing sketches. The application will automaticly recognize the intention of the user and instead of sketch, it will draw geometric objects.
There is three modes:
  <img src="https://github.com/MilicaSelakovic/master/blob/master/app/src/main/ic_helpDraw-web.png" width="48"> Construction mode (default)
  <img src="https://github.com/MilicaSelakovic/master/blob/master/app/src/main/ic_helpMove-web.png" width="48"> Drag mode
  <img src="https://github.com/MilicaSelakovic/master/blob/master/app/src/main/ic_helpSelect-web.png" width="48"> Selection mode
### Construction mode
In this mode, the user constructs geometric objects (Points, Lines, Circles, Polygons, and Triangles) by drawing them on the screen. During the drawing of an object a temporary dashed object is shown. The dashed object to what would be recognized if the user stops drawing.
#### Basic constructions
 - Point 

The point can be constructed just by taping on the screen.
 - Line

Line is constructed by drawing a streight path on the canvas
 - Circle 
 
Circle is constructed by drawing path on canvas that is close to part of circle
 - Polygon

Drawing the path that is similar to polygon the user will draw the polygon. All polygons are closed (first and the last vertex are connected). In the case when the last vertex is almost on the line between fist and next to the last vertex then the last vertex is dismissed.
 - Significant objects

The tool checks relationships between the object that was recognized last and the existing geometric objects.
For instance, if the drawn line is close to an altitude of an existing triangle, then the drawn line is updated (docked) to be that altitude, as it was,
likely, the intention of the user. In that case, information that line will be
recognized as altitude of the triangle is shown.

![Altitude](https://github.com/MilicaSelakovic/master/blob/master/app/src/main/ic_altitude-web.png)

### Drag mode
Constructed points are points that are recognized during the construction as an intersection of some objects or as a significant point of a triangle. All points that are not constructed are marked as free. All free points can be dragged in this mode.
### Selection mode
By entering this mode a user can change which points of the triangle are free. 
Points can be marked as free (green), be candidates to become free (yellow) and be other points witch can't become free points (red). By taping a green point it will become yellow and by taping yellow it will become green. Red points can become yellow when some green point become yellow.
### Utilities 
![new](https://github.com/MilicaSelakovic/master/blob/master/app/src/main/res/drawable-hdpi/new_light.png) New project

![help](https://github.com/MilicaSelakovic/master/blob/master/app/src/main/res/drawable-hdpi/help_light.png) Help

![save](https://github.com/MilicaSelakovic/master/blob/master/app/src/main/res/drawable-hdpi/save_light.png) Save project

![load](https://github.com/MilicaSelakovic/master/blob/master/app/src/main/res/drawable-hdpi/load_light.png) Load project

![center](https://github.com/MilicaSelakovic/master/blob/master/app/src/main/res/drawable-hdpi/center_light.png) Center

![settings](https://github.com/MilicaSelakovic/master/blob/master/app/src/main/res/drawable-hdpi/sett_light.png) Settings

![undo](https://github.com/MilicaSelakovic/master/blob/master/app/src/main/res/drawable-hdpi/undo_light.png) ![redo](https://github.com/MilicaSelakovic/master/blob/master/app/src/main/res/drawable-hdpi/redo_light.png) Undo and redo
