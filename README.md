[![mvn](https://github.com/PickBas/video_hosting_api/actions/workflows/mvn.yml/badge.svg)](https://github.com/PickBas/video_hosting_api/actions/workflows/mvn.yml)
![license](https://img.shields.io/badge/license-MIT-brightgreen)

# Video Hosting Api
API based on Java Spring framework

## Running the API
### Docker
* Enter your S3 keys into docker-compose.yml  
* Open your terminal, go to video_hosting_api folder, run following command:


      docker-compose up --build -d
  
If you did everything correctly, the API should be up and running on port 8080.
### Locally
Open video_hosting_api folder in IDE (e.g. IntelliJ IDEA). Add environment variables:

      AMAZON_ACCESS_KEY - access key for S3 storage;
      AMAZON_SECRET_KEY - secret key for S3 storage;
    
Then run the API. If you did everything correctly, the API should be up and running on port 8080.
 
## URLs
A token which you get after login has to be provided as a request header with any request (except Sign in/up). Example: header key - Authorization, value - "Bearer your_token".
You can find API documentation here by visiting `swagger-ui/index.html` endpoint.
