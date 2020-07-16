# video_hosting_java_api
API based on Java Spring framework

## Run
  docker-compose up --build -d
  
  The container is going to be on port 8080
 
## URLs
A token which you get after logging in has to be provided as a request header with any request (except Sign in/up). Example: header key - Authorization, value - TOKEN_<token>
### Sign in/up:
    * /api/auth/register - Registration. POST request. JSON Example: {"email": "example@example.com", "username": "example", "password": "example"}
    * /api/auth/login - Authentication. POST request. JSON Example: {"username": "example", "password": "example"}
    * /api/auth/logout - Logging out. POST request. JSON Example: {"logout": "true"}
### User
    * /api/users - Getting all the users. GET request.
    * /api/user - Getting the current user. GET request.
    * /api/user/id/{id} - Getting a user by its id. GET request.
    * /api/user/update - Updating user's first name and last name. POST request. JSON Example: {"firstName": "example", "lastName": "example"}
### Channel
    * /api/channels - Getting all the channels. GET request.
    * /api/channels/owned - Getting all the owned channels. GET request.
    * /api/channel/{id} - Getting a channel with its id. GET request.
    * /api/channel/create - Creating a channel. POST request. JSON Example: {"name": "example", "info": "example"}
    * /api/channel/{id}/update - Updating channel's data. POST request. JSON Example: {"name": "example", "info": "example"}
    * /api/channel/{id}/subscribe - Subscribing to a channel. POST request.
### Profile
    * /api/profiles - Getting all the profiles. GET request.
    * /api/profilee - Getting the current profile. GET request.
    * /api/profile/id/{id} - Getting a profile with its id. GET request.
    * /api/profile/update - Updating profiles's data. POST request. JSON Example: {"aboutProfileInfo": "example", "gender": "M/F", "country": "example", "customUrl": "example", "isPrivateSublist": "false/true"}

