# mvp-sandbox
Android sample app based on Visum. The backend is located at https://safe-reaches-4393.herokuapp.com/

Backend sources: https://github.com/ragnor-rs/mvp-sandbox-backend

## Model

Let's discuss a single feature, to clear up, how visual framework works.

Large part of every MVP application is Model.
Visum helps (but not enforces) you to build your model layer with [StorIo](https://github.com/pushtorefresh/storio) and [Retrofit](http://square.github.io/retrofit/) libraries [reactive way](https://github.com/ReactiveX/RxJava). 
And it is covered by current example.

### Providing data
Sandbox application loads data from api, saves it to db and displays in View.
RetrofitService (remote service) and StorIoServices (local service) provide data from api and database respectively.

CachedService unites remote and local services and provides data to presenter. It also saves data to both DB and server.
