package com.example.template.service;

import com.example.template.dto.request.CreateUserRequest;
import com.example.template.dto.request.PasswordCreationRequest;
import com.example.template.dto.response.UserResponse;
import com.example.template.entity.User;

public interface UserService {
    /**
     * @param email - Input email
     * @return boolean indicating if the email already exited or not
     */
    boolean isEmailExistAndActive(String email);

    /**
     * @param id - Input UserId
     * @return User Object on a given userId
     */
    User fetchUserById(String id);

    /**
     * @return UserResponse Object - Info currentUser
     */
    UserResponse fetchMyInfo();

    /**
     * @param request - password
     */
    void createPassword(PasswordCreationRequest request);

    /**
     * @param email - Input email
     * @return User Object on a given email
     */
    User fetchUserByEmail(String email);

    /**
     * @param id - Input UserId
     * @return User Details based on a given data updated to database
     */
    UserResponse fetchResUserDtoById(String id) ;

    /**
     * @param spec - filter
     * @param pageable - page, size, sort(field,asc(desc))
     * @return ResultPaginationDTO based on a given spec and pageable
     */
//    ResultPaginationDTO fetchAllUser(Specification<User> spec, Pageable pageable);

    /**
     * @param newUser - Input CreateUserRequest Object
     * @return User Details based on a given data saved to database
     */
    UserResponse handleCreateUser(CreateUserRequest newUser);

//    /**
//     * @param id - Input UserId
//     * @param updateUserRequest - Input UpdateUserRequest Object
//     * @return User Details based on a given data updated to database
//     */
//    UserResponse handleUpdateUser(String id, UpdateUserRequest updateUserRequest);


//    /**updated
//     * @param id - Input UserId
//     * @param lockRequest - Input LockRequest Object
//     * @return User Details based on a given data updated to database
//     */
//    UserResponse handlerLockUser(String id, LockRequest lockRequest);

//    List<SimpInfoUserResponse> fetchUserByIdIn(List<String> ids);

    Boolean forgotPassword(String email);

//    List<UserPointResponse> getLeaderBoard();

}