![Build](https://github.com/PickBas/video_hosting_api/workflows/Build/badge.svg)
![Spring Tests](https://github.com/PickBas/video_hosting_api/workflows/Spring%20Tests/badge.svg)
![license](https://img.shields.io/badge/license-MIT-brightgreen)

# Video Hosting Api
API based on Java Spring framework

## How to run the API
### Docker
Enter your S3 keys into the docker-compose.yml file which is in the root of the repository.
Next, open your terminal, go to the video_hosting_api folder. Then, run the command:

      docker-compose up --build -d
  
If you did everything correctly, the API should be up and running on port 8080.
### Localhost
Open video_hosting_api folder in an IDE (e.g. IntelliJ IDEA). Add environment variables: AMAZON_ACCESS_KEY, AMAZON_SECRET_KEY, MYSQL_URL. The first one is the access key for your S3 storage. The second one is the secret key for your S3 storage. The third one is the URL for the database. If you run containerized MySQL, URL should look like this:

    mysql://localhost:3306/video_hosting_db
    
Then run the API. If you did everything correctly, the API should be up and running on port 8080.
 
## URLs
A token which you get after logging in has to be provided as a request header with any request (except Sign in/up). Example: header key - Authorization, value - TOKEN_<token>
### Sign in/up:
    /api/auth/register - Registration. POST request. JSON Example: {"email": "example@example.com", "username": "example", "password": "example"}
    /api/auth/login - Authentication. POST request. JSON Example: {"username": "example", "password": "example"}
### User
    /api/users - Getting all the users. GET request.
    /api/user - Getting the current user. GET request.
    /api/user/id/{id} - Getting a user by its id. GET request.
    /api/user/update - Updating user's first name and last name. POST request. JSON Example: {"firstName": "example", "lastName": "example"}
### Profile
    /api/profiles - Getting all the profiles. GET request.
    /api/profile - Getting the current profile. GET request.
    /api/profile/id/{id} - Getting a profile with its id. GET request.
    /api/profile/upload/avatar - Updating an avatar. POST request. form-data: key = "file"; value = jpg, png or gif pictures.
    /api/profile/{id}/download/avatar - Downloading avatar. GET request.
    /api/profile/update - Updating profiles's data. POST request. JSON Example: {"aboutProfileInfo": "example", "gender": "M/F", "country": "example", "customUrl": "example", "isPrivateSublist": "false/true"}
    /api/profile/{id}/likedvideos - Getting all the profile's liked videos. GET request.
### Channel
    /api/channels - Getting all the channels. GET request.
    /api/channels/owned - Getting all the owned channels. GET request.
    /api/channel/{id} - Getting a channel with its id. GET request.
    /api/channel/{id} - Deletion a channel with its id. DELETE request.
    /api/channel/create - Creating a channel. POST request. JSON Example: {"name": "example", "info": "example"}
    /api/channel/{id}/update - Updating channel's data. POST request. JSON Example: {"name": "example", "info": "example"}
    /api/channel/{id}/subscribe - Subscription to a channel. POST request.
    /api/channel/{id}/unsubscribe - Unsubscription from a channel. POST request.
    /api/channel/{id}/videos - Getting all the videos of a channel. GET request.
    /api/channel/{id}/upload/video - Uploading a video. POST request.
    /api/channel/{id}/upload/avatar - Updating an avatar. POST request. form-data: key = "file"; value = jpg, png or gif pictures.
### Video
    /api/videos - Getting all the videos. GET request.
    /api/video/{id} - Getting a video by its id. GET request.
    /api/video/{id} - Deletion a video by its id. DELETE request.
    /api/video/{id}/like - Setting like. POST request.
    /api/video/{id}/dislike - Setting dislike. POST request.
    /api/video/{id}/comment - Commenting a video. POST request. JSON Example: {"commentBody": "example"}
    /api/video/{id}/get/comments - Get all comments on a video. GET request.
    /api/video/{video_id}/comment/{comment_id} - Delete comment. DELETE request.
    /api/video/{id}/update/name - Updating a video name. POST request. JSON Example: {"name": "example"}
