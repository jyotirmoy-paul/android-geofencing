# Geofencing Android App
A demo android app, explains (my version of) geofencing, which can automatically put your phone in Vibrate Mode/ General Mode, depending on your presence in the geofence area, (here it is Webel-IT-Park, Kalyani, West Bengal)

## Getting Started
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites
What things you need to install the software

```
1. Android Studio should be set-up and running on your system.
2. Google Play Services SDK should be installed.
3. An android device running on Jelly Bean or higher.
```

### Installing
Import the project to android studio, build the project, and finally deploy it in a device (or emulator).

## Geofence Detection
The following code snippet, explains how the android device locates if the user is in a destined location
```
    // this function returns the boolean value, if the current lat and long is present in the
    // desired region
    private boolean isInCampus(Double x,Double y){
        /*
        * Make a circle, with center (X,Y) i.e (latitude, longitude) having a radius of 690m (approx)
        * now, if the user is present inside the circle, then satisfying (x,y) on the equation
        * of the circle will give a negative value
        * based on this, it is judged, if the user is in the geo-fence or not
        * */

        // the lat and long of : Webel-It Park
        final Double X = 22.9611869;
        final Double Y = 88.4333625;

        // lat and long of a nearby place at dist of 690m (approx)
        final Double X_01 = 22.96;
        final Double Y_01 = 88.44;

        // getting the radius by finding the distance between the two points
        Double radius = Math.sqrt( Math.pow(X - X_01,2) + Math.pow(Y - Y_01,2));

        // making the circle equation and satisfying the (x,y) point
        Double function = Math.pow(x - X,2) + Math.pow(y - Y,2) - Math.pow(radius,2);

        if(function <= 0){
            // the location is in campus
            return true;
        }
        else{
            // outside campus
            return false;
        }
    }

```
## Build With
* [Google Api Client](https://developers.google.com/android/reference/com/google/android/gms/common/api/GoogleApiClient)
* [Fused Location Provider Client](https://developers.google.com/android/reference/com/google/android/gms/location/FusedLocationProviderClient)
## Authors
* **Jyotirmoy Paul** - Initial work - [jyotirmoy-paul](https://github.com/jyotirmoy-paul)

