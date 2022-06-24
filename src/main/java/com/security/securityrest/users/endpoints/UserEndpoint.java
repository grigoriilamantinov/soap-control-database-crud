package com.security.securityrest.users.endpoints;

import com.security.securityrest.common.AuthoritiesParser;
import com.security.securityrest.common.ValidationControl;
import com.security.securityrest.users.entity.UserEntity;
import com.security.securityrest.users.entity.user.AddUserRequest;
import com.security.securityrest.users.entity.user.AddUserResponse;
import com.security.securityrest.users.entity.user.DeleteUserRequest;
import com.security.securityrest.users.entity.user.DeleteUserResponse;
import com.security.securityrest.users.entity.user.GetAllUserResponse;
import com.security.securityrest.users.entity.user.GetUserByLoginRequest;
import com.security.securityrest.users.entity.user.GetUserByLoginResponse;
import com.security.securityrest.users.entity.user.UpdateUserRequest;
import com.security.securityrest.users.entity.user.UpdateUserResponse;
import com.security.securityrest.users.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.stream.Collectors;


@Endpoint
public class UserEndpoint {
    private static final String NAMESPACE_URI = "http://user.entity.users.securityrest.security.com";

    private final UserService userService;
    private final AuthoritiesParser authoritiesParser;

    public UserEndpoint(
        @Autowired final UserService userService,
        @Autowired final AuthoritiesParser authoritiesParser
    ) {
        this.userService = userService;
        this.authoritiesParser = authoritiesParser;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getUserByLoginRequest")
    @ResponsePayload
    public GetUserByLoginResponse getUserById (@RequestPayload final GetUserByLoginRequest request) {
        GetUserByLoginResponse response = new GetUserByLoginResponse();
        response.setUser(userService.getById(request.getLogin()));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllUserRequest")
    @ResponsePayload
    public GetAllUserResponse getAllUser() {
        GetAllUserResponse response = new GetAllUserResponse();
        response.setUserList(userService.getAll());
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteUserRequest")
    @ResponsePayload
    public DeleteUserResponse deleteUserById(@RequestPayload final DeleteUserRequest request) {
        DeleteUserResponse response = new DeleteUserResponse();
        userService.delete(request.getLogin());
        response.setMessage("Deleted");
        return response;
    }

//    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateUserRequest")
//    @ResponsePayload
//    public UpdateUserResponse updateUser(@RequestPayload final UpdateUserRequest request) {
//        UpdateUserResponse response = new UpdateUserResponse();
//        return response;
//    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "addUserRequest")
    @ResponsePayload
    public AddUserResponse addUser(@RequestPayload final AddUserRequest request) {

        final var isValid = ValidationControl.checkInput(request);
        AddUserResponse response = new AddUserResponse();

        if (isValid) {
            UserEntity userEntity = new UserEntity(
                request.getLogin(),
                request.getName(),
                request.getPassword(),
                authoritiesParser.parsToList(request.getAuthority())
            );
            userService.save(userEntity);

            response.setUser(userService.getById(request.getLogin()));
        }

        response.setSuccess(isValid);
        return response;
    }
}
