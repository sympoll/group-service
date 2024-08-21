# Group Service

## Overview

The Group Service is a REST API controller designed to manage group operations within a Kubernetes cluster. This service interacts with a PostgreSQL database to handle requests related to creating, updating, deleting, and retrieving group data. It provides various endpoints to manage groups and their members, ensuring that all operations are performed efficiently and securely.   

<br />   

## Endpoints

### 1. **Health Check**

   - **URL:** `/api/group/health`
   - **Method:** `GET`
   - **Description:** Performs a health check of the Group Service.
   - **Sample Request:** `/api/group/health`
   - **Response:**
     - `String`: "OK" if the service is running properly.
       
       ```json
       "OK"
       ```
   - **Response Status:** `200 OK`
   - **Logs:**
     - `INFO`: Logs when a health check request is received.

---   


### 2. **Create a Group**

   - **URL:** `/api/group`   
   - **Method:** `POST`   
   - **Description:** Creates a new group in the database.
   - **Request Body:**   
     - `GroupCreateRequest`: Contains the details of the group to be created.
       
       ```json
       {
           "groupId": "1a2b3c",
           "groupName": "Developers",
           "description": "A group for all developers.",
           "creatorId": "d290f1ee-6c54-4b01-90e6-d701748f0851"
       }
       ```   
   - **Response:**   
     - `GroupResponse`: Contains full information about the created group.
       
       ```json
       {
           "groupId": "1a2b3c",
           "groupName": "Developers",
           "description": "A group for all developers.",
           "creatorId": "d290f1ee-6c54-4b01-90e6-d701748f0851",
           "timeCreated": "2024-08-20T12:34:56",
           "membersList": []
       }
       ```
   - **Response Status:** `201 CREATED`
   - **Logs:**
     - `INFO`: Logs when a request to create a group is received.
     - `DEBUG`: Logs the details of the group to be created.

---   

### 3. **Add a Member to a Group**

   - **URL:** `/api/group/add-member`
   - **Method:** `POST`
   - **Description:** Adds a new member to an existing group.
   - **Query Parameters:**
     - `groupId` (String): The ID of the group to which the user will be added.
     - `userId` (UUID): The ID of the user to add to the group.
   - **Sample Request:**
     - URL: `/api/group/add-member?groupId=1a2b3c&userId=d290f1ee-6c54-4b01-90e6-d701748f0851`
   - **Response:**
     - `MemberResponse`: Contains information about the added member.
       
       ```json
       {
           "userId": "d290f1ee-6c54-4b01-90e6-d701748f0851"
       }
       ```
   - **Response Status:** `200 OK`
   - **Logs:**
     - `INFO`: Logs when a request to add a member is received.

---   
   
### 4. **Remove a Member from a Group**

   - **URL:** `/api/group/remove-member`
   - **Method:** `DELETE`
   - **Description:** Removes a member from a group.
   - **Query Parameters:**
     - `groupId` (String): The ID of the group from which the user will be removed.
     - `userId` (UUID): The ID of the user to remove from the group.
   - **Sample Request:**
     - URL: `/api/group/remove-member?groupId=1a2b3c&userId=d290f1ee-6c54-4b01-90e6-d701748f0851`
   - **Response:**
     - `MemberResponse`: Contains information about the removed member.
       
       ```json
       {
           "userId": "d290f1ee-6c54-4b01-90e6-d701748f0851"
       }
       ```
   - **Response Status:** `200 OK`
   - **Logs:**
     - `INFO`: Logs when a request to remove a member is received.

---   

### 5. **Get All Groups**

   - **URL:** `/api/group/all`
   - **Method:** `GET`
   - **Description:** Retrieves all groups from the database.
   - **Sample Request:** `/api/group/all`
   - **Response:**
     - `List<GroupResponse>`: A list containing information for all groups.
       
       ```json
       [
           {
               "groupId": "1a2b3c",
               "groupName": "Developers",
               "description": "A group for all developers.",
               "creatorId": "d290f1ee-6c54-4b01-90e6-d701748f0851",
               "timeCreated": "2024-08-20T12:34:56",
               "membersList": [
                   {
                       "userId": "d290f1ee-6c54-4b01-90e6-d701748f0851"
                   }
               ]
           },
           {
               "groupId": "4d5e6f",
               "groupName": "Designers",
               "description": "A group for all designers.",
               "creatorId": "a12b3c4d-5e6f-7g8h-9i10-jk11lm12no13",
               "timeCreated": "2024-08-19T11:23:45",
               "membersList": []
           }
       ]
       ```
   - **Response Status:** `200 OK`
   - **Logs:**
     - `INFO`: Logs when a request to retrieve all groups is received.

