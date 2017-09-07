package com.flow.platform.api.controller;

import com.flow.platform.api.domain.request.LoginParam;
import com.flow.platform.api.domain.user.User;
import com.flow.platform.api.service.user.UserService;
import com.google.common.base.Strings;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author liangpengyv
 */

@RestController
@RequestMapping(path = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * @api {get} List
     * @apiName List users
     * @apiGroup User
     * @apiDescription Get user list with user joined flow and roles
     *
     * @apiSuccessExample {json} Success-Response
     *  [
     *      {
     *          email: xxx,
     *          username: xxx,
     *          createdAt: xxx,
     *          updatedAt: xxx,
     *          flows: [
     *              flow-1,
     *              flow-2,
     *          ],
     *          roles: [
     *              {
     *                  id: xx,
     *                  name: xx,
     *                  description: xx,
     *                  createdBy: xxx,
     *                  createdAt: xxx,
     *                  updatedAt: xxx
     *              }
     *          ]
     *      }
     *  ]
     */
    @GetMapping
    public List<User> list() {
        return userService.list(true, true);
    }

    /**
     * @api {post} /login Login
     * @apiName User Login
     * @apiGroup User
     * @apiDescription Login by request information
     *
     * @apiParamExample {json} Request-Body:
     *     {
     *         "emailOrUsername" : "admin",
     *         "password" : "admin"
     *     }
     *
     * @apiSuccessExample {String} Success-Response:
     *     HTTP/1.1 200 OK
     *     eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBmaXIuaW0iLCJleHAiOjE1MDM3MTk0NjF9.Lv3vSvQTv_qgpuFD8e59t60YbAWafZO6W5cjYMx5lcw
     *
     * @apiErrorExample {json} Error-Response:
     *     HTTP/1.1 400 Bad Request
     *     {
     *         "message": "Illegal login request parameter: username format false"
     *     }
     *
     * @apiErrorExample {json} Error-Response:
     *     HTTP/1.1 500 Internal Server Error
     *     {
     *         "message": "JSON parse error: java.io.EOFException: End of input at line 4 column 1 path $.password; nested exception is com.google.gson.JsonSyntaxException: java.io.EOFException: End of input at line 4 column 1 path $.password"
     *     }
     */
    @PostMapping("/login")
    public String login(@RequestBody LoginParam loginForm) {
        return userService.login(loginForm);
    }

    /**
     * @api {post} /register Register
     * @apiParam {String} roles Param example: ?roles=admin,user
     * @apiParamExample {json} Request-Body:
     *     {
     *         	"email" : "test1@fir.im",
     *         	"username" : "test1",
     *         	"password" : "test1",
     *         	"roleId" : "developer"
     *     }
     * @apiName User Register
     * @apiGroup User
     * @apiDescription Register by request information
     *
     * @apiSuccessExample {json} Success-Response:
     *     HTTP/1.1 200 OK
     *
     * @apiErrorExample {json} Error-Response:
     *     HTTP/1.1 400 Bad Request
     *     {
     *         "message": "Illegal register request parameter: email already exist"
     *     }
     *
     * @apiErrorExample {json} Error-Response:
     *     HTTP/1.1 500 Internal Server Error
     *     {
     *         "message": "JSON parse error: java.io.EOFException: End of input at line 6 column 1 path $.roleId; nested exception is com.google.gson.JsonSyntaxException: java.io.EOFException: End of input at line 6 column 1 path $.roleId"
     *     }
     */
    @PostMapping("/register")
    public void register(@RequestBody User user, @RequestParam(required = false) String roles) {
        Set<String> roleNameSet = new HashSet<>(2);

        // to split roles parameter from xx,xx,xx, to role name set
        if (!Strings.isNullOrEmpty(roles)) {
            roles = roles.trim();

            for (String role : roles.trim().split(",")) {
                role = role.trim();
                if (!Strings.isNullOrEmpty(role)) {
                    roleNameSet.add(role);
                }
            }
        }

        userService.register(user, roleNameSet);
    }

    /**
     * @api {delete} /delete Delete
     * @apiName Delete User
     * @apiGroup User
     * @apiDescription Delete user by email
     *
     * @apiParamExample {json} Request-Example:
     *     [
     *         "test1@fir.im",
     *         "test2@fir.im"
     *     ]
     *
     * @apiSuccessExample {json} Success-Response:
     *     HTTP/1.1 200 OK
     *
     * @apiErrorExample {json} Error-Response:
     *     HTTP/1.1 500 Internal Server Error
     *     {
     *         "message": "JSON parse error: java.io.EOFException: End of input at line 4 column 1 path $[2]; nested exception is com.google.gson.JsonSyntaxException: java.io.EOFException: End of input at line 4 column 1 path $[2]"
     *     }
     */
    @DeleteMapping
    public void delete(@RequestBody List<String> emailList) {
        userService.delete(emailList);
    }
}