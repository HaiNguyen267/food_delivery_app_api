package com.example.lesson3_food_delivery_app_api.controller;

import com.example.lesson3_food_delivery_app_api.dto.request.LoginRequest;
import com.example.lesson3_food_delivery_app_api.dto.response.ErrorResponse;
import com.example.lesson3_food_delivery_app_api.dto.response.AccessToken;
import com.example.lesson3_food_delivery_app_api.dto.response.SuccessResponse;
import com.example.lesson3_food_delivery_app_api.service.AuthService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthController {


    private final AuthService authService;

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login successfully",
                    content = {
                            @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "status": 200,
                                        "message": "Login successful",
                                        "data": {
                                            "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImRlbDFAZC5jb20iLCJyb2xlIjoiREVMSVZFUllfUEFSVE5FUiIsImlhdCI6MTY3NDU2NzA0NiwiZXhwIjoxNjc1MTcxODQ2fQ.aZwO_TQd8CdCx9GIcUt4GS5KL1JBMWyWe_e9mwJ4xG8"
                                        }
                                    }
                                    """)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Wrong username or password",
                    content = {
                            @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "status": 400,
                                        "message": "Wrong username or password"
                                    }
                                    """)
                        )
                    }
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Account is locked",
                    content = {
                            @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "status": 409,
                                        "message": "Account is locked"
                                    }
                                    """)
                        )
                    }
            )
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }
}
