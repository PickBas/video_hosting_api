![Build](https://github.com/PickBas/video_hosting_api/workflows/Build/badge.svg)
![Spring Tests](https://github.com/PickBas/video_hosting_api/workflows/Spring%20Tests/badge.svg)
![license](https://img.shields.io/badge/license-MIT-brightgreen)

# Video Hosting Api
API based on Java Spring framework

## How to run the API
### Docker
Enter your S3 keys into docker-compose.yml file which is at root of the repository.
Next, open your terminal, go to video_hosting_api folder. Then, run following command:

      docker-compose up --build -d
  
If you did everything correctly, the API should be up and running on port 8080.
### Localhost
Open video_hosting_api folder in IDE (e.g. IntelliJ IDEA). Add environment variables:

      AMAZON_ACCESS_KEY - access key for S3 storage;
      AMAZON_SECRET_KEY - secret key for S3 storage;
    
Then run the API. If you did everything correctly, the API should be up and running on port 8080.
 
## URLs
A token which you get after logging in has to be provided as a request header with any request (except Sign in/up). Example: header key - Authorization, value - TOKEN_<token>
### Sign in/up:
    /api/auth/register - Registration. POST request. JSON Example: {"email": "example@example.com", "username": "example", "password": "TestPassword1!"}
    /api/auth/login - Authentication. POST request. JSON Example: {"username": "example", "password": "example"}
    /api/auth/password/update - Updating user password. POST request. Request must be authenticated. JSON Example: {"old_password": "TestOldPassword1!", "updated_password": "TestNewPassword1!"}
    /api/auth/token/refresh - Refreshing access token  GET request with Bearer<space>refresh_token provided
### User
    /api/users - Getting all users. GET request.
    /api/user - Getting current user. GET request.
    /api/user/id/{id} - Getting user by its id. GET request.
    /api/user/update - Updating user's first name and last name. POST request. JSON Example: {"firstName": "example", "lastName": "example"}
### Profile
    /api/profiles - Getting all profiles. GET request.
    /api/profile - Getting current profile. GET request.
    /api/profile/id/{id} - Getting profile with its id. GET request.
    /api/profile/upload/avatar - Updating avatar. POST request. form-data: key = "file"; value = jpg, png or gif pictures.
    /api/profile/{id}/download/avatar - Downloading avatar. GET request.
    /api/profile/update - Updating profiles's data. POST request. JSON Example: {"aboutProfileInfo": "example", "gender": "M/F", "country": "example", "customUrl": "example", "isPrivateSublist": "false/true"}
    /api/profile/{id}/likedvideos - Getting all profile's liked videos. GET request.
### Channel
    /api/channels - Getting all channels. GET request.
    /api/channels/owned - Getting all owned channels. GET request.
    /api/channel/{id} - Getting channel with its id. GET request.
    /api/channel/{id} - Deletion channel with its id. DELETE request.
    /api/channel/create - Creating channel. POST request. JSON Example: {"name": "example", "info": "example"}
    /api/channel/{id}/update - Updating channel's data. POST request. JSON Example: {"name": "example", "info": "example"}
    /api/channel/{id}/subscribe - Subscription to a channel. POST request.
    /api/channel/{id}/unsubscribe - Unsubscription from a channel. POST request.
    /api/channel/{id}/videos - Getting all videos of a channel. GET request.
    /api/channel/{id}/upload/video - Uploading video. POST request.
    /api/channel/{id}/upload/avatar - Updating avatar. POST request. form-data: key = "file"; value = jpg, png or gif pictures.
    /api/channel/{id}/download/avatar - Downloading avatar. GET request.
### Video
    /api/videos - Getting all videos. GET request.
    /api/video/{id} - Getting video by its id. GET request.
    /api/video/{id} - Deletion video by its id. DELETE request.
    /api/video/{id}/like - Setting like. POST request.
    /api/video/{id}/dislike - Setting dislike. POST request.
    /api/video/{id}/comment - Commenting a video. POST request. JSON Example: {"commentBody": "example"}
    /api/video/{id}/get/comments - Get all comments on a video. GET request.
    /api/video/{video_id}/comment/{comment_id} - Delete comment. DELETE request.
    /api/video/{id}/update/name - Updating video name. POST request. JSON Example: {"name": "example"}
