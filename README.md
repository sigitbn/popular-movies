# Popular Movies
Popular Movies is an app to help users discover popular and recent movies

## Installation
This app requires API key for TMDB.org. You can get it from here [TMDB API](https://www.themoviedb.org/documentation/api "TMDB API")

Before running the app please :

Replace `secretsProperties['TheMovieDbApiKey']` with your TMDB API key value in `app/build.gradle`


```
        each {
            buildConfigField 'String', 'TheMovieDbApiKey', secretsProperties['TheMovieDbApiKey']
        }

```
### OR

Create a 'secrets.properties' file on your project root, copy the following code bellow, and replace 'TheMovieDbApiKey' with your TMDB API key

```
TheMovieDbApiKey = 'TheMovieDbApiKey'

```

