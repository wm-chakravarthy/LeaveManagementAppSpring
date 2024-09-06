# Application Overview

## 1. Dashboard
The **Dashboard** displays a summary of your leave balance, upcoming holidays, and your team's upcoming leaves. It provides a quick view of your leave status and helps track team availability for better planning.


## 2. Applying for Leave
When applying for leave, I have implemented validation on both the **frontend** and **backend** to ensure that the total leave days are calculated correctly, excluding weekends. The backend API does not accept the total days from the frontend but instead calculates it internally and updates the database accordingly. Additionally, the backend API only returns the leave types specific to **male** and **female** employees based on their eligibility.

## 3. My Teams Tab
In the **"My Teams"** tab, there are two sections:

### I. Leaves Tab
This section displays all approved leave requests. It also includes a **"Details"** button for viewing more information about each employee's leave request.

### II. Requests Tab
This section contains all **pending**, **cancelled**, and **rejected** leave requests.

## 4. Approving/Rejecting Employee Leave Requests
When a manager approves or rejects an employee's leave request, a **dialog box** will appear displaying the employee's details, including their total available leaves, pending leaves, and the specific leave type for which the request was made. This information helps the manager make an informed decision on whether to approve or reject the request.

## 5. Employee Leave Request
When an employee applies for a leave request, the system calls the **EmployeeLeaveSummary API**, which provides a list of the employee's leave summaries, including the leave type, available leaves, and pending leaves. This data is displayed in the leave type selection bar, allowing the employee to view how many leaves are available for each specific leave type before submitting their request.

## 6. Exception Handling
I have handled all exceptions and implemented multiple **catch blocks** in the controllers, ensuring that a specific message and status code are returned for each particular exception.

## 7. Singleton Pattern
I have implemented the **singleton pattern** for repositories, ensuring that only a single instance of the repository is created, even when multiple client requests are made. This helps optimize resource usage and improve performance.

## 8. Response Handling
After converting the response into **JSON** format, I delete the custom POJO objects by assigning them to `null`, allowing the **garbage collector** to free up memory by removing them.

## 9. Client Response Handler
I have implemented a **ClientResponseHandler** in the `util` package to handle sending responses to the client. This functionality is reusable across all servlets, and I have used it within the `finally` block to ensure that the client receives a response, regardless of whether the outcome is successful or an error.

## 10. Session Management
I maintain user sessions using both **cookies** and **HttpSession**, ensuring consistent session management and user authentication throughout the application. I have also implemented an **authentication filter** in my application to manage and enforce authentication requirements effectively.

## 11. Data Transfer Objects (DTOs)
I have written **Data Transfer Objects (DTOs)** to handle requests that need specific data, ensuring that only the necessary information is transferred and processed.

## 12. Login and Logout Servlets
I have implemented both a **login servlet** and a **logout servlet**. Upon login, a random cookie value is created, added to the response, and stored in the database. During logout, the cookie is deleted to ensure proper session management and security.

 1. Dashboard
![LeaveManagementApp - Dashboard](https://github.com/user-attachments/assets/6bae5fd5-5e07-490c-ac2a-7857fb23b1e3)

 2. My Leaves Tab - Leaves Section
![image](https://github.com/user-attachments/assets/b3d35266-36cb-40e7-808b-5005ab7939aa)

 3. When Applies for the leave which has more than the current current leave type limit
![image](https://github.com/user-attachments/assets/4280e8cc-afb5-4bc5-9b00-276e10d17f20)

4. The Details Dialog Box For My Leaves
![image](https://github.com/user-attachments/assets/ec827ea0-77e0-47d5-a43b-e0a5bb36b7df)

5. When applying for the leave, showing available and used leaves on every leave type on specific employee
![image](https://github.com/user-attachments/assets/ee542942-957e-4dd4-9543-8a373d4fe36e)


6. My Team - Leaves Summary, Showing My Team Employee leave summary - available and used leaves on every leave type
![image](https://github.com/user-attachments/assets/f9d892ee-1e7b-4da9-97af-4fbd92bbf28f)


7. Approving a leave request, shows employee leave specific details
![image](https://github.com/user-attachments/assets/fd4a6e42-9e28-4727-9fbb-e5b4d83f6137)

8. On Rejecting a Leave Request Also Shows Employee Leave Summary 
![image](https://github.com/user-attachments/assets/02f370eb-1a37-4dd4-a3ca-a6d419325309)

9. My Team - Leaves Section - shows employee APPROVED, CANCELLED, REJECTED Leave requests
![image](https://github.com/user-attachments/assets/58430c54-a85d-4cab-a0cd-f5d7a8a9d50f)

10. Filter Tab for My team - Leaves section which can shows leave requests based on STATUS 
![image](https://github.com/user-attachments/assets/4be97fa5-3ee9-40fa-9cdc-0b7f28ab3f21)

11. On Clicking the Employee Profile icon or profile name, Employee basic details will come
![image](https://github.com/user-attachments/assets/192bb816-0ce5-44db-a628-65c8a9274d5b)
