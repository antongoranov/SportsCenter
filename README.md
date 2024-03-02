# Sports Center
Welcome to the Sports Center application, a Spring Boot MVC web application designed for seamless sport class bookings.</br> 
This project allows users to register, book sport classes, manage their bookings, and interact with various features to enhance their experience.

## ER Diagram:

![database](database.jpg)

## Tech Stack
Java </br>
Spring Boot MVC </br>
Spring Security </br>
OAuth 2.0 </br>
Spring Data JPA </br>
Hibernate </br>
MySQL </br>
JUnit </br>
Maven </br>
Lombok </br>
Mapstruct </br>
Cloudinary </br>
Bootstrap </br>
Thymeleaf </br>
HTML CSS JS </br>

## Features 
### User Features
**User Registration:** Users can register for an account, providing necessary details. </br>
**Sport Class Schedule:** View a table displaying the schedule of available sport classes. </br>
**Booking:** Users can book a spot in a sport class from the available schedule. </br>
**Booking Management:** Users can see their booked classes, cancel bookings, and access generated QR Codes for hall entrance. </br>
**Instructor Selection:** View a list of instructors, choose based on preferences. </br>
**Profile Management:** Users can view and update their profile picture and other details. </br>
**Password Reset:** Option to reset passwords securely. </br>
**Google OAuth Login:** Users can log in using their Google accounts. </br>
**Email Notifications:** Receive emails for new registrations and password resets. </br>

### Admin Features
**Booking Management:** Admins can Accept or Cancel user bookings, searching for users by username. </br>
**User Management:** Edit or Delete users. </br>
**User Role Management:** Control and change user roles. </br>
**Instructor Management:** Add new instructors to the system. </br>
**Sport Class Management:** Add new Sport Classes considering current schedule. </br>
**Sport Management:** Add or Delete sports. </br>

### Other features
#### Scheduled Tasks
The system executes the following tasks at specified intervals: </br>

Everyday at midnight:</br>
Sets the status of unused bookings to expired and delete their QR codes.</br>
Reset the capacity of passed classes for the next week bookings.</br>

Every Sunday:</br>
Delete all non-active bookings and their QR codes to reset them for the next week.</br>
Delete expired password reset tokens for the past week.</br>

#### Interceptors
Having two interceptors:</br>
1 restricting access to the app during Maintenance window in Sunday between 23:00 PM and 00:00 AM</br>
1 to collect information for all requests sent to the system.</br>


**Implements error handling and data validation both client and server-side.** </br>

**Has a REST Controller that is called upon by the JavaScript fetch API to asynchronously load the user's bookings to their profile.**</br>

## Testing
Achieved 60% code coverage, having both Integration and Unit tests. </br>

## Setting Up the Environment
Follow these steps to set up environment variables in the application.yml file for local development:</br>

**Cloudinary Account:**
Set cloudinary.cloud-name, cloudinary.api-key, and cloudinary.api-secret with your Cloudinary account details.</br>

**Database Configuration:**
Set spring.datasource.url, spring.datasource.username, and spring.datasource.password for your database.</br>

**Email SMTP Server:**
Set spring.mail.host, spring.mail.port, spring.mail.username, and spring.mail.password for your email SMTP server.</br>

## Running the Application Locally
Clone the repository: git clone https://github.com/antongoranov/SportsCenter.git </br>
Navigate to the project directory: cd SportsCenter </br>
Open src/main/resources/application.yml and set the required environment variables. </br>
Build the project: ./mvnw clean install </br>
Run the application: ./mvnw spring-boot:run </br>
Visit http://localhost:8080 in your web browser to access the Sports Center application </br>

#### Feel free to explore, book classes, and manage the system as a user or an admin!