---   

### 6. **Get All Members of a Group**

   - **URL:** `/api/group/members`
   - **Method:** `GET`
   - **Description:** Retrieves all members of a specified group.
   - **Query Parameters:**
     - `groupId` (String): The ID of the group whose members will be retrieved.
   - **Sample Request:** `/api/group/members?groupId=1a2b3c`
   - **Response:**
     - `List<MemberResponse>`: A list containing information for all members of the specified group.
       
       ```json
       [
           {
               "userId": "d290f1ee-6c54-4b01-90e6-d701748f0851"
           },
           {
               "userId": "e36g4h5i-7j8k-9l10-mn11-op12qr13st14"
           }
       ]
       ```
   - **Response Status:** `200 OK`
   - **Logs:**
     - `INFO`: Logs when a request to retrieve members of a group is received.

---   

### 7. **Delete a Group**

   - **URL:** `/api/group`
   - **Method:** `DELETE`
   - **Description:** Deletes a group from the database by its ID.
   - **Query Parameters:**
     - `groupId` (String): The ID of the group to be deleted.
   - **Sample Request:** `/api/group?groupId=1a2b3c`
   - **Response:**
     - `DeleteGroupResponse`: Contains the ID of the group that was deleted.
       
       ```json
       {
           "groupId": "1a2b3c"
       }
       ```
   - **Response Status:** `200 OK`
   - **Logs:**
     - `INFO`: Logs when a request to delete a group is received.

---   

### 8. **Check if a Group ID Exists**

   - **URL:** `/api/group/id`
   - **Method:** `GET`
   - **Description:** Checks whether a group ID exists in the database.
   - **Query Parameters:**
     - `groupId` (String): The ID of the group to check.
   - **Sample Request:** `/api/group/id?groupId=1a2b3c`
   - **Response:**
     - `GroupIdExistsResponse`: Indicates whether the group ID exists.
       
       ```json
       {
           "isExists": true
       }
       ```
   - **Response Status:** `200 OK`
   - **Logs:**
     - `INFO`: Logs when a request to check a group ID is received.

---   

### 9. **Check User's Permission to Delete a Poll**

   - **URL:** `/api/group/user-role/permission/delete`
   - **Method:** `GET`
   - **Description:** Verifies whether a given user has the permission to delete a poll in a specified group.
   - **Query Parameters:**
     - `userId` (UUID): The ID of the user whose permissions are being checked.
     - `groupId` (String): The ID of the group in which the poll deletion permission is being verified.
   - **Sample Request:**
     - URL: `/api/group/user-role/permission/delete?userId=d290f1ee-6c54-4b01-90e6-d701748f0851&groupId=1a2b3c`
   - **Response:**
     - `boolean`: `true` if the user has permission to delete polls in the specified group, `false` otherwise.
       
       ```json
       true
       ```
   - **Response Status:** `200 OK`
   - **Logs:**
     - `INFO`: Logs when a request to check user permission to delete a poll is received.

<br />   


## Service Architecture

- **Controller Layer:** The `ServiceController` class handles HTTP requests and delegates operations to the `GroupService` layer.
- **Service Layer:** The `GroupService` performs the business logic, interacting with the PostgreSQL database to manage group and member data.
- **Database:** The service uses PostgreSQL as the underlying database to persist group and member data.


<br />   


## Logging

The service uses `SLF4J` for logging.   
Key operations such as group creation, deletion, and member management are logged with `INFO` level logs for tracking request handling.   
More detailed `DEBUG` level logs capture the specifics of requests, useful for troubleshooting.

<br />   


## Error Handling

The service uses exception handling mechanisms to manage and respond to errors gracefully. When an error occurs during request processing, the service returns appropriate HTTP status codes along with descriptive error messages to the client. This ensures that clients are informed about what went wrong and can take corrective actions.

Typical error scenarios might include:
* `Invalid Input`: If the input data is invalid or missing required fields, the service returns a 400 Bad Request status with details about the validation error.   
* `Resource Not Found`: If a requested group or member does not exist in the database, the service returns a 404 Not Found status with an appropriate error message.   
* `Conflict`: If there is a conflict, such as trying to create a group with a duplicate ID, the service returns a 409 Conflict status.   
* `Server Errors`: For unexpected server errors, the service returns a 500 Internal Server Error status, ensuring that the issue is logged for further investigation.   
   
These error responses ensure that the service remains robust, secure, and user-friendly, providing clear feedback to API consumers while maintaining operational integrity.
