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
    // the following two function checks if the current location is in geo-fence area
    // function to find distance between two latitude and longitude
    public static double getDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(Math.abs(lat2 - lat1));
        double lonDistance = Math.toRadians(Math.abs(lon2 - lon1));

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = R * c * 1000; // distance in meter

        distance = Math.pow(distance, 2);
        return Math.sqrt(distance);
    }
    
    private boolean isInCampus(double x,double y){

        // the lat and long of : Webel-IT Park
        final double X = 22.9611167;
        final double Y = 88.4335215;
        
        // radiusToCheck is defined as 200
        // radius up to 200 m is checked
        if(getDistance(X,Y,x,y) <= radiusToCheck){
            return true;
        }
        else{
            return false;
        }
    }

```
## Build With
* [Google Api Client](https://developers.google.com/android/reference/com/google/android/gms/common/api/GoogleApiClient)
* [Fused Location Provider Client](https://developers.google.com/android/reference/com/google/android/gms/location/FusedLocationProviderClient)
## Authors
* **Jyotirmoy Paul** - Initial work - [jyotirmoy-paul](https://github.com/jyotirmoy-paul)

