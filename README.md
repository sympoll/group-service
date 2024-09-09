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
           "profilePictureUrl": null,
           "profileBannerUrl": null,
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
     - `username` (String): The username of the user to add to the group.
   - **Sample Request:**
     - URL: `/api/group/add-member?groupId=1a2b3c&username=user123`
   - **Response:**
     - `MemberResponse`: Contains information about the added member.
       
       ```json
       {
       "userData": {
           "userId": "ca98fcb8-28b3-4708-becd-9114c9bba4b3",
           "username": "user123",
           "email": "user123@gmail.com",
           "profilePictureUrl": null
       },
       "roleName": "Member"
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
               "profilePictureUrl": null,
               "profileBannerUrl": null,
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
               "profilePictureUrl": null,
               "profileBannerUrl": null,
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
              "userData": {
                  "userId": "9edc3d7f-b5cb-4e8b-9a9b-3b4b4d91bf0b",
                  "username": "user345",
                  "email": "user345@gmail.com",
                  "profilePictureUrl": null
              },
              "roleName": "Member"
          },
          {
              "userData": {
                  "userId": "b1f8e925-2129-473d-bc09-b3a2a331f839",
                  "username": "user123",
                  "email": "user123@gmail.com",
                  "profilePictureUrl": null
              },
              "roleName": "Admin"
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

---   

### 10. **Get Groups by Member ID**

   - **URL:** `/api/group/by-member-id`
   - **Method:** `GET`
   - **Description:** Retrieves all groups associated with a specific member ID.
   - **Query Parameters:**
     - `memberId` (UUID): The ID of the member whose groups will be retrieved.
   - **Sample Request:** `/api/group/by-member-id?memberId=d290f1ee-6c54-4b01-90e6-d701748f0851`
   - **Response:**
     - `List<GroupResponse>`: A list of groups associated with the member.
   - **Response Status:** `200 OK`
   - **Logs:**
     - `INFO`: Logs when a request to retrieve groups by member ID is received.

---

### 11. **Add User Role**

   - **URL:** `/api/group/user-role`
   - **Method:** `POST`
   - **Description:** Adds a new role to a user in a specific group.
   - **Request Body:**   
     - `UserRoleCreateRequest`: Contains the user ID, group ID, and the role name.
       
       ```json
       {
           "userId": "d290f1ee-6c54-4b01-90e6-d701748f0851",
           "groupId": "1a2b3c",
           "roleName": "Admin"
       }
       ```
   - **Response:**   
     - `UserRoleResponse`: Contains the user ID and the new role.
   - **Response Status:** `201 CREATED`
   - **Logs:**
     - `INFO`: Logs when a request to add a user role is received.

---

### 12. **Get User Role in a Group**

   - **URL:** `/api/group/user-role`
   - **Method:** `GET`
   - **Description:** Retrieves the role of a specific user in a group.
   - **Query Parameters:**
     - `userId` (UUID): The ID of the user.
     - `groupId` (String): The ID of the group.
   - **Sample Request:** `/api/group/user-role?userId=d290f1ee-6c54-4b01-90e6-d701748f0851&groupId=1a2b3c`
   - **Response:**   
     - `String`: The role name of the user in the group.
   - **Response Status:** `200 OK`
   - **Logs:**
     - `INFO`: Logs when a request to get a user's role is received.

---

### 13. **Change User Role in a Group**

   - **URL:** `/api/group/user-role`
   - **Method:** `PUT`
   - **Description:** Changes the role of a user in a specific group.
   - **Request Body:**   
     - `UserRoleChangeRequest`: Contains the user ID, group ID, and the new role name.
       
       ```json
       {
           "userId": "d290f1ee-6c54-4b01-90e6-d701748f0851",
           "groupId": "1a2b3c",
           "newRoleName": "Moderator"
       }
       ```
   - **Response:**   
     - `String`: The previous role of the user in the group.
   - **Response Status:** `200 OK`
   - **Logs:**
     - `INFO`: Logs when a request to change a user's role is received.

---

### 14. **Delete User Role**

   - **URL:** `/api/group/user-role`
   - **Method:** `DELETE`
   - **Description:** Deletes a user's role from a group.
   - **Request Body:**   
     - `UserRoleDeleteRequest`: Contains the user ID, group ID, and the role name.
       
       ```json
       {
           "userId": "d290f1ee-6c54-4b01-90e6-d701748f0851",
           "groupId": "1a2b3c",
           "roleName": "Member"
       }
       ```
   - **Response:**   
     - `UserRoleDeleteResponse`: Contains the user ID and the deleted role name.
   - **Response Status:** `200 OK`
   - **Logs:**
     - `INFO`: Logs when a request to delete a user role is received.

---

### 15. **Get Group Name by Group ID**

   - **URL:** `/api/group/name/by-group-id`
   - **Method:** `GET`
   - **Description:** Retrieves the name of a group by its ID.
   - **Query Parameters:**
     - `groupId` (String): The ID of the group.
   - **Sample Request:** `/api/group/name/by-group-id?groupId=1a2b3c`
   - **Response:**   
     - `GroupNameResponse`: Contains the group ID and group name.
   - **Response Status:** `200 OK`
   - **Logs:**
     - `INFO`: Logs when a request to retrieve the group name by ID is received.

---

### 16. **Get Group by ID**

   - **URL:** `/api/group/by-group-id`
   - **Method:** `GET`
   - **Description:** Retrieves group details by its ID.
   - **Query Parameters:**
     - `groupId` (String): The ID of the group.
   - **Sample Request:** `/api/group/by-group-id?groupId=1a2b3c`
   - **Response:**   
     - `GroupResponse`: Contains details of the group.
   - **Response Status:** `200 OK`
   - **Logs:**
     - `INFO`: Logs when a request to retrieve a group by its ID is received.

---

### 17. **Get All User Groups**

   - **URL:** `/api/group/all-user-groups`
   - **Method:** `GET`
   - **Description:** Retrieves all group IDs that a specific user belongs to.
   - **Query Parameters:**
     - `userId` (UUID): The ID of the user.
   - **Sample Request:** `/api/group/all-user-groups?userId=d290f1ee-6c54-4b01-90e6-d701748f0851`
   - **Response:**   
     - `UserGroupsResponse`: Contains a list of group IDs.
   - **Response Status:** `200 OK`
   - **Logs:**
     - `INFO`: Logs when a request to retrieve all groups of a user is received.

---

### 18. **Get Groups Data by IDs**

   - **URL:** `/api/group/groups-list`
   - **Method:** `POST`
   - **Description:** Retrieves data of multiple groups by their IDs.
   - **Request Body:**   
     - `List<String>`: A list of group IDs.
   - **Sample Request:**
     
       ```json
       {
           "groupIds": ["1a2b3c", "4d5e6f"]
       }
       ```
   - **Response:**   
     - `List<GroupResponse>`: Contains information for each group.
   - **Response Status:** `200 OK`
   - **Logs:**
     - `INFO`: Logs when a request to retrieve groups' data by IDs is received.

---

### 19. **Update Group Profile Picture URL**

   - **URL:** `/api/group/profile-picture-url`
   - **Method:** `POST`
   - **Description:** Updates the profile picture URL for a group.
   - **Request Body:**   
     - `GroupUpdateProfilePictureUrlRequest`: Contains the group ID and the new profile picture URL.
       
       ```json
       {
           "groupId": "1a2b3c",
           "profilePictureUrl": "http://localhost/api/media/picture.jpg"
       }
       ```
   - **Response:**   
     - `String`: The ID of the group that was updated.
   - **Response Status:** `200 OK`
   - **Logs:**
     - `INFO`: Logs when a request to update a group's profile picture URL is received.

---

### 20. **Update Group Profile Banner URL**

   - **URL:** `/api/group/profile-banner-url`
   - **Method:** `POST`
   - **Description:** Updates the profile banner URL for a group.
   - **Request Body:**   
     - `GroupUpdateProfileBannerUrlRequest`: Contains the group ID and the new profile banner URL.
       
       ```json
       {
           "groupId": "1a2b3c",
           "profileBannerUrl": "http://localhost/api/media/banner.jpg"
       }
       ```
   - **Response:**   
     - `String`: The ID of the group that was updated.
   - **Response Status:** `200 OK`
   - **Logs:**
     - `INFO`: Logs when a request to update a group's profile banner URL is received.

---

### 21. **Update Group Profile Description**

   - **URL:** `/api/group/description`
   - **Method:** `POST`
   - **Description:** Updates the description for a group.
   - **Request Body:**   
     - `GroupUpdateDescriptionRequest`: Contains the group ID and the new description.
       
       ```json
       {
           "groupId": "1a2b3c",
           "description": "A group for expert developers."
       }
       ```
   - **Response:**   
     - `String`: The ID of the group that was updated.
   - **Response Status:** `200 OK`
   - **Logs:**
     - `INFO`: Logs when a request
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
