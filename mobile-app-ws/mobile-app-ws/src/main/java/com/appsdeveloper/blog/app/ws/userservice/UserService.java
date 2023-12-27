package com.appsdeveloper.blog.app.ws.userservice;

import com.appsdeveloper.blog.app.ws.ui.model.request.UserDetailsRequestModel;
import com.appsdeveloper.blog.app.ws.ui.model.response.UserRest;

public interface UserService {

	UserRest createUser(UserDetailsRequestModel userDetails);
}
