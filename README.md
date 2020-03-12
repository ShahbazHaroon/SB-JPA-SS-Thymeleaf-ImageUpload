# SB-JPA-SS-Thymeleaf-ImageUpload

This is an image upload (with Multipart File Upload) application built with Spring Boot, Spring MVC, Spring Data JPA, Spring Security, Thymeleaf, and Bootstrap.

In order to run the application, clone or download it. You can import it into your preferred IDE or you can go to the location where it is saved/downloaded and run mvn clean spring-boot:run . The program will start up on localhost:8070.

The images are set to be saved in a temporary directory, but this can be changed by going to the ImageGalleryApplication class and changing the Path to something else such as Paths.get("/home/YOUR-DIRECTORY/images").

To build with Docker, open up the terminal and use the command docker build -t image-gallery . to build the image. To run the docker container use the command docker run -p 8070:8070 image-gallery:latest. The container should run on localhost:8070.

To Register:
http://localhost:8070/register

To Login:
http://localhost:8070/login

To Find:
http://localhost:8070/find
