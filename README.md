![Build](https://github.com/ThereAreNoTasksForUs/video_hosting_api/workflows/Build/badge.svg)
![Spring Tests](https://github.com/ThereAreNoTasksForUs/video_hosting_api/workflows/Spring%20Tests/badge.svg)

# Video Hosting Api
API based on Java Spring framework

## Run
Enter your S3 keys into docker-compose file
docker-compose up --build -d
  
The container is going to be on port 8080
 
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
### Channel
    /api/channels - Getting all the channels. GET request.
    /api/channels/owned - Getting all the owned channels. GET request.
    /api/channel/{id} - Getting a channel with its id. GET request.
    /api/channel/create - Creating a channel. POST request. JSON Example: {"name": "example", "info": "example"}
    /api/channel/{id}/update - Updating channel's data. POST request. JSON Example: {"name": "example", "info": "example"}
    /api/channel/{id}/subscribe - Subscription to a channel. POST request.
    /api/channel/{id}/unsubscribe - Unsubscription from a channel. POST request.
### Profile
    /api/profiles - Getting all the profiles. GET request.
    /api/profile - Getting the current profile. GET request.
    /api/profile/id/{id} - Getting a profile with its id. GET request.
    /api/profile/upload/avatar - Uploading an avatar. POST request. form-data: key = "file"; value = jpg, png or gif pictures.
    /api/profile/update - Updating profiles's data. POST request. JSON Example: {"aboutProfileInfo": "example", "gender": "M/F", "country": "example", "customUrl": "example", "isPrivateSublist": "false/true"}
### Video
    /api/videos - Getting all the videos. GET request.
    /api/video/{id} - Getting a video by its id. GET request.
    /api/video/{id}/like - Setting like. POST request.
    /api/video/{id}/dislike - Setting dislike. POST request.
    /api/video/{id}/comment - Commenting a video. POST request. JSON Example: {"commentBody": "example"}
    /api/channel/{id}/videos - Getting all the videos of a channel. GET request.
    /api/profiles/{id}/likedvideos - Getting all the profile's liked videos. GET request.
    /api/channel/{id}/upload/video - Uploading a video. POST request.
    /api/video/{id}/update/name - Updating a video name. POST request. JSON Example: {"name": "example"}
