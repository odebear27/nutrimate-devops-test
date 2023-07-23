import { useContext, useState } from "react";
import Input from "../components/Input";
import Signin from "../components/Signin";
import BasicAuth from "../promises/BasicAuth";
import axios from "axios";
import { BasicAuthContext } from "../contexts/BasicAuthContext";
import { useNavigate } from "react-router-dom";

function Login() {
  const authContext = useContext(BasicAuthContext);
  const navigate = useNavigate();

  const handlerAuthentication = async () => {
    let response = null;
    try {
      response = await BasicAuth.get("/nutrimate/basicauth", {
        withCredentials: true,
        method: "GET",
        headers: {
          authorization: createBasicAuthToken(
            authContext.credentials.username,
            authContext.credentials.passcode
          ),
        },
      });

      let displayName = response.data.trim();

      authContext.setDisplayName(displayName);
      authContext.setShowAuthError(false);
      authContext.setAuthErrorMessage("");
      authContext.setLoginStatus(true);
      authContext.resetCredentials();
      navigate("/home", { replace: true });

      console.log("Login - GET response", response);
      console.log("Login - GET response", response.data);
      console.log("Login - GET status", response.status);
    } catch (error) {
      let errorResponse = error.response;
      let errorResponseData = errorResponse.data;

      //Need something to catch other errors other than two below
      // 1) Bad Credentials
      // 2) Accoutn locked
      const authErrorMessage =
        errorResponseData.trim().toLowerCase() === "bad credentials"
          ? "You have entered either a wrong username or password!"
          : errorResponseData;

      authContext.setShowAuthError(true);
      authContext.setAuthErrorMessage(authErrorMessage);
      authContext.setLoginStatus(false);
      authContext.setLogoutStatus(false);

      console.log("GET Login error", error.response.data);
    }
  };

  const createBasicAuthToken = (username, password) => {
    return "Basic " + window.btoa(username + ":" + password);
  };

  return (
    <>
      <p className="text-center fs-2 fw-bold text-primary pt-4 mb-0 pb-2">
        Sign in to Nutrimate
      </p>

      {authContext.isShowAuthError && (
        <div>
          <p className="text-danger text-center fs-6 mb-2">
            {authContext.authErrorMessage}
          </p>
        </div>
      )}

      {authContext.isLoggedOut && (
        <div>
          <p className="text-warning text-center fs-6 mb-2">
            You have been logged out.
          </p>
        </div>
      )}

      {/* <div>
        <p class="text-warning text-center fs-6 mb-2">
          You have been logged out.
        </p>
      </div> */}

      <div className="d-flex justify-content-center">
        <div className="w-25 p-2">
          <Input
            divClassName="form-outline mb-2"
            labelName="Username"
            value={authContext.credentials.username}
            type="text"
            className="form-control"
            id="username"
            name="username"
            placeholder="your email or user ID"
            onChange={authContext.handlerUserNameChange}
          />
          <Input
            divClassName="form-outline mb-3"
            labelName="Password"
            value={authContext.credentials.passcode}
            type="password"
            className="form-control"
            id="password"
            name="password"
            placeholder="your password"
            onChange={authContext.handlerPasscodeChange}
          />
          <div className="form-check mb-3">
            <input
              className="form-check-input"
              type="checkbox"
              id="remember-me"
              name="remember-me"
            />
            <label className="form-check-label" htmlFor="remember-me">
              Remember Me
            </label>
          </div>
          <Signin onClick={handlerAuthentication} />
        </div>
      </div>
    </>
  );
}

export default Login;
